package com.example.calista.acceleration_process_4;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


/**
 * This class shows the alert screen where the user can cancel for 300 seconds.
 * After the countdown finishes, the paired phone is instructed to send emergency
 * SMS
 */
public class AlertActivityKal extends Activity {

    NotificationCompat.Builder notification; //set variabel untuk bikin layar notif nya
    private static final int uniqueID = 692030; //tiap notif harus punya unique id sendiri


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        notification = new NotificationCompat.Builder(this); //bikin notifnya
        notification.setAutoCancel(true); //ngapus notif setiap dibuka

        //custom notification
        // notification.setSmallIcon(R.drawable.ic_launcher); //bikin icon notif
        notification.setTicker("Alert: Fall Detected"); //bikin notif pop up
        notification.setWhen(System.currentTimeMillis()); //kasih waktu di notifnya
        notification.setContentTitle("Alert"); //notif center title
        notification.setContentText("Fall detected"); //notif center text

        //otomatis ke app kalo notif dipencet
        Intent intent = new Intent(this, AlertActivityKal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //bikin sesuatu yang ngirim notif
        nm.notify(uniqueID, notification.build()); //aktivitas ngirim notif



    }


}
