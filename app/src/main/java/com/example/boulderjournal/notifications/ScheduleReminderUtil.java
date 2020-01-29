package com.example.boulderjournal.notifications;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.boulderjournal.Utils.NotificationTimeUtils;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ScheduleReminderUtil {
    private static int REMINDER_SYNC_FLEX_MINUTES = 10;
    private static final int REMINDER_SYNC_FLEX_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_SYNC_FLEX_MINUTES));

    private static final String REMINDER_JOB_TAG = "notes_reminder_tag";
    private static boolean sInitialized = false;

    synchronized public static void scheduleReminder(@NonNull final Context context, String sharedPrefKey, String climbDayKey) {

      long longTimeDelay = NotificationTimeUtils.calculateDelay(context, sharedPrefKey, climbDayKey );
      int REMINDER_INTERVAL_SECONDS = (int)(longTimeDelay);

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(BouderFirebaseJobService.class)
                .setRecurring(true)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + REMINDER_SYNC_FLEX_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);

        sInitialized = true;
    }
}
