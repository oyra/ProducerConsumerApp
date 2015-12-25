package com.oyra.pw.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.oyra.pw.data.DataHolder;
import com.oyra.pw.data.Unit;

import java.util.Random;

public class ConsumerService extends Service {

    public final static String UPDATE_UI_EVENT = "update_ui";
    public static volatile boolean sKeepRunning = true;

    @SuppressWarnings("FieldCanBeLocal")
    private final long TIMEOUT_NANOS = 15 * 1000;
    private Random mRandom = new Random();
    /*
     probability in percents
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int ERROR_PROBABILITY = 20;


    public ConsumerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread() {
            public void run() {

                while (sKeepRunning) {

                    if (!process()) {
                        continue;
                    }
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


    private boolean process() {
        if (DataHolder.sQueue.isEmpty()) {
            return false;
        }
        Unit unit = DataHolder.sQueue.poll();

        Intent i = new Intent(UPDATE_UI_EVENT);
        boolean fail = isError();
        if (fail) {
            //back to the queue
            DataHolder.sQueue.add(unit);
            //send info to activity
            i.putExtra("fail", true);
        } else {
            i.putExtra("unit", unit);
            i.putExtra("queue_size", DataHolder.sQueue.size());
        }


        boolean hasReceivers = LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
        if (!hasReceivers && !fail) {
            DataHolder.sQueue.add(unit);
        }
        return true;
    }

    private boolean isError() {
        return mRandom.nextInt(100) < ERROR_PROBABILITY;
    }


}
