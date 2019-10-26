package com.frc107.scouting.core.file;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.ScoutingStrings;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.eTableType;
import com.frc107.scouting.core.utils.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Use this class to perform file operations.
 *
 * The intent here is to, as much as possible, abstract the file writing and reading code so that
 * you don't need to worry about how it works.
 */
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

    /**
     * Clear the loaded file definitions and load again. Call this if you do something like moving files to the scouting directory.
     */
    public void reloadFileDefinitions() {
        clearFileDefinitions();
        loadFileDefinitions();
    }

    /**
     * Look through the files in the scouting directory and make sure that we have a FileDefinition for each of them.
     */
    public void loadFileDefinitions() {
        File[] files = scoutingDirectory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory())
                continue;

            FileDefinition fileDef = getDefinitionFromFile(file);
            if (fileDef == null)
                continue;

            fileDefinitions.add(fileDef);
        }
    }

    /**
     * Wipe all the created FileDefinitions. This does not delete the files.
     */
    public void clearFileDefinitions() {
        fileDefinitions.clear();
    }

    /**
     * Create a FileDefinition from a File.
     * @param file The input file.
     * @return A new FileDefinition containing data from your File parameter.
     */
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

    /**
     * Get a file of a specific name.
     * @param name The filename, excluding the path.
     * @return A File.
     */
    public File getFile(String name) {
        return new File(getScoutingDirectory(), name);
    }

    /**
     * Get a formatted message based on an inputted Calendar.
     * @param calendar The current time.
     * @return A message containing the data from the calendar.
     */
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

    /**
     * Save scouting data.
     * This will either save to the most recent file with the same table type and initials, or create a brand new one.
     * @param tableType The type of table that your data is part of.
     * @param initials User initials.
     * @param data Your data.
     * @throws IOException When there is an error creating a new file or writing data.
     */
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
            writeDataToEndOfFile(mostRecentFile.getFile(), data);
        }
    }

    /**
     * Get the most recently created file definition matching specific parameters.
     * @param tableType The desired eTableType.
     * @param concatenated Do you want concatenated or non-concatenated files?
     * @param initials The desired initials.
     * @return The most recent FileDefinition, or null if there are none.
     */
    public FileDefinition getMostRecentFileDefinition(eTableType tableType, boolean concatenated, String initials) {
        List<FileDefinition> definitions = getFileDefinitionsOfType(tableType, concatenated, initials);
        if (definitions.isEmpty())
            return null;

        Collections.sort(definitions, (o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));

        return definitions.get(definitions.size() - 1);
    }

    /**
     * Get all the file definitions matching a specific table type.
     * @param tableType The desired eTableType.
     * @return A List of FileDefinition.
     */
    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    /**
     * Get all the file definitions matching specific parameters.
     * @param tableType The desired eTableType.
     * @param initials The desired initials.
     * @return A List of FileDefinition.
     */
    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType, String initials) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType &&
                fileDef.getInitials().equals(initials))
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    /**
     * Get all the file definitions matching specific parameters.
     * @param tableType The desired eTableType.
     * @param concatenated Do you want concatenated or non-concatenated files?
     * @return A List of FileDefinition.
     */
    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType, boolean concatenated) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType &&
                fileDef.isConcatenated() == concatenated)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    /**
     * Get all the file definitions matching specific parameters.
     * @param tableType The desired eTableType.
     * @param concatenated Do you want concatenated or non-concatenated files?
     * @param initials The desired initials.
     * @return A List of FileDefinition.
     */
    public List<FileDefinition> getFileDefinitionsOfType(eTableType tableType, boolean concatenated, String initials) {
        List<FileDefinition> fileDefs = new ArrayList<>();
        for (FileDefinition fileDef : fileDefinitions) {
            if (fileDef.getTableType() == tableType &&
                fileDef.getInitials().equals(initials) &&
                fileDef.isConcatenated() == concatenated)
                fileDefs.add(fileDef);
        }
        return fileDefs;
    }

    public List<FileDefinition> getAllFileDefinitions() {
        return new ArrayList<>(fileDefinitions);
    }

    /**
     * Add a new file definition.
     * @param tableType An eTableType.
     * @param file The File object this FileDefinition will associate with.
     * @param date A Calendar object.
     * @param initials User initials.
     * @param isConcat Is this a concatenated file?
     */
    private void addFileDefinition(eTableType tableType, File file, Calendar date, String initials, boolean isConcat) {
        FileDefinition fileDef = new FileDefinition(tableType, file, date, initials, isConcat);
        fileDefinitions.add(fileDef);
    }

    /**
     * Get all the data in a file.
     * @param file The File to read.
     * @return A String containing the data.
     * @throws IOException If there was an error reading the file.
     */
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

    /**
     * Write data to the end of a file.
     * @param file The file to write to.
     * @param data The data to write.
     * @throws IOException If there was an error while writing data.
     */
    public void writeDataToEndOfFile(File file, String data) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("Cannot write to null file!");

        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Logger.log("External storage not mounted, cannot save data!");
            throw new IllegalStateException("Cannot save data, external storage is not mounted!");
        }

        if (StringUtils.isEmptyOrNull(data))
            return;

        data = ScoutingStrings.NEW_LINE + data;
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
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

    /**
     * Get a formatted file name. This adds .csv to the end, so don't worry about that.
     * @param tableType Your eTableType.
     * @param calendar A Calendar representing the date of creation.
     * @param initials User initials.
     * @param concat Is this a concatenated file?
     * @return The file name.
     */
    private String getNewFileName(eTableType tableType, Calendar calendar, String initials, boolean concat) {
        String prefix = tableType.getPrefix(concat);
        String timeMessage = getTimeMessage(calendar);
        return prefix + FILE_NAME_DELIMITER + initials + FILE_NAME_DELIMITER + timeMessage + ".csv";
    }

    /**
     * Concatenate a collection of files.
     * @param targetTableType The eTableType to concatenate the data as.
     * @param filesToConcatenate The files you want to concatenate.
     * @return The file created.
     * @throws IOException If there was an error reading or writing data.
     */
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

    /**
     * Checks if a file exists in the main scouting directory.
     * @param name The name of your file.
     * @return True if it exists, false if not.
     */
    public boolean doesFileExist(String name) {
        return getFile(name).exists();
    }

    // region Photo
    /**
     * Creates a photo file with the name "{team number}.jpg".
     * @param teamNumber The team number.
     * @return The photo file.
     */
    public File createPhotoFile(String teamNumber) throws IOException {
        if (teamNumber == null)
            throw new IllegalArgumentException("Team number cannot be null");

        String prefix = "_" + teamNumber + "_";

        // We need to be able to have multiple photos of the same robot, so this handles that.
        int num = 1;
        File file = new File(photoDirectory, prefix + ".jpg");
        while (file.exists()) {
            prefix += num;
            file = new File(photoDirectory, prefix + ".jpg");
            num++;
        }

        file.createNewFile();
        return file;
    }

    /**
     * Compress a photo file so that Bluetooth transfers will be faster.
     * @param context Context so that this method can perform necessary operations.
     * @param fileName The file name of the photo.
     * @throws IOException If there was an error compressing or writing the file.
     */
    public void compressPhoto(Context context, String fileName) throws IOException {
        File file = new File(photoDirectory, fileName);
        if (!file.exists())
            throw new FileNotFoundException("File at \"" + file.getPath() + "\" does not exist.");

        // for more useful info on getting the bitmap of an image, look here:
        // https://stackoverflow.com/questions/40392666/how-can-i-get-bitmap-from-camera-on-android
        Uri uri = Uri.fromFile(file);

        BitmapFactory.Options options = new BitmapFactory.Options();

        // I confess I don't know quite if there are criteria for numbers here, but 3 was used in the above link and it works fine for this
        options.inSampleSize = 3;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            Logger.log(e.getLocalizedMessage());
        }

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            // after taking the photo, it is rotated 90 degrees so we need to rotate it again
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            rotated.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);

            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        }
    }

    /**
     * @return A List of Uri objects for each saved photo file.
     */
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
