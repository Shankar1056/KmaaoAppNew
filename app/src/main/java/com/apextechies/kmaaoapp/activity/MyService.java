package com.apextechies.kmaaoapp.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Shankar on 2/12/2018.
 */

public class MyService extends Service {
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    Callbacks activity;
    private long startTime = 0;
    private long millis = 0;
    private final IBinder mBinder = new LocalBinder();
    Handler handler = new Handler();
    Runnable serviceRunnable;
    int count = 0;
     Handler handler1;
    final int delay = 1000; //milliseconds
    boolean stop = false;




    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        serviceRunnable = new Runnable() {
            @Override
            public void run() {
                handler1 = new Handler();
                /*millis = System.currentTimeMillis() - startTime;
                activity.updateClient(millis); //Update Activity (client) by the implementd callback
                handler.postDelayed(this, 1000);
*/


                handler1.postDelayed(new Runnable() {
                    public void run() {
                        count++;
                        activity.updateClient(count);

                        handler1.postDelayed(this, 1000);
                    }
                }, delay);


            }
        };
        //Do what you need in onStartCommand when service has been started
//        return START_NOT_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //returns the instance of the service
    public class LocalBinder extends Binder {
        public MyService getServiceInstance() {
            return MyService.this;
        }
    }

    //Here Activity register to the service as Callbacks client
    public void registerClient(Activity activity) {
        this.activity = (Callbacks) activity;
        count = 0;
        handler.removeCallbacks(serviceRunnable);
    }

    public void startCounter() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(serviceRunnable, 0);
     //   Toast.makeText(getApplicationContext(), "Counter started", Toast.LENGTH_SHORT).show();
    }

    public void stopCounter() {
        handler.removeCallbacks(serviceRunnable);
        handler.removeCallbacksAndMessages(0);
    }


    //callbacks interface for communication with service clients!
    public interface Callbacks {
        public void updateClient(long data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
