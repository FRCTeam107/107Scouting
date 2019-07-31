package com.frc107.scouting.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.eTable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

            FileDefinition fileDef = getDefinitionFromFile(file);
            if (fileDef == null)
                continue;

            fileDefinitions.add(fileDef);
        }
    }

    public void clearFileDefinitions() {
        fileDefinitions.clear();
    }

    private FileDefinition getDefinitionFromFile(File file) {
        String name = file.getName();

        // Separate the content from the extension. We use "\\." instead of "." because .split
        // will interpret the parameter as regex if possible, and "." is valid regex. "\\."
        // tells split to use the period character instead of regex.
        String[] contentAndExtension = name.split("\\.");
        if (contentAndExtension.length != 2)
            return null;

        String content = contentAndExtension[0];
        String extension = contentAndExtension[1];
        if (!extension.equals(FILE_EXTENSION))
            return null;

        String[] parts = content.split(FILE_NAME_DELIMITER);
        if (parts.length != 4)
            return null;

        String prefix = parts[0];
        eTable tableType = eTable.getTableTypeFromPrefix(prefix);

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

        return new FileDefinition(file, calendar, initials, tableType);
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

    public void saveData(eTable tableType, String initials, String data) throws IOException {
        FileDefinition mostRecentFile = getMostRecentFileDefinition(tableType, initials);
        Calendar date = Calendar.getInstance();
        if (mostRecentFile == null) {
            // there is no FileDefinition for this eFileType and initials; create a new file.
            createAndWriteToNewFile(tableType, date, initials, data);
        } else if (mostRecentFile.getFile() == null || !mostRecentFile.getFile().exists()) {
            // there is a FileDefinition, but the file does not exist; delete the existing FileDefinition and create a new file + FileDefinition.
            File file = createAndWriteToNewFile(tableType, date, initials, data);

            fileDefinitions.remove(mostRecentFile);
            addFileDefinition(tableType, file, date, initials);
        } else {
            // all is well and good, the file exists; write to the existing file.
            writeLineToEndOfFile(mostRecentFile.getFile(), data);
        }
    }

    public FileDefinition getMostRecentFileDefinition(eTable tableType, String initials) {
        List<FileDefinition> definitions = fileDefinitions;
        Collections.sort(definitions, (o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));

        FileDefinition mostRecentDefinition = null;
        for (FileDefinition fileDefinition : definitions) {
            if (fileDefinition.getTableType() != tableType ||
                !fileDefinition.getInitials().equals(initials))
                continue;

            mostRecentDefinition = fileDefinition;
        }

        return mostRecentDefinition;
    }

    public List<FileDefinition> getFileDefinitionsOfType(eTable tableType) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    private void addFileDefinition(eTable tableType, File file, Calendar date, String initials) {
        FileDefinition fileDef = new FileDefinition(file, date, initials, tableType);
        fileDefinitions.add(fileDef);
    }

    public String getFileData(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("Parameter \"file\" cannot be null.");

        StringBuilder builder = new StringBuilder();

        // This try block is here so that the FileReader and BufferedReader will automatically be closed.
        try (FileReader fileReader = new FileReader(file.getPath());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                builder.append(Scouting.NEW_LINE);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }

    private boolean writeLineToEndOfFile(File file, String data) {
        if (file == null)
            throw new IllegalArgumentException("Cannot write to null file!");

        if (StringUtils.isEmptyOrNull(data))
            return true;

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(Scouting.SCOUTING_TAG, "External storage not mounted!");
            return false;
        }

        data = Scouting.NEW_LINE + data;
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e(Scouting.SCOUTING_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Write data to a new file. This method automatically adds the needed header to the top of the data, so don't worry about that.
     * @param tableType The type of data that you are writing.
     * @param initials User initials.
     * @param data Your data.
     * @return
     */
    private File createAndWriteToNewFile(eTable tableType, Calendar date, String initials, String data) throws IOException {
        String fileName = getNewFileName(tableType, date, initials);
        data = tableType.getHeader() + Scouting.NEW_LINE + data;
        return createAndWriteToNewFileCore(scoutingDirectory, fileName, data);
    }
    private File createAndWriteToNewFileCore(File directory, String fileName, String data) throws IOException {
        File file = new File(directory, fileName);
        if (file.exists())
            throw new IllegalArgumentException("Invalid file name \"" + fileName + "\"; file already exists.");

        if (StringUtils.isEmptyOrNull(data))
            throw new IllegalArgumentException("data cannot be null or empty.");

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            throw new IllegalStateException("External storage not mounted; cannot save data.");

        // This try block is here in case the .write fails, as it'll automatically close the FileOutputStream.
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
        }

        return file;
    }

    private String getNewFileName(eTable tableType, Calendar calendar, String initials) {
        String prefix = tableType.getPrefix();
        String timeMessage = getCurrentTimeMessage(calendar);
        return prefix + FILE_NAME_DELIMITER + initials + FILE_NAME_DELIMITER + timeMessage + ".csv";
    }

    public File concatenateFiles(eTable target, File... filesToConcatenate) throws IOException {
        if (target == null)
            throw new IllegalArgumentException("Cannot concatenate files to a null eTable.");

        String newLine = Scouting.NEW_LINE;

        StringBuilder builder = new StringBuilder();
        builder.append(target.getHeader());
        builder.append(newLine);

        for (File file : filesToConcatenate) {
            String data = getFileData(file);
            String[] lines = data.split(newLine);

            // We go through each line so that we can skip the header line.
            for (int i = 1; i < lines.length; i++) {
                builder.append(lines[i]);
                builder.append(newLine);
            }
        }

        String data = builder.toString();
        Calendar calendar = Calendar.getInstance();
        String fileName = target.getConcatPrefix() + FILE_NAME_DELIMITER + getCurrentTimeMessage(calendar) + "." + FILE_EXTENSION;
        return createAndWriteToNewFileCore(scoutingDirectory, fileName, data);
    }

    public class FileDefinition {
            private File file;
            private Calendar dateCreated;
            private String initials;
            private eTable tableType;

            FileDefinition(File file, Calendar dateCreated, String initials, eTable tableType) {
                this.file = file;
                this.dateCreated = dateCreated;
                this.initials = initials;
                this.tableType = tableType;
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

        public eTable getTableType() {
            return tableType;
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
            file = new File(photoDirectory, teamNumber + "-" + num + ".jpg");
            num++;
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

    public boolean compressPhoto(String fileName) {
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

            // Based on recent tests, it's no longer needed to rotate the photo.
            /*Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);*/

            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);

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
