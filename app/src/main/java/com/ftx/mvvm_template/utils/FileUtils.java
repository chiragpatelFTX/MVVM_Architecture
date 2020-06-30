package com.ftx.mvvm_template.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Contains all the method for the File Operation.
 *
 * @author hrdudhat
 */
public class FileUtils {

    /**
     * This method is used to extract the Zip File.
     *
     * @param aZipFile        a zip file that you want to unpack
     * @param aDestinationDir destination directory where you want to unpack the zip file
     * @return true if zip will be unpacked
     * @throws FileNotFoundException
     */
    public static boolean unpackZipFile(File aZipFile, File aDestinationDir) throws FileNotFoundException {
        FileInputStream fileInputStream;
        ZipInputStream zipInputStream;
        final String sWorkingDir = aDestinationDir.getAbsolutePath() + File.separator;
        try {
            fileInputStream = new FileInputStream(aZipFile);
            zipInputStream = new ZipInputStream(fileInputStream);

            byte buffer[] = new byte[4096];
            int iByteRead;
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    final File dir = new File(sWorkingDir, zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                } else {
                    final FileOutputStream fout = new FileOutputStream(sWorkingDir + zipEntry.getName());
                    while ((iByteRead = zipInputStream.read(buffer)) != -1) {
                        fout.write(buffer, 0, iByteRead);
                    }
                    fout.close();
                }
            }
            zipInputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @param aSourceFile     SourceFile to be Copied.
     * @param aDestinationDir Destination directory where file should be copied.
     * @throws IOException if destination directory can't be created
     */
    public static void copyFile(File aSourceFile, File aDestinationDir) throws IOException {
        if (aSourceFile.isDirectory()) {
            if (!aDestinationDir.exists() && !aDestinationDir.mkdirs()) {
                throw new IOException("Cannot create dir " + aDestinationDir.getAbsolutePath());
            }

            String[] children = aSourceFile.list();
            for (int i = 0; i < children.length; i++) {
                copyFile(new File(aSourceFile, children[i]), new File(aDestinationDir, children[i]));
            }
        } else {
            // make sure the directory we plan to store the recording in exists
            File directory = aDestinationDir.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }
            final InputStream in = new FileInputStream(aSourceFile);
            final OutputStream out = new FileOutputStream(aDestinationDir);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    /**
     * This method is used to read the file line by line. In this
     * method given file will be read and each
     * line will be added to the Array list.
     *
     * @param aSourceFile   source file from where you want to read lines
     * @param aFileEncoding encoding of the dat written in the file
     * @returns ArrayList<String> list of the line read from file
     */
    public static ArrayList<String> readFile(File aSourceFile, String aFileEncoding) throws IOException {
        final ArrayList<String> alLinesList = new ArrayList<String>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(aSourceFile);
            if (fileInputStream != null) {
                final InputStreamReader reader = new InputStreamReader(fileInputStream, Charset.forName(aFileEncoding));
                final BufferedReader bufferedReader = new BufferedReader(reader);
                String sLine;
                while ((sLine = bufferedReader.readLine()) != null) {
                    alLinesList.add(sLine);
                }

                fileInputStream.close();
            }
        } catch (IOException e) {
            throw new IOException("File not found");
        }
        return alLinesList;
    }

    /**
     * This method is used to delete the Given File. In this method if
     * File is Directory then System list all the Files of the
     * Directory and then call this method Recursively to Delete the
     * Files of the Directory.
     *
     * @param afTargetFile
     */
    public static void deleteDirectoryOrFile(File afTargetFile) {
        if (afTargetFile.exists()) {
            if (afTargetFile.isDirectory()) {
                final File[] arrayFileList = afTargetFile.listFiles();
                if (arrayFileList != null) {
                    if (arrayFileList.length > 0) {
                        for (File file : arrayFileList) {
                            deleteDirectoryOrFile(file);
                        }
                        afTargetFile.delete();
                    } else {
                        afTargetFile.delete();
                    }
                }
            } else {
                afTargetFile.delete();
            }
        }
    }

    /**
     * To Remove all the Files from the Given
     * Directory. In this method System Get All the List of Files and
     * Remove all the Files if given file path is for Directory else delete file.
     */
    public static void deleteFileOrDirectory(String aFilePath) throws IOException {
        final File sDirectory = new File(aFilePath);
        deleteDirectoryOrFile(sDirectory);
    }

    /**
     * To convert the File to byte[].
     *
     * @param aFilePath path of file from which you need byte array
     * @throws Exception if file not exist or invalid file
     */
    public static byte[] convertFileToByteArray(String aFilePath) throws Exception {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            fileInputStream = new FileInputStream(aFilePath);
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new Exception("File not exist or invalid file");
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (baos != null) {
                baos.close();
                baos.flush();
            }

        }
    }

    /**
     * To get the File path from the Uri. In this
     * method Uri is converted to File Path.
     */
    public static String getPathFromUri(Context aContext, Uri aUri) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = aContext.getContentResolver().query(aUri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        final String aFilePath = cursor.getString(column_index);
        cursor.close();
        return aFilePath;

    }

    /**
     * To convert byte array data to File. In this
     * method byte is conveted to file and witteen to the given
     * Directory with given Extention.
     *
     * @param aDirectory Parent Directory when files to be stored.
     * @param aContent   byte array Data to be converted in to File.
     * @param aFileName  Sting Name of the File.
     * @param aExtn      Type of the File. (Extension)
     * @returns File Return the Created {@link File} in the Given Directory.
     */
    public static File getFileFromByteArray(File aDirectory, byte[] aContent, String aFileName, String aExtn) {
        FileOutputStream fileOutputStream = null;
        final File imgFile = new File(aDirectory, aFileName + aExtn);
        try {
            if (imgFile.exists()) {
                imgFile.delete();
            }
            fileOutputStream = new FileOutputStream(imgFile);
            fileOutputStream.write(aContent);

            fileOutputStream.flush();
            fileOutputStream.close();

            return imgFile;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            aContent = null;
        }
        return null;
    }

    /**
     * To get the total size of all the Files in
     * the Array. In this method if Array is NULL then return 0.
     *
     * @param aFiles {@link Array} of {@link File} to get the size of all files.
     * @return double Returns the total size of all files in given array.
     */
    public static long getFileSize(File[] aFiles) {
        long lTotalSize = 0;
        try {
            /**
             * Array is NULL or Size of array is 0 then simple return 0.
             */
            if (aFiles == null || aFiles.length == 0) {
                return 0;
            }
            for (File file : aFiles) {
                // IF File Exist then Add the Current File size to total file size.
                if (file.exists()) {
                    lTotalSize = lTotalSize + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lTotalSize;
    }

    /**
     * To create file in the SD card. This method
     * creates File with given Content and type and Stored at the
     * location.
     *
     * @param asFilePath File Path (Directory where file is to be created.)
     * @param asContent  Content to be written on the File.
     * @param asFileName File Name.
     * @returns File Returns the Created File.
     */
    public static File createFile(String asFilePath, String asContent, String asFileName) {
        /**
         * If the Given Parameters are not valida then Return false.
         */
        if (asFilePath == null || asFilePath.trim().length() == 0 || asFileName == null || asFileName.trim().length() == 0) {
            return null;
        }

        File file = new File(asFilePath, asFileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(asContent);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
        return null;
    }

    /**
     * To create file in the SD card. This method
     * creates File with given Content and type and Stored at the
     * location.
     * <p/>
     * In this method File content is written as line given in the
     * Parameter. For Each element of the Array New Line is Created in
     * the File and written to the File in New Line.
     *
     * @param aFilePath         File Path (Directory where file is to be created.)
     * @param aLinesTobeWritten Array of String for lines to be written in the File.
     * @param aFileName         File Name.
     * @returns Return the File created .
     */
    public static File createFile(String aFilePath, String[] aLinesTobeWritten, String aFileName) {
        /**
         * If the Given Parameters are not valida then Return false.
         */
        if (aFilePath == null || aFilePath.trim().length() == 0 || aFileName == null || aFileName.trim().length() == 0) {
            return null;
        }

        File file = new File(aFilePath, aFileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            for (String line : aLinesTobeWritten) {
                fileWriter.write(line);
                fileWriter.write("\r\n");
            }
            fileWriter.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
        return null;
    }

    /**
     * To create New File from the Existing File
     * with Given Character Set. In this method System Reads the File
     * from Given Character set and Creates new File In Given Character
     * Set.
     *
     * @param aSourceFile sourceFile to create file
     * @param aDestFile   Destination directory to create a new file
     * @param aCharSet    charset of existing file
     */
    public static void createFilewithCharacterset(File aSourceFile, File aDestFile, String aCharSet) {
        FileInputStream fileInputStream = null;
        FileOutputStream outputStream = null;
        Writer out = null;
        try {
            fileInputStream = new FileInputStream(aSourceFile);

            if (fileInputStream != null) {
                final InputStreamReader reader = new InputStreamReader(fileInputStream, Charset.forName(aCharSet));
                final BufferedReader bufferedReader = new BufferedReader(reader);

                outputStream = new FileOutputStream(aDestFile);
                out = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName(aCharSet)));

                String sLine = "";
                while ((sLine = bufferedReader.readLine()) != null) {
                    out.write(sLine);
                }
                bufferedReader.close();
                out.flush();
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null && out != null) {
                try {
                    out.close();
                    fileInputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * To Create File from Given bytes[]
     *
     * @param aDataBytes          data bytes to write in file
     * @param aDestinationDirPath Destination directory to write data
     * @param aFileName           file name to create file in destination directory
     * @return true if data written successfully else false
     */
    public static boolean createFile(byte[] aDataBytes, String aDestinationDirPath, String aFileName) {
        if (aDataBytes == null || aDataBytes.length == 0) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(aDestinationDirPath.concat(File.separator).concat(aFileName));
            fileOutputStream.write(aDataBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * To write the object to File.
     *
     * @param aContext  Context to write object in given file path using Context.getFileStreamPath()
     * @param aObject   object to write in file
     * @param aFileName File name to create file in application memory
     * @returns boolean true if object written successfully
     */
    public static boolean writeObjectToFile(Context aContext, Object aObject, String aFileName) {
        File file = null;
        FileOutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (aFileName != null && aFileName.trim().length() > 0) {
                file = aContext.getFileStreamPath(aFileName);
                if (file.exists() || file.createNewFile()) {
                    outputStream = aContext.openFileOutput(aFileName, Context.MODE_PRIVATE);
                    objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(aObject);
                    objectOutputStream.close();
                    outputStream.close();

                    return true;
                }
            }

        } catch (Exception e) {
        } finally {
        }
        return false;
    }

    /**
     * <p/>
     * To read the Object from File.
     *
     * @returns Object
     */
    public static Object readObjectFromFile(String strFileName, Context context) {
        File file = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object object = null;
        try {
            if (strFileName != null && strFileName.trim().length() > 0) {
                file = context.getFileStreamPath(strFileName);
                if (file.exists()) {
                    fileInputStream = context.openFileInput(strFileName);
                    objectInputStream = new ObjectInputStream(fileInputStream);
                    object = objectInputStream.readObject();
                    fileInputStream.close();
                    return object;
                }
            }
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * @MethodName readFileAsSring
     * @CreatedBy hrdudhat
     * @CreatedDate May 1, 2015
     * @ModifiedBy hrdudhat
     * @ModifiedDate May 1, 2015
     * @returns String
     * @Purpose This method is used to read the file line by line. In thie
     * method given file will be read and added to the Array list each
     * line.
     */
    public static String readFileAsSring(File afFile) {
        if (afFile == null || !afFile.exists()) {
            return null;
        }
        FileInputStream fin = null;
        String ret = null;
        try {
            fin = new FileInputStream(afFile);
            ret = convertStreamToString(fin);
        } catch (IOException aE) {
            aE.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
            }
        }

        //Make sure you close all streams.

        return ret;
    }

    /**
     * To write bitmap object to give file path as png encoded image
     *
     * @param aBitmap   bitmap to write
     * @param aFilename file path to write bitmap as png image
     */
    public static void writeBitmapToPNGFile(Bitmap aBitmap, String aFilename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(aFilename);
            aBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * To get bitmap from file path
     *
     * @param aFilename file path of the bitmap
     * @return
     */
    public static final Bitmap readBitmapFromFile(String aFilename) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        final Bitmap bitmap = BitmapFactory.decodeFile(aFilename, options);
        return bitmap;
    }

    /**
     * To get file list with given extension from directory
     *
     * @param aDropboxCachedBackup directory path to get file list
     * @param aBackUpFileExtn      extension for which you want a list of files
     * @param sortBylastModified   true to sort list by last modified date
     * @return list of files
     */
    public static ArrayList<File> getFileFromDirectory(String aDropboxCachedBackup, String aBackUpFileExtn, boolean sortBylastModified) {
        File file = new File(aDropboxCachedBackup);
        ArrayList<File> alFiles = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles(new FileFilterByExtension(aBackUpFileExtn));
            if (sortBylastModified) {
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return Long.compare(f1.lastModified(), f2.lastModified());
                    }
                });
            }
            Collections.addAll(alFiles, files);
        }
        return alFiles;
    }

    /**
     * This method is used to convert specified InputStream to
     * readable String. We are using this method generally in API/Web
     * service response parsing
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw new IOException("File not exist");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
