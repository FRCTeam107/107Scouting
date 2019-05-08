package com.frc107.scouting.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.frc107.scouting.Scouting;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    private FileOutputStream fileOutputStream;
    private File directory;

    public FileUtils() {
        directory = new File(Environment.getExternalStorageDirectory() + "/Scouting");
    }

    public boolean directoryExists() {
        return directory.exists();
    }

    public boolean createDirectory() {
        return directory.mkdirs();
    }

    public File[] getFilesInDirectory() {
        return directory.listFiles();
    }

    public File[] getPhotos() {
        File directory = new File(Environment.getExternalStorageDirectory() + "/Scouting/Photos");
        if (!directory.exists() || directory.list() == null)
            return new File[0];

        return directory.listFiles();
    }

    public File getPhoto(String teamNumber) {
        File photo = new File(Environment.getExternalStorageDirectory() + "/Scouting/Photos/" + teamNumber + ".jpg");
        if (!photo.exists())
            return null;

        return photo;
    }

    public File getFile(String name) {
        File file = new File(directory, name);
        if (!file.exists())
            return null;

        return file;
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

    public File getScoutingDirectory() {
        return directory;
    }

    public String writeData(String fileNameHeader, String data) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (!directory.exists())
                directory.mkdirs();

            String uniqueId = Scouting.getInstance().getUniqueId();
            File file = new File(directory, fileNameHeader + uniqueId + ".csv");

            try {
                fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.flush();
            } catch (IOException e) {
                Log.d("Scouting", e.getMessage());
            } finally {
                try {
                    fileOutputStream.close();
                    return "Saved successfully.";
                } catch (IOException e) {
                    Log.d("Scouting", e.getMessage());
                    return "Failure while saving.";
                }
            }
        }
        return "SD card not found.";
    }
}
