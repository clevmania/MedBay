package com.clevmania.medbay.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.clevmania.medbay.MainActivity;

/**
 * Created by grandilo-lawrence on 4/18/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIF_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        Uri alarmBeep = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent summaryIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,summaryIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle("You're one step to feeling better")
                .setContentText("It's time to take your medications")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("It's time to take your medications"))
                .setSound(alarmBeep)
                .setWhen(when)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIF_ID,mBuilder.build());
    }
}
