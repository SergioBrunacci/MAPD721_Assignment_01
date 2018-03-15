package com.sergio.lotto.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.sergio.lotto.notification.NotificationDecorator;
import java.util.Arrays;


public class LotteryNumberService extends Service {
    private static final String TAG = "LotteryNumberService";

    public static final String MSG_CMD = "msg_cmd";
    public static final int CMD_STOP_SERVICE = 50;
    public static final int CMD_GENERATE_NUMBER = 60;

    private NotificationManager notificationMgr;
    private PowerManager.WakeLock wakeLock;
    private NotificationDecorator notificationDecorator;

    public LotteryNumberService() {
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();
        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationMgr);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();
            handleData(data);
            if (!wakeLock.isHeld()) {
                Log.v(TAG, "acquiring wake lock");
                wakeLock.acquire();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        notificationMgr.cancelAll();
        Log.v(TAG, "releasing wake lock");
        wakeLock.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleData(Bundle data) {
        int command = data.getInt(MSG_CMD);
        int[]lotteryNumbers = new int[6];
        for(int i=1; i<6; i++){
            for(int ii = 0; ii < lotteryNumbers.length; ii++){
                lotteryNumbers[ii] = (int)(Math.random()*49 + 1);
            }
            Arrays.sort(lotteryNumbers);
        }
        Log.d(TAG, "-(<- received command data to service: command=" + command);
        if (command == CMD_STOP_SERVICE) {
            notificationDecorator.displaySimpleNotification("Stopping service...", "Service Stopped...");
        } else if (command == CMD_GENERATE_NUMBER) {
            notificationDecorator.displaySimpleNotification("Received Data...", Arrays.toString(lotteryNumbers));
        } else {
            Log.w(TAG, "Ignoring Unknown Command! id=" + command);
        }
    }
}

