package com.example.boulderjournal.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boulderjournal.R;

import java.util.Calendar;
import java.util.Date;

public final class TimeUtils {



    public static Date getTodaysDate(){
        Date today = new Date();
        return today;
    }

    public static String getClimbDay(Context context, String sharedPrefKey, String climbDayKey){ ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                sharedPrefKey, Context.MODE_PRIVATE);
        String defaultValue = "MONDAY";
        String preferedDay = sharedPreferences.getString(climbDayKey, defaultValue);
        return preferedDay;

    }

    public static int converClimbDayToInt(Context context, String sharedPrefKey, String climbDayKey){
        String day = getClimbDay(context, sharedPrefKey, climbDayKey);
        int dayNumber;

        switch (day) {
            case "MONDAY":
                dayNumber = 1;
                break;
            case "TUESDAY":
                dayNumber = 2;
                break;
            case "WEDNESDAY":
                dayNumber = 3;
                break;
            case "THURSDAY":
                dayNumber = 4;
                break;
            case "FRIDAY":
                dayNumber = 5;
                break;
            case "SATURDAY":
                dayNumber = 6;
                break;
            case "SUNDAY":
                dayNumber = 7;
                break;
            default:
                return 1;
        }

        return dayNumber;

    }

    public static Date calculateNextClimbDay(Context context, String sharedPrefKey, String climbDayKey){
        Calendar calendar = Calendar.getInstance();
        Date date;
        int climbday = converClimbDayToInt(context, sharedPrefKey, climbDayKey);
        Date today = getTodaysDate();
        int todayint = today.getDay();
        int daysUntilClimbDay;
        if(climbday > todayint){
            daysUntilClimbDay = climbday - todayint;
            calendar.add(Calendar.DATE, daysUntilClimbDay);
            date = calendar.getTime();
            return date;
        }else if(todayint > climbday){
            daysUntilClimbDay = (7 - todayint) + climbday;
            calendar.add(Calendar.DATE, daysUntilClimbDay);
            date = calendar.getTime();
            return date;


        }else {
            return date = new Date();
        }
    }

    public static long calculateDelay(Context context, String sharedPrefKey, String climbDayKey){
        Date nextClimbDate = calculateNextClimbDay(context, sharedPrefKey, climbDayKey);
        Date today = getTodaysDate();
        return nextClimbDate.getTime() - today.getTime();
    }

}
