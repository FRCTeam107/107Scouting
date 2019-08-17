package com.frc107.scouting.core.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.table.eTableType;
import com.frc107.scouting.core.utils.StringUtils;

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

    public void reloadFileDefinitions() {
        clearFileDefinitions();
        loadFileDefinitions();
    }

    public void loadFileDefinitions() {
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
        eTableType tableType = eTableType.getTableTypeFromPrefix(prefix);

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
            Logger.log(e.getLocalizedMessage());
        }

        Calendar date = Calendar.getInstance();
        date.set(year, month, day, hour, minute, second);

        boolean isConcat = prefix.startsWith("Concat");
        return new FileDefinition(tableType, file, date, initials, isConcat);
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

    private static String getTimeMessage(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // yyyy-mm-dd_hh-mm-ss
        return year + "-" + month + "-" + day + FILE_NAME_DELIMITER + hour + "-" + minute + "-" + second;
    }

    public void saveScoutingData(eTableType tableType, String initials, String data) throws IOException {
        FileDefinition mostRecentFile = getMostRecentFileDefinition(tableType, false, initials);
        Calendar date = Calendar.getInstance();
        if (mostRecentFile == null) {
            // there is no FileDefinition for this eFileType and initials; create a new file.
            File file = createAndWriteToNewScoutingFile(tableType, date, initials, data, false);
            addFileDefinition(tableType, file, date, initials, false);
        } else if (mostRecentFile.getFile() == null || !mostRecentFile.getFile().exists()) {
            // there is a FileDefinition, but the file does not exist; delete the existing FileDefinition and create a new file + FileDefinition.
            File file = createAndWriteToNewScoutingFile(tableType, date, initials, data, false);

            fileDefinitions.remove(mostRecentFile);
            addFileDefinition(tableType, file, date, initials, false);
        } else {
            // all is well and good, the file exists; write to the existing file.
            writeLineToEndOfFile(mostRecentFile.getFile(), data);
        }
    }

    public FileDefinition getMostRecentFileDefinition(eTableType tableType, boolean concatenated, String initials) {
        List<FileDefinition> definitions = getFileDefinitionsOfType(tableType, concatenated);
        if (definitions.isEmpty())
            return null;

        Collections.sort(definitions, (o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));

        FileDefinition mostRecentDefinition = definitions.get(definitions.size() - 1);
        return mostRecentDefinition;
    }

    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType, boolean concatenated) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.isConcatenated() != concatenated)
                continue;

            if (fileDef.getTableType() == tableType)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    public List<FileDefinition> getFileDefinitions() {
        return new ArrayList<>(fileDefinitions);
    }

    private void addFileDefinition(eTableType tableType, File file, Calendar date, String initials, boolean isConcat) {
        FileDefinition fileDef = new FileDefinition(tableType, file, date, initials, isConcat);
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
                builder.append(ScoutingStrings.NEW_LINE);
                line = bufferedReader.readLine();
            }
        }

        return builder.toString();
    }

    public boolean writeLineToEndOfFile(File file, String data) {
        if (file == null)
            throw new IllegalArgumentException("Cannot write to null file!");

        if (StringUtils.isEmptyOrNull(data))
            return true;

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(ScoutingStrings.SCOUTING_TAG, "External storage not mounted!");
            return false;
        }

        data = ScoutingStrings.NEW_LINE + data;
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            return true;
        } catch (IOException e) {
            Log.e(ScoutingStrings.SCOUTING_TAG, e.getMessage());
            return false;
        }
    }

    /**
     * Create a new scouting file and write data to it. This method automatically adds the needed header to the top of the data, so don't worry about that.
     * @param tableType The type of data that you are writing.
     * @param initials User initials.
     * @param data Your data.
     * @return Your new file.
     * @throws IOException If there is an error.
     */
    private File createAndWriteToNewScoutingFile(eTableType tableType, Calendar date, String initials, String data, boolean concat) throws IOException {
        String fileName = getNewFileName(tableType, date, initials, concat);
        String header = Scouting.getInstance().getTable(tableType).getHeader();
        data = header + ScoutingStrings.NEW_LINE + data;
        return createAndWriteToNewFileCore(scoutingDirectory, fileName, data);
    }

    /**
     * Write data to a new file.
     * @param directory The directory in which to place your file.
     * @param fileName Your file name.
     * @param data The data you want saved.
     * @return Your new file.
     * @throws IOException If there is an error.
     */
    public File createAndWriteToNewFileCore(File directory, String fileName, String data) throws IOException {
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

    private String getNewFileName(eTableType tableType, Calendar calendar, String initials, boolean concat) {
        String prefix = tableType.getPrefix(concat);
        String timeMessage = getTimeMessage(calendar);
        return prefix + FILE_NAME_DELIMITER + initials + FILE_NAME_DELIMITER + timeMessage + ".csv";
    }

    public File concatenateFiles(eTableType targetTableType, File... filesToConcatenate) throws IOException {
        if (targetTableType == null)
            throw new IllegalArgumentException("Cannot concatenate files to a null eTableType.");

        String newLine = ScoutingStrings.NEW_LINE;

        StringBuilder builder = new StringBuilder();

        String header = Scouting.getInstance().getTable(targetTableType).getHeader();
        builder.append(header);
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

        Calendar date = Calendar.getInstance();
        String initials = Scouting.getInstance().getUserInitials();
        String fileName = getNewFileName(targetTableType, date, initials, true);

        String data = builder.toString();

        // We call createAndWriteToNewFileCore instead of saveScoutingData because we always want a new file for concatenation.
        File file = createAndWriteToNewFileCore(scoutingDirectory, fileName, data);

        addFileDefinition(targetTableType, file, date, initials, true);

        return file;
    }

    public boolean doesFileExist(String name) {
        return new File(scoutingDirectory, name).exists();
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
                Logger.log("Photo file already exists! Path: \"" + file.getAbsolutePath() + "\"");
                return null;
            }
        } catch (IOException e) {
            Logger.log(e.getMessage());
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
                Logger.log("Bitmap is null");
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
            Logger.log(e.getMessage());
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
