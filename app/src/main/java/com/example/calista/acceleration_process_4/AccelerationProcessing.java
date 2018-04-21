package com.example.calista.acceleration_process_4;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;



/**
 * Created by calista on 15/04/2018.
 */

/** Fall processing dari HEALTH-E**/

public class AccelerationProcessing extends Service implements SensorEventListener{




    //save values and timestamps in separate buffers so we don't need an additional class to store both



    //HEALTH E

    float gravity[] = {0f, 0f, 0f}, linear_acceleration[] = {0f, 0f, 0f}; //0f = float 0
    double Zvalue, totLinear, totAcc, fallAcc, FallCounter = 0, threshold = 14, g = 9.8;

    AlertDialog popup;
    CountDownTimer time;
    long[] pattern = {400, 400};


    SensorManager sensorManager;
    Sensor sensorAcc;

    boolean flag = false, detected = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
       // di tutorial gaada, kalo gabisa dimasukin super.onCreate();
        // Create our Sensor Manager
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        // Accelerometer Sensor
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        // Register sensor Listener
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL); //*120418 13333 delaynya
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        stopForeground(true);
        sensorManager.unregisterListener((SensorEventListener) this, sensorAcc); //diubah

    }

    @Override
    public void onAccuracyChanged(Sensor sensorAcc, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update the accelerometer

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = (float) 0.8;
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]; // gravity = 0.8 * gravity[0] + (1-0.8) * acceleration
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            Log.i("Fall", " gravity 0:" + gravity[0] + " gravity 1: " + gravity[1] + " gravity 2: " + gravity[2]);
            Log.i("Fall", " acc 0:" + event.values[0] + " gravity 1: " + event.values[1] + " gravity 2: " + event.values[2]);
            Log.i("Fall", " linear 0:" + linear_acceleration[0] + " linear 1: " + linear_acceleration[1] + " linear 2: " + linear_acceleration[2]);

            totAcc = Math.sqrt(event.values[0] * event.values[0] +
                    event.values[1] * event.values[1] +
                    event.values[2] * event.values[2]);

            Log.i("Fall", "totAcc = " + totAcc);

            totLinear = Math.sqrt(linear_acceleration[0] * linear_acceleration[0] +
                    linear_acceleration[1] * linear_acceleration[1] +
                    linear_acceleration[2] * linear_acceleration[2]);

            Log.i("Fall", "totLinear = " + totLinear);

            Zvalue = ((totAcc * totAcc) - (totLinear * totLinear) - (g * g))/(2 *g);

            Log.i("Fall", "Z value = " + Zvalue);

        }

        flag = false;

        FallCounter = ((Zvalue > threshold) ? FallCounter + 1 : 0); //if fall counter = totacc > threshold, fallcounter = +1, else 0.

        if (Zvalue > threshold) {
            Log.i("Fall", "melebihi threshold");
            notifiy();
        }

        Log.i("Fall", "fall counter = " + FallCounter);

        if (FallCounter == 5) { //if (FallCounter == 5 && !detected)
            Log.i("Fall", "FALL DETECTED");

        }
    }

    public void notifiy() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RSSPullService");

        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = getApplicationContext();

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context)
                .setContentTitle("Alert")
                .setContentText("Fall Detected")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);



    }

    public void fallDetectionAction() {

        Log.i("Fall", "FALL DETECTED");
        detected = true;


    }



    /**
     * add a reading to the backBuffer
     * @param n reading acceleration magnitude
     * @param t reading timestamp
     */

}
