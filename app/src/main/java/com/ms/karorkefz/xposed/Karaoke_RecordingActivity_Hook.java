package com.ms.karorkefz.xposed;

import com.ms.karorkefz.util.Log.LogUtil;

public class Karaoke_RecordingActivity_Hook {
    int i;
    private ClassLoader classLoader;
    private boolean b = true;

    Karaoke_RecordingActivity_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入RecordingActivity" );
        new Karaoke_KaraScore_Hook( classLoader ).init();
    }
}
