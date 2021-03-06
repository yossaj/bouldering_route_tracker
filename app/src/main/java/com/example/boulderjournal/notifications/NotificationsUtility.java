package com.example.boulderjournal.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.example.boulderjournal.LoginActivty;
import com.example.boulderjournal.R;

public class NotificationsUtility {

    private static final int NOTES_REMINDER_NOTIFICATION_ID = 3345;
    private static final int NOTES_REMINDER_PENDING_INTENT_ID = 4456;

    public static void makeNotesReminder(Context context){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.note_notification))
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentTitle(context.getString(R.string.reminder_title))
                    .setContentText(context.getString(R.string.reminder_content))
                    .setSmallIcon(R.drawable.stone)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.reminder_content)))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(contentIntent(context))
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            notificationManager.notify(NOTES_REMINDER_NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, LoginActivty.class);
        return PendingIntent.getActivity(
                context,
                NOTES_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
