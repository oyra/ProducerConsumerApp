package com.oyra.pw.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.oyra.pw.R;
import com.oyra.pw.data.Unit;
import com.oyra.pw.services.ConsumerService;
import com.oyra.pw.services.ProducerService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Intent mProducerIntent;
    private Intent mConsumerIntent;

    @Bind(R.id.queue_size)
    TextView mQueueSizeText;
    @Bind(R.id.name)
    TextView mNameText;
    @Bind(R.id.error)
    TextView mErrorText;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean fail = intent.getBooleanExtra("fail", false);
            if (fail) {//an error occurred
                setError(true);
                return;
            }
            //everything's fine, show the data
            Unit unit = (Unit) intent.getSerializableExtra("unit");
            int size = intent.getIntExtra("queue_size", -1);
            showData(unit == null ? "" : unit.getName(), size);
            setError(false);

        }
    };

    private void showData(String name, int size) {
        mNameText.setText(name);
        mQueueSizeText.setText("" + size);
    }

    private void setError(boolean error) {
        mErrorText.setVisibility(error ? View.VISIBLE : View.GONE);
        mNameText.setVisibility(error ? View.GONE : View.VISIBLE);
        mQueueSizeText.setVisibility(error ? View.GONE : View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(ConsumerService.UPDATE_UI_EVENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServices();
    }

    private void startServices() {
        mProducerIntent = new Intent(this, ProducerService.class);
        mConsumerIntent = new Intent(this, ConsumerService.class);
        startService(mProducerIntent);
        startService(mConsumerIntent);
    }

    private void stopServices() {
        ConsumerService.sKeepRunning = false;
        ProducerService.sKeepRunning = false;
        if (mProducerIntent != null) {
            stopService(mProducerIntent);
        }
        if (mConsumerIntent != null) {
            stopService(mConsumerIntent);
        }
    }
}
