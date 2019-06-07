package com.ms.karorkefz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XSingleThreadPool {
    private List<Runnable> runList = Collections.synchronizedList( new ArrayList<Runnable>() );
    private a coverThread;
    private Object lock = new Object();

    public XSingleThreadPool() {
        return;
    }

    public void add(Runnable run) {
        synchronized (this.lock) {
            if (null == this.coverThread) {
                this.coverThread = new a( this.lock );
                this.coverThread.start();
                System.out.println( "核心线程启动" );
            }
            this.runList.add( run );
        }
    }

    private class a extends Thread {
        private Object lock;
        private long sleep = 12000;

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
                            System.out.println( "核心线程停止" );
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
