package com.ms.karorkefz.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ms.karorkefz.util.Log.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Shot {
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou/Pictures";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName( "android.app.ActivityThread" );
            Object activityThread = activityThreadClass.getMethod( "currentActivityThread" ).invoke( null );
            Field activitiesField = activityThreadClass.getDeclaredField( "mActivities" );
            activitiesField.setAccessible( true );
            Map activities = (Map) activitiesField.get( activityThread );
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField( "paused" );
                pausedField.setAccessible( true );
                if (!pausedField.getBoolean( activityRecord )) {
                    Field activityField = activityRecordClass.getDeclaredField( "activity" );
                    activityField.setAccessible( true );
                    Activity activity = (Activity) activityField.get( activityRecord );
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap activityShot(Activity activity) {

        LogUtil.i( "karorkefz", "调用activityShot" );
        if (!checkSelf( activity )) {
            return null;
        }
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled( true );
        view.buildDrawingCache();
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame( rect );
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics( outMetrics );
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        Bitmap bitmap = Bitmap.createBitmap( view.getDrawingCache(), 0, statusBarHeight, width,
                height - statusBarHeight );
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled( false );
        saveBitmapToLocal( bitmap );
        return bitmap;
    }

    public static void saveBitmapToLocal(Bitmap bitmap) {

        LogUtil.i( "karorkefz", "调用saveBitmapToLocal" );
        try {
            String fileName = TimeHook.SimpleDateFormat_Time();
            File file = new File( FILE_PATH, fileName + ".png" );
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            bitmap.compress( Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream( file ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkSelf(Activity activity) {
        if (ActivityCompat.checkSelfPermission( activity, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE )) {
                Toast.makeText( activity, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT ).show();
                LogUtil.i( "karorkefz", "checkPermission: 未授权！" );
            }
            ActivityCompat.requestPermissions( activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE );
            LogUtil.i( "karorkefz", "checkPermission: 未授权！" );
        } else {
            LogUtil.i( "karorkefz", "checkPermission: 已经授权！" );
            return true;
        }
        return false;
    }
}