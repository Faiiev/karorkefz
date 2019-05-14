package com.ms.karorkefz;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;


class Karaoke_Live_Hook {
    private static String s1 = " ";
    private static String s2 = " ";
    XSharedPreferences xSharedPreferences = new XSharedPreferences( "com.ms.karorkefz" );
    boolean enableLIVE_W = xSharedPreferences.getBoolean( "enableLIVE_W", true );
    boolean enableLIVE_cS = xSharedPreferences.getBoolean( "enableLIVE_cS", true );
    private Object ClickObject;
    private EditText inputEditText;
    private ImageView sendImageView;
    private View view;
    private ClassLoader classLoader;

    Karaoke_Live_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        Log.e( "karorkefz", "进入Live" );
        //禁止滑动
        if (enableLIVE_cS) {
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.live.ui.LivePageViewPager",
                    classLoader,
                    "canScroll",// 被Hook的函数
                    View.class,
                    boolean.class,
                    int.class,
                    int.class,
                    int.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {

                        }

                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( null );
                            //返回false
                            //KtvPageViewPager > canScroll
                        }
                    } );
        }
        if (enableLIVE_W) {
            //自动欢迎
            //得数据信息
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.live.ui.j",
                    classLoader,
                    "a",// 被Hook的函数
                    List.class,
                    new XC_MethodHook() {
                        List<Map<String, String>> list_post = new ArrayList();
                        List<Map<String, String>> list_post1 = new ArrayList();

                        @SuppressLint("ResourceType")
                        @Override
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            list_post = (List<Map<String, String>>) param.args[0];
                            //数组数据拆分
                            for (int i = 0; i < list_post.size(); i++) {
                                list_post1 = Collections.singletonList( list_post.get( 0 ) );
                                Gson ls = new Gson();
                                String jsonString = ls.toJson( list_post1 );
                                try {
                                    // 新建JSONArray
                                    JSONArray jsonArray = new JSONArray( jsonString );
                                    //得到数组下标为0的JSONObject
                                    JSONObject jsonObject0 = jsonArray.getJSONObject( 0 );
                                    //直接可取userId
                                    String g = jsonObject0.getString( "g" );//消息人
                                    String h = jsonObject0.getString( "h" );//消息内容
//                                Log.e( "karorkefz", "Hook" );
                                    Log.e( "karorkefz", "发消息人：" + g + "        消息内容：" + h );
                                    if (g.equals( "默守" )) {
                                        Log.i( "karorkefz", "自己" );
                                        break;
                                    }
                                    if (h.equals( "进入房间" ) && !g.equals( s2 )) {
                                        s2 = g;
                                        new MyThread( g, inputEditText, ClickObject, sendImageView ).Live_init();
                                    }
                                    if (!h.equals( s1 ) && !g.equals( s2 )) {
                                        s1 = h;
                                        s2 = g;
                                        String a1 = "发消息人：" + g + "        消息内容：" + h;

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } );
            //发送函数对象，文本框，发送按钮
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.widget.comment.b",
                    classLoader,
                    "a",// 被Hook的函数
                    View.class,
                    LayoutInflater.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            ClickObject = param.thisObject;
                            Field inputField = XposedHelpers.findField( param.thisObject.getClass(), "c" );
                            Field sendField = XposedHelpers.findField( param.thisObject.getClass(), "o" );
                            inputEditText = (EditText) inputField.get( param.thisObject );
                            sendImageView = (ImageView) sendField.get( param.thisObject );
                        }
                    } );
        }
    }
}