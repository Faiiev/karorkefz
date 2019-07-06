package com.ms.karorkefz.util.Log;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.robv.android.xposed.XSharedPreferences;

public class LogUtil {

    private static final int VERBOSE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARNING = 3;
    private static final int ERROR = 4;
    private static final int NO_LOG = 5;
    private static String LOG_FILE = null;
    private static LogUtil mInstance = null;
    private static int LOG_LEVEL = NO_LOG;
    private static boolean logFileEnable = false;

    static {
        try {
            XSharedPreferences xSharedPreferences = new XSharedPreferences( "com.ms.karorkefz" );
            boolean Log = xSharedPreferences.getBoolean( "Log", true );
            int Log_Level = Integer.parseInt( xSharedPreferences.getString( "Log_Level", "0" ) );
            LOG_LEVEL = Log_Level;
            logFileEnable = Log;

            String logFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou/Log/Hook.txt";
            File file = new File( logFilePath );
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                LOG_FILE = logFilePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LogUtil getInstance() {
        if (mInstance == null) {
            synchronized (LogUtil.class) {
                if (mInstance == null) {
                    mInstance = new LogUtil();
                }
            }
        }
        return mInstance;
    }

    public static void v(String tag, String msg) {
        if (VERBOSE < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        Log.v( tag, msg );
        write( "VERBOSE", getInstance().getPrefixName(), msg );
    }

    public static void v(String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        v( getInstance().getPrefixName(), msg );
    }

    public static void d(String tag, String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        Log.d( tag, msg );
        write( "DEBUG", getInstance().getPrefixName(), msg );
    }

    public static void d(String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        d( getInstance().getPrefixName(), msg );
    }

    public static void i(String tag, String msg) {
        if (INFO < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        Log.i( tag, msg );
        write( "INFO", getInstance().getPrefixName(), msg );
    }

    public static void i(String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        i( getInstance().getPrefixName(), msg );
    }

    public static void w(String tag, String msg) {
        if (WARNING < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        Log.w( tag, msg );
        write( "WARNING", getInstance().getPrefixName(), msg );
    }

    public static void w(String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        w( getInstance().getPrefixName(), msg );
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (WARNING < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        LogUtil.w( tag, msg, tr );
        write( "WARNING", getInstance().getPrefixName(), msg );
    }

    public static void e(String tag, String msg) {
        if (ERROR < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        Log.e( tag, msg );
        write( "ERROR", getInstance().getPrefixName(), msg );
    }

    public static void e(String msg) {
        if (DEBUG < LOG_LEVEL)
            return;
        if (msg == null)
            return;
        e( getInstance().getPrefixName(), msg );
    }

    private static void write(String level, String prefix, String content) {
        if (!logFileEnable)
            return;
        if (LOG_FILE.equals( null ))
            return;
        try {
            FileWriter writer = new FileWriter( LOG_FILE, true );
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.getDefault() );
            String time = sdf.format( new Date() );
            writer.write( time + "  :   " + level + "/" + prefix + "  :   " + content + "\n" );
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPrefixName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null || sts.length == 0) {
            return "[ minify ]";
        }
        try {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals( Thread.class.getName() )) {
                    continue;
                }
                if (st.getClassName().equals( this.getClass().getName() )) {
                    continue;
                }
                if (st.getFileName() != null) {
                    return "[ " + Thread.currentThread().getName() +
                            ": " + st.getFileName() + ":" + st.getLineNumber() +
                            " " + st.getMethodName() + " ]";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[ minify ]";
    }
}
