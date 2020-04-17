package com.example.boulderjournal.notifications

import android.content.Context
import com.example.boulderjournal.Utils.NotificationTimeUtils
import com.firebase.jobdispatcher.*
import java.util.concurrent.TimeUnit

object ScheduleReminderUtil {
    private const val REMINDER_SYNC_FLEX_MINUTES = 1
    private val REMINDER_SYNC_FLEX_SECONDS = TimeUnit.MINUTES.toSeconds(REMINDER_SYNC_FLEX_MINUTES.toLong()).toInt()
    private const val REMINDER_JOB_TAG = "notes_reminder_tag"
    private var sInitialized = false

    @Synchronized
    fun scheduleReminder(context: Context?, sharedPrefKey: String?, climbDayKey: String?) {
        val longTimeDelay = NotificationTimeUtils.calculateDelay(context, sharedPrefKey, climbDayKey)
        val REMINDER_INTERVAL_SECONDS = longTimeDelay.toInt()
        if (sInitialized) return
        val driver: Driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)
        val constraintReminderJob = dispatcher.newJobBuilder()
                .setService(BouderFirebaseJobService::class.java)
                .setRecurring(true)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + REMINDER_SYNC_FLEX_SECONDS))
                .setReplaceCurrent(true)
                .build()
        dispatcher.schedule(constraintReminderJob)
        sInitialized = true
    }
}