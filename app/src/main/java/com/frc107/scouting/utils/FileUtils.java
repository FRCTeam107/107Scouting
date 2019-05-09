package com.frc107.scouting.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.frc107.scouting.Scouting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private File scoutingDirectory;
    private File photoDirectory;

    private static final String SCOUTING_PATH = Environment.getExternalStorageDirectory() + "/Scouting";
    private static final String PHOTO_PATH = Environment.getExternalStorageDirectory() + "/Scouting/Photos";

    public FileUtils() {
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

    public File getPhoto(String teamNumber) {
        File photo = new File(getPhotoDirectory(), teamNumber + ".jpg");
        if (photo.exists())
            return photo;

        return null;
    }

    public File getFile(String name) {
        File file = new File(getScoutingDirectory(), name);
        if (file.exists())
            return file;

        return null;
    }

    public File getMatchFile() {
        return getFile("Match" + Scouting.getInstance().getUniqueId() + ".csv");
    }

    public File getPitFile() {
        return getFile("Pit" + Scouting.getInstance().getUniqueId() + ".csv");
    }

    public File getConcatMatchFile() {
        return getFile("ConcatenatedMatch.csv");
    }

    public File getConcatPitFile() {
        return getFile("ConcatenatedPit.csv");
    }

    public File createPhotoFile(String teamNumber) {
        File dir = Scouting.FILE_UTILS.getPhotoDirectory();
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
