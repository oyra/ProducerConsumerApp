package com.oyra.pw.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.oyra.pw.data.DataHolder;
import com.oyra.pw.data.Unit;

public class ProducerService extends Service {

    @SuppressWarnings("FieldCanBeLocal")
    private final long TIMEOUT_NANOS = 6 * 1000;
    public static volatile boolean sKeepRunning = true;


    public ProducerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            public void run() {

                while (sKeepRunning) {
                    DataHolder.sQueue.add(new Unit());

                    try {
                        Thread.sleep(TIMEOUT_NANOS);
                    } catch (InterruptedException e) {
                        Log.e("error", "error", e);
                    }


                }
            }
        }.start();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
