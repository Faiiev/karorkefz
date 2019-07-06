package com.ms.karorkefz.thread;

import com.ms.karorkefz.util.Log.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XSingleThreadPool {
    private List<Runnable> runList = Collections.synchronizedList( new ArrayList<Runnable>() );
    private a coverThread;
    private long sleep = 11000;
    private Object lock = new Object();

    public XSingleThreadPool() {
        return;
    }

    public void add(Runnable run, long sleep) {
        this.sleep = sleep;
        synchronized (this.lock) {
            if (null == this.coverThread) {
                this.coverThread = new a( this.lock );
                this.coverThread.start();
                LogUtil.e( "karorkefz", "核心线程启动" );
            }
            this.runList.add( run );
        }
    }

    private class a extends Thread {
        private Object lock;

        public a(Object lock) {
            this.lock = lock;
        }

        public void run() {
            while (true) {
                try {
                    Runnable r;
                    synchronized (this.lock) {
                        r = runList.get( 0 );
                        runList.remove( 0 );
                    }
                    r.run();
                    synchronized (this.lock) {
                        if (runList.size() == 0) {
                            coverThread = null;
                            LogUtil.e( "karorkefz", "核心线程停止" );
                            break;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    sleep( sleep );
                } catch (InterruptedException e) {
                    break;
                }

            }
        }
    }
}
