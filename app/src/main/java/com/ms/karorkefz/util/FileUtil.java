package com.ms.karorkefz.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou";

    public static void writeFileSdcardFile(String fileName, String writeStr) throws IOException {

        try {
            Log.i( "karorkefz", "调用写文件" );
            File file = new File( FILE_PATH, fileName );
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream( file, true );
            byte[] bytes = writeStr.getBytes();
            fout.write( bytes );
            fout.write( "\r\n".getBytes() );
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            Log.i( "karorkefz", "调用读文件" );
            FileInputStream fin = new FileInputStream( FILE_PATH + fileName );
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read( buffer );
            res = new String( buffer, "UTF-8" );
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
}

