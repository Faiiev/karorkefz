package com.ms.karorkefz;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


class Karaoke_Ktv_Hook {

    private ClassLoader classLoader;
    Karaoke_Ktv_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        //长按语音席改开关
        //final Class h = XposedHelpers.findClass( "com.tencent.karaoke.module.ktv.ui.h", classLoader );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.ui.h",
                classLoader,
                "O",// 被Hook的函数
                new XC_MethodHook() {
                    boolean i = true;
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        Field quRenField = XposedHelpers.findField( param.thisObject.getClass(), "J" );
                        final ImageView quRenButton = (ImageView) quRenField.get( param.thisObject );
                        quRenButton.setOnClickListener( new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (i) {
                                    XposedHelpers.callMethod( param.thisObject, "v" );
                                    i = !i;
                                } else {
                                    i = !i;
                                }
                            }
                        } );
                    }
                } );


        final Class g = XposedHelpers.findClass( "com.tencent.karaoke.base.business.g", classLoader );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.widget.RoomPasswordDialog",
                classLoader,
                "onClick",// 被Hook的函数
                View.class,
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //super.beforeHookedMethod(param);
                        XposedBridge.log( "---Hook Start ---" );
//                        XposedBridge.log("已经HOOK");
//                        Class o = param.thisObject.getClass();
//
//                        XposedBridge.log(o.getName());
//                        Field.setAccessible(o.getDeclaredFields(), true);
//                        Field fieldPassword = findField(o,"d");
//                        EditText editText = (EditText)fieldPassword.get(param.thisObject);
//                        String password = editText.getText().toString();
//                        XposedBridge.log( password );
                    }

                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log( "---Hook End---" );
//                        Object gObject = XposedHelpers.newInstance( g, "0331" );
//                        Object object =  XposedHelpers.callStaticMethod(g, "a", "0331");
                    }
                } );
    }
}
