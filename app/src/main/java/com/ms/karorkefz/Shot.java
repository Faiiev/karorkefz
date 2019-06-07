package com.ms.karorkefz;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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
                    Shot.activityShot( activity );
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
        Log.v( "karorkefz", "调用activityShot" );
//        Shot.activityShot( MainActivity.this );//调用方法
        if (!checkSelf( activity )) {
            return null;
        }
        /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled( true );
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame( rect );
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();

        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics( outMetrics );
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap( view.getDrawingCache(), 0, statusBarHeight, width,
                height - statusBarHeight );

        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled( false );
        saveBitmapToLocal( bitmap );
        return bitmap;
    }

    public static void saveBitmapToLocal(Bitmap bitmap) {
        Log.v( "karorkefz", "调用saveBitmapToLocal" );
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            String fileName = TimeHook.SimpleDateFormat_Time();
            File file = new File( FILE_PATH, fileName + ".png" );
//            Log.i( "karorkefz", String.valueOf( file ) );
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();

            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress( Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream( file ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkSelf(Activity activity) {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission( activity, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE )) {
                Toast.makeText( activity, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT ).show();
                Log.e( "karorkefz", "checkPermission: 未授权！" );
            }
            //申请权限
            ActivityCompat.requestPermissions( activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE );
            Log.e( "karorkefz", "checkPermission: 未授权！" );
        } else {
            Log.v( "karorkefz", "checkPermission: 已经授权！" );
            return true;
        }
        return false;
    }
}