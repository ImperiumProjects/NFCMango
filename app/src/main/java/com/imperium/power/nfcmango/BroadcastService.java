package com.imperium.power.nfcmango;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Chronometer;

/**
 * Created by power on 06/07/2017.
 */

public class BroadcastService extends Service {

    private final static String tag = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.imperium.power.nfcmango.CountUpTimer";
    Intent bi = new Intent(COUNTDOWN_BR);

    Chronometer sChronometer;
    CountUpTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!NFCScreen.timerStarted){
            Log.i("fkthis", "Starting timer");
            NFCScreen.timerStarted = true;
            cdt = new CountUpTimer(300900) {
                @Override
                public void onTick(int second) {
                    bi.putExtra("countdown", second);
                    sendBroadcast(bi);
                }
                /*@Override
                public void onTick(long millisUntilFinished) {
                    bi.putExtra("countdown", CountUpTimer.second);
                    sendBroadcast(bi);
                }*/

                @Override
                public void onFinish() {
                    Log.i(tag, "Timer Finished");
                }
            };
        }
        cdt.start();
    }
    @Override
    public void onDestroy(){
        cdt.cancel();
        Log.i(tag, "Timer Cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }
}
