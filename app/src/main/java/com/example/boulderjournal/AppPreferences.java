package com.example.boulderjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AppPreferences extends AppCompatActivity {

    private MaterialDayPicker dayPicker;
    private SharedPreferences sharedPreferences;
    private String climbDay;
    private SharedPreferences.Editor editor;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_preferences);
        context = getApplicationContext();
         sharedPreferences = context.getSharedPreferences(
                 getString(R.string.shared_preference_key), Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();

        dayPicker = (MaterialDayPicker)findViewById(R.id.day_picker);
        getClimbDayPreference();
        setDayPickerListener();

    }

    public void setDayPickerListener(){
        dayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(List<MaterialDayPicker.Weekday> selectedDay) {
                 climbDay = selectedDay.get(0).toString() ;
                 editor.putString(getString(R.string.climb_day_key),climbDay);
                 editor.commit();
            }
        });
    }

    public void getClimbDayPreference(){
        String defaultValue = "MONDAY";
        String preferedDay = sharedPreferences.getString(getString(R.string.climb_day_key), defaultValue);
        MaterialDayPicker.Weekday day = MaterialDayPicker.Weekday.valueOf(preferedDay);
        dayPicker.selectDay(day);
    }
}
