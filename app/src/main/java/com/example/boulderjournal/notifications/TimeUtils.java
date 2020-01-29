package com.example.boulderjournal.notifications;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boulderjournal.R;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils extends AppCompatActivity {

    Context context;


    public Date getTodaysDate(){
        Date today = new Date();
        return today;
    }

    public String GetClimbDay( ){
        context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        String defaultValue = "MONDAY";
        String preferedDay = sharedPreferences.getString(getString(R.string.climb_day_key), defaultValue);
        return preferedDay;

    }

    public int converClimbDayToInt(){

        switch (month) {
            case "JAN":
                monthNumber = 1;
                break;
            case "FEB":
                monthNumber = 2;
                break;
            case "MAR":
                monthNumber = 3;
                break;
            case "APR":
                monthNumber = 4;
                break;
            case "MAY":
                monthNumber = 5;
                break;
            case "JUN":
                monthNumber = 6;
                break;
            case "JUL":
                monthNumber = 7;
                break;
            case "AUG":
                monthNumber = 8;
                break;
            case "SEP":
                monthNumber = 9;
                break;
            case "OCT":
                monthNumber = 10;
                break;
            case "NOV":
                monthNumber = 11;
                break;
            case "DEC":
                monthNumber = 12;
                break;
            default:
                return null;
        }

    }

    public void calculateNextClimbDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.getFirstDayOfWeek();
    }

}
