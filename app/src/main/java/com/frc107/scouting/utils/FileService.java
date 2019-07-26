package com.frc107.scouting.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.frc107.scouting.BuildConfig;
import com.frc107.scouting.Scouting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FileService {
    private File scoutingDirectory;
    private File photoDirectory;

    public enum eFile {
        PIT
    }

    private static final String SCOUTING_PATH = Environment.getExternalStorageDirectory() + "/Scouting";
    private static final String PHOTO_PATH = Environment.getExternalStorageDirectory() + "/Scouting/Photos";

    public FileService() {
        scoutingDirectory = new File(SCOUTING_PATH);
        photoDirectory = new File(PHOTO_PATH);
    }

    public File getScoutingDirectory() {
        if (scoutingDirectory.exists())
            return scoutingDirectory;

        boolean success = scoutingDirectory.mkdir();
        if (success)
            return scoutingDirectory;

        return null;
    }

    public File getPhotoDirectory() {
        if (photoDirectory.exists())
            return photoDirectory;

        boolean success = photoDirectory.mkdir();
        if (success)
            return photoDirectory;

        return null;
    }

    public File[] getFilesInDirectory() {
        return getScoutingDirectory().listFiles();
    }

    public File[] getPhotos() {
        return getPhotoDirectory().listFiles();
    }

    public List<Uri> getPhotoUriList() {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (File photo : getPhotos()) {
            Uri uri = Uri.parse(photo.getAbsolutePath());
            uriList.add(uri);
        }
        return uriList;
    }

    public File getPhoto(String teamNumber) {
        File photo = new File(getPhotoDirectory(), teamNumber + ".jpg");
        if (photo.exists())
            return photo;

        return null;
    }

    public boolean fileExists(eFile fileType) {
        File file = getFile(fileType);
        return file != null && file.exists();
    }

    public File getFile(eFile file) {
        switch (file) {
            case PIT: return getPitFile(false);
            default: return null;
        }
    }

    public File getFile(String name) {
        File file = new File(getScoutingDirectory(), name);
        if (file.exists())
            return file;

        return null;
    }

    private String getFileNamePrefix(eFile file) {
        switch (file) {
            case PIT:
                return "ConcatPit";
            default: return "default";
        }
    }

    public boolean createPitFile(String data) {
        String timeMessage = getCurrentTimeMessage();
        String fileName = "Pit_initialshere_" + timeMessage + ".csv";
        boolean success = writeDataToNewFile(scoutingDirectory, fileName, data);
        return success;
    }

    private static String getCurrentTimeMessage() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return hour + "-" + minute + "-" + second;
    }

    public File getMatchFile(boolean concatenated) {
        if (concatenated) {
            return getFile("ConcatenatedMatch.csv");
        } else {
            return getFile("Match" + Scouting.getInstance().getUniqueId() + ".csv");
        }
    }

    public File getPitFile(boolean concatenated) {
        if (concatenated) {
            return getFile("ConcatenatedMatch.csv");
        } else {
            return getFile("Pit" + Scouting.getInstance().getUniqueId() + ".csv");
        }
    }

    public File createPhotoFile(String teamNumber) {
        if (teamNumber == null)
            throw new IllegalArgumentException("Team number cannot be null");

        File dir = Scouting.FILE_SERVICE.getPhotoDirectory();
        if (dir == null)
            return null;

        File file = new File(dir, teamNumber + ".jpg");

        // We need to be able to have multiple photos of the same robot, so this handles that.
        int num = 1;
        while (file.exists()) {
            num++;
            file = new File(dir, teamNumber + "-" + num + ".jpg");
        }

        try {
            boolean success = file.createNewFile();
            if (!success) {
                Log.d(Scouting.SCOUTING_TAG, "Photo file already exists! Path: \"" + file.getAbsolutePath() + "\"");
                return null;
            }
        } catch (IOException e) {
            Log.d(Scouting.SCOUTING_TAG, e.getMessage());
            return null;
        }

        return file;
    }

    public boolean rotateAndCompressPhoto(String fileName) {
        File file = new File(photoDirectory, fileName);
        if (!file.exists())
            return false;

        try (FileInputStream fileInputStream = new FileInputStream(file);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            if (bitmap == null) {
                Log.d(Scouting.SCOUTING_TAG, "Bitmap is null");
                return false;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            rotated.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);

            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            return true;
        } catch (IOException e) {
            Log.d(Scouting.SCOUTING_TAG, e.getMessage());
        }
        return false;
    }

    public boolean writeToEndOfFile(eFile fileType, String data) {
        File file = getFile(fileType);
        if (file == null)
            throw new IllegalArgumentException("Invalid eFile");

        if (StringUtils.isEmptyOrNull(data))
            return true;

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(Scouting.SCOUTING_TAG, "External storage not mounted!");
            return false;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e(Scouting.SCOUTING_TAG, e.getMessage());
            return false;
        }
    }

    public boolean writeDataToMostRecentFile(eFile file, String data) {
        // todo implement
        return false;
    }

    public boolean writeDataToNewFile(eFile file, String data) {
        String timeMessage = getCurrentTimeMessage();
        String prefix = getFileNamePrefix(file);
        String fileName = prefix + "_initialshere_" + timeMessage + ".csv";
        return writeDataToNewFile(scoutingDirectory, fileName, data);
    }

    private boolean writeDataToNewFile(File directory, String fileName, String data) {
        File file = new File(directory, fileName);
        if (file.exists())
            throw new IllegalStateException("Invalid file name \"" + fileName + "\"; file already exists.");

        if (StringUtils.isEmptyOrNull(data))
            return true;

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(Scouting.SCOUTING_TAG, "External storage not mounted!");
            return false;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e(Scouting.SCOUTING_TAG, e.getMessage());
            return false;
        }
    }
}
