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
import java.util.ArrayList;
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

    public List<Uri> getPhotoUriList(Context context) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (File photo : getPhotos()) {
            // TODO: This seems weird, look here: https://stackoverflow.com/questions/3004713/get-content-uri-from-file-path-in-android
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photo);
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

        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.d("Scouting", e.getMessage());
            return null;
        }

        return file;
    }

    public boolean rotateAndCompressPhoto(String teamNumber) {
        try {
            File file = getPhoto(teamNumber);
            FileInputStream fileInputStream = new FileInputStream(file);

            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            if (bitmap == null) {
                Log.d("Scouting", "Bitmap is null");
                return false;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            rotated.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            return true;
        } catch (IOException e) {
            Log.d("Scouting", e.getMessage());
        }
        return false;
    }

    public boolean writeToEndOfFile(eFile fileType, String data) {
        File file = getFile(fileType);
        if (file == null)
            throw new IllegalArgumentException("Invalid eFile");

        if (StringUtils.isEmptyOrNull(data)) {
            return true;
        }

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e("Scouting", "External storage not mounted!");
            return false;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e("Scouting", e.getMessage());
            return false;
        }
    }

    public boolean writeData(String fileNameHeader, String data) {
        if (fileNameHeader == null)
            throw new IllegalArgumentException("Header cannot be null");

        if (StringUtils.isEmptyOrNull(data))
            throw new IllegalArgumentException("Data cannot be null or empty");

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e("Scouting", "External storage not mounted!");
            return false;
        }

        String uniqueId = Scouting.getInstance().getUniqueId();
        File file = new File(getScoutingDirectory(), fileNameHeader + uniqueId + ".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
        } catch (IOException e) {
            Log.e("Scouting", e.getMessage());
            return false;
        }

        return true;
    }
}
