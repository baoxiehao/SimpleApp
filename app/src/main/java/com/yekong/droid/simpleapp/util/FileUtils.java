package com.yekong.droid.simpleapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by baoxiehao on 17/2/2.
 */

public class FileUtils {

    public static boolean copyFile(File srcFile, File targetFile) {
        try {
            if (srcFile.exists()) {
                InputStream inputStream = new FileInputStream(srcFile);
                FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
                inputStream.close();
                fileOutputStream.close();
                Logger.d("copyFile(): done copy from %s to %s", srcFile.getAbsolutePath(), targetFile.getAbsolutePath());
                return true;
            }
        } catch (Exception e) {
            Logger.e("copyFile", e);
        }
        Logger.d("copyFile(): failed to copy from %s to %s", srcFile.getAbsolutePath(), targetFile.getAbsolutePath());
        return false;
    }
}
