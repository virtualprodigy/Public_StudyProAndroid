package com.virtualprodigy.studypro.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.virtualprodigy.studypro.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by virtualprodigyllc on 11/10/15.
 */
public class FileUtils {
    private final String TAG = getClass().getSimpleName();
    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    /**
     * This method takes a FileInputStream for an image and copies the data
     * to the StudyPro image folder located in the StudyPro folder.
     *
     * @param sourceInputStream
     * @return null if any issues have arisen
     */
    public File copyImageToNotesImageFolder(FileInputStream sourceInputStream, String mimeType) {
        File imageFile = createImageFilePlusDirectory(mimeType);
        if (imageFile == null) {
            return null;
        }
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                FileChannel src = sourceInputStream.getChannel();
                FileChannel dst = new FileOutputStream(imageFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "copyImageToNotesImageFolder : Failed writing image to Notes image folder", e);
            return null;
        }

        return imageFile;
    }

    /**
     * This method creates a file for the image and also creates the image directory if
     * it doesn't already exist
     *
     * @return
     */
    public File createImageFilePlusDirectory(String mimeType) {
        String externalRoot = Environment.getExternalStorageDirectory().getPath();
        String rootExternalDirectory = externalRoot + context.getString(R.string.root_external_directory);
        //a directory with a prefix . to hide the folder
        String imageExternalDirectory = externalRoot + context.getString(R.string.images_external_directory);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dFormat = new SimpleDateFormat(context.getString(R.string.image_time_format));
        //use the mimetype to get the file extension
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String fileExtension = mimeTypeMap.getExtensionFromMimeType(mimeType);

        String imageFileName = "studypro_image_" + dFormat.format(cal.getTime()) + "." + fileExtension;
        File imageFile = new File(imageExternalDirectory, imageFileName);

        //check if the directories exist or need to be created. return null if there's a problem
        if (!checkDirectoryExist(new File(rootExternalDirectory))) {
            return null;
        } else if (!checkDirectoryExist(new File(imageExternalDirectory))) {
            return null;
        }

        return imageFile;
    }

    /**
     * This method creates the image directory if
     * it doesn't already exist
     *
     * @return
     */
    public boolean createImageDirectory() {
        String externalRoot = Environment.getExternalStorageDirectory().getPath();
        String rootExternalDirectory = externalRoot + context.getString(R.string.root_external_directory);
        //a directory with a prefix . to hide the folder
        String imageExternalDirectory = externalRoot + context.getString(R.string.images_external_directory);

        //check if the directories exist or need to be created. return null if there's a problem
        if (!checkDirectoryExist(new File(rootExternalDirectory))) {
            return false;
        } else if (!checkDirectoryExist(new File(imageExternalDirectory))) {
            return false;
        }
        return true;
    }

    /**
     * Creates a file in the notes image directory
     *
     * @param imageFileName
     * @return
     */
    public File createDownloadImageFile(String imageFileName) {
        String externalRoot = Environment.getExternalStorageDirectory().getPath();
        String imageExternalDirectory = externalRoot + context.getString(R.string.images_external_directory);
        return new File(imageExternalDirectory, imageFileName);
    }

    /**
     * This method checks if the directory exist. If it doesn't, create it
     *
     * @param directory
     * @return
     */
    private boolean checkDirectoryExist(File directory) {
        boolean doesExist = directory.exists();
        if (!doesExist) {
            doesExist = directory.mkdir();
        }
        return doesExist;
    }

    /**
     * This method checks if a file exist, if it doesn't it creates it
     *
     * @param file
     * @return
     */
    private boolean checkFileExist(File file) {
        boolean doesExist = file.exists();
        if (!doesExist) {
            try {
                doesExist = file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "Failed to create file", e);
                return false;
            }
        }
        return doesExist;
    }

    /**
     * this method gets the file path from the uri
     */
    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    /**
     * This method deletes a file
     *
     * @param file - the file to be deleted
     * @return
     */
    public boolean deleteFile(File file) {
        if (file.delete()) {
            return true;
        }

        Log.e(TAG, "failed to delete file " + file.getAbsolutePath());

        return false;
    }
}
