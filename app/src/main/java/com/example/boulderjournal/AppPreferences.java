package com.example.boulderjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AppPreferences extends AppCompatActivity {

    private MaterialDayPicker dayPicker;
    private String climbDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_preferences);
        dayPicker = (MaterialDayPicker)findViewById(R.id.day_picker);
        setDayPickerListener();

    }

    public void setDayPickerListener(){
        dayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(List<MaterialDayPicker.Weekday> selectedDay) {
                 climbDay = selectedDay.toString();
            }
        });
    }
}
