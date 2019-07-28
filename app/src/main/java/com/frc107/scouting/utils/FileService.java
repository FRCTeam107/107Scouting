package com.frc107.scouting.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.frc107.scouting.Scouting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileService {
    /**
     * Valid file names created by this application are in the format:
     * prefix_initials_yyyy-mm-dd_hh-mm-ss.csv
     */

    private static final String FILE_NAME_DELIMITER = "_";
    private static final String TIME_MESSAGE_DELIMITER = "-";
    private static final String FILE_EXTENSION = "csv";

    private static final String SCOUTING_PATH = Environment.getExternalStorageDirectory() + "/Scouting";
    private static final String PHOTO_PATH = Environment.getExternalStorageDirectory() + "/Scouting/Photos";

    private File scoutingDirectory;
    private File photoDirectory;

    private List<FileDefinition> fileDefinitions;

    /**
     * This is where you'll define your own file types, if you want to do that.
     *
     * For example, for 2019, we would have had 4 different kinds of files:
     * - Match
     * - Concatenated Match
     * - Pit
     * - Concatenated Pit
     *
     * So I would have had the eFileType enum values be:
     *  - PIT
     *  - CONCAT_PIT
     *  - MATCH
     *  - CONCAT_MATCH
     *
     * I would have had four String constants:
     *  private static final String PIT_PREFIX = "Pit";
     *  private static final String CONCAT_PIT_PREFIX = "ConcatPit";
     *  private static final String MATCH_PREFIX = "Match";
     *  private static final String CONCAT_MATCH_PREFIX = "ConcatMatch";
     *
     * Finally I would make sure eFileType.getPrefix and getFileTypeFromPrefix are updated accordingly.
     */
    //region File Types
    private static final String PIT_PREFIX = "Pit";
    private static final String CONCAT_PIT_PREFIX = "ConcatPit";

    public enum eFileType {
        PIT,
        CONCAT_PIT,
        NONE;

        String getPrefix() {
            switch (this) {
                case PIT:
                    return PIT_PREFIX;
                case CONCAT_PIT:
                    return CONCAT_PIT_PREFIX;
                default:
                    return null;
            }
        }
    }

    /**
     * This method is used to figure out what file type an existing file is.
     * @param prefix
     * @return
     */
    private static eFileType getFileTypeFromPrefix(String prefix) {
        switch (prefix) {
            case PIT_PREFIX:
                return eFileType.PIT;
            case CONCAT_PIT_PREFIX:
                return eFileType.CONCAT_PIT;
            default:
                return eFileType.NONE;
        }
    }
    //endregion

    public FileService() {
        scoutingDirectory = new File(SCOUTING_PATH);
        if (!scoutingDirectory.exists())
            scoutingDirectory.mkdir();

        photoDirectory = new File(PHOTO_PATH);
        if (!photoDirectory.exists())
            photoDirectory.mkdir();

        fileDefinitions = new ArrayList<>();
    }

    public void loadFiles() {
        File[] files = scoutingDirectory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                continue;

            String name = file.getName();

            // Separate the content from the extension. We use "\\." instead of "." because .split
            // will interpret the parameter as regex if possible, and "." is valid regex. "\\."
            // tells split to use the period character instead of regex.
            String[] contentAndExtension = name.split("\\.");
            if (contentAndExtension.length != 2)
                continue;

            String content = contentAndExtension[0];
            String extension = contentAndExtension[1];
            if (!extension.equals(FILE_EXTENSION))
                continue;

            String[] parts = content.split(FILE_NAME_DELIMITER);
            if (parts.length != 4)
                continue;

            String prefix = parts[0];
            eFileType fileType = getFileTypeFromPrefix(prefix);

            String initials = parts[1];

            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int minute = 0;
            int second = 0;
            try {
                String[] dateParts = parts[2].split(TIME_MESSAGE_DELIMITER);
                year = Integer.parseInt(dateParts[0]);
                month = Integer.parseInt(dateParts[1]);
                day = Integer.parseInt(dateParts[2]);

                String[] timeParts = parts[3].split(TIME_MESSAGE_DELIMITER);
                hour = Integer.parseInt(timeParts[0]);
                minute = Integer.parseInt(timeParts[1]);
                second = Integer.parseInt(timeParts[2]);
            } catch (NumberFormatException e) {
                Log.d(Scouting.SCOUTING_TAG, e.getLocalizedMessage());
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute, second);

            FileDefinition fileDefinition = new FileDefinition(file, calendar, initials, fileType);
            fileDefinitions.add(fileDefinition);
        }
    }

    public File getScoutingDirectory() {
        return scoutingDirectory;
    }

    public File[] getFilesInDirectory() {
        return getScoutingDirectory().listFiles();
    }

    public File getFile(String name) {
        File file = new File(getScoutingDirectory(), name);
        if (!file.exists())
            throw new IllegalArgumentException("No file with name \"" + name + "\" was found.");

        return file;
    }

    private static String getCurrentTimeMessage(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // yyyy-mm-dd_hh-mm-ss
        return year + "-" + month + "-" + day + FILE_NAME_DELIMITER + hour + "-" + minute + "-" + second;
    }

    public boolean saveData(eFileType fileType, String initials, String data) {
        FileDefinition mostRecentFile = getMostRecentFile(fileType, initials);
        boolean success;
        if (mostRecentFile == null) {
            // there is no FileDefinition for this eFileType and initials
            success = createAndWriteToNewFile(fileType, initials, data);
        } else if (mostRecentFile.getFile() == null || !mostRecentFile.getFile().exists()) {
            // there is a FileDefinition, but the file does not exist
            fileDefinitions.remove(mostRecentFile);
            success = createAndWriteToNewFile(fileType, initials, data);
        } else {
            // all is well and good, the file exists
            success = writeToEndOfFile(mostRecentFile.getFile(), data);
        }
        return success;
    }

    public FileDefinition getMostRecentFile(eFileType fileType, String initials) {
        List<FileDefinition> definitions = fileDefinitions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            definitions.sort((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));
        }

        FileDefinition mostRecentDefinition = null;
        for (FileDefinition fileDefinition : definitions) {
            if (fileDefinition.getFileType() != fileType ||
                !fileDefinition.getInitials().equals(initials))
                continue;

            mostRecentDefinition = fileDefinition;
        }

        return mostRecentDefinition;
    }

    private boolean writeToEndOfFile(File file, String data) {
        if (file == null)
            throw new IllegalArgumentException("Cannot write to null file!");

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
    private boolean createAndWriteToNewFile(eFileType fileType, String initials, String data) {
        Calendar calendar = Calendar.getInstance();
        String prefix = fileType.getPrefix();
        String timeMessage = getCurrentTimeMessage(calendar);
        String fileName = prefix + FILE_NAME_DELIMITER + initials + FILE_NAME_DELIMITER + timeMessage + ".csv";
        return createAndWriteToNewFileCore(scoutingDirectory, fileName, data, calendar);
    }
    private boolean createAndWriteToNewFileCore(File directory, String fileName, String data, Calendar dateCreated) {
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
        } catch (IOException e) {
            Log.e(Scouting.SCOUTING_TAG, e.getMessage());
            return false;
        }

        String initials = Scouting.getInstance().getUserInitials();
        FileDefinition fileDefinition = new FileDefinition(file, dateCreated, initials, eFileType.PIT);
        fileDefinitions.add(fileDefinition);

        return true;
    }

    public void concatenateFiles(eFileType target, File... filesToConcatenate) {

    }

    public class FileDefinition {
        private File file;
        private Calendar dateCreated;
        private String initials;
        private FileService.eFileType fileType;

        FileDefinition(File file, Calendar dateCreated, String initials, FileService.eFileType fileType) {
            this.file = file;
            this.dateCreated = dateCreated;
            this.initials = initials;
            this.fileType = fileType;
        }

        public File getFile() {
            return file;
        }

        public Calendar getDateCreated() {
            return dateCreated;
        }

        public String getInitials() {
            return initials;
        }

        public FileService.eFileType getFileType() {
            return fileType;
        }
    }

    // region Photo
    public File createPhotoFile(String teamNumber) {
        if (teamNumber == null)
            throw new IllegalArgumentException("Team number cannot be null");

        File file = new File(photoDirectory, teamNumber + ".jpg");

        // We need to be able to have multiple photos of the same robot, so this handles that.
        int num = 1;
        while (file.exists()) {
            num++;
            file = new File(photoDirectory, teamNumber + "-" + num + ".jpg");
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

    public File getPhoto(String teamNumber) {
        File photo = new File(photoDirectory, teamNumber + ".jpg");
        if (!photo.exists())
            throw new IllegalArgumentException("No photo with name \"" + teamNumber + ".jpg\" was found.");

        return photo;
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

    public List<Uri> getPhotoUriList() {
        ArrayList<Uri> uriList = new ArrayList<>();
        File[] photos = photoDirectory.listFiles();
        for (File photo : photos) {
            Uri uri = Uri.parse(photo.getAbsolutePath());
            uriList.add(uri);
        }
        return uriList;
    }
    // endregion
}
