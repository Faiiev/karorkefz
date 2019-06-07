package com.ms.karorkefz;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveFile {
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou/txt";

    // write SDCard
    public static void writeFileSdcardFile(String fileName, String writeStr) throws IOException {
        try {
            Log.e( "karorkefz", "调用写文件" );
            File file = new File( FILE_PATH, fileName + ".txt" );
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            FileOutputStream fout = new FileOutputStream( file, true );
            byte[] bytes = writeStr.getBytes();
            fout.write( bytes );
            fout.write( "\r\n".getBytes() );// 写入一个换行
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    read SDCard
    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream( fileName );
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read( buffer );
            res = new String( buffer, "UTF-8" );
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
