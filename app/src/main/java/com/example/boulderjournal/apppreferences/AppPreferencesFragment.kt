package com.example.boulderjournal.apppreferences

import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.example.boulderjournal.R
import com.example.boulderjournal.databinding.FragmentAppPreferencesBinding


class AppPreferencesFragment :  Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding : FragmentAppPreferencesBinding   = DataBindingUtil.inflate(
                inflater, R.layout.fragment_app_preferences , container, false)

        val application = requireNotNull(this.activity).application


        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_app_preferences)
        context = applicationContext
        sharedPreferences = context!!.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE
        )
        editor = sharedPreferences!!.edit()

        dayPicker = findViewById(R.id.day_picker)
        getClimbDayPreference()
        setDayPickerListener()
    }

    fun setDayPickerListener() {
        dayPicker!!.daySelectionChangedListener = object : MaterialDayPicker.DaySelectionChangedListener {
            override fun onDaySelectionChanged(selectedDay: List<MaterialDayPicker.Weekday>) {
                climbDay = selectedDay[0].toString()
                editor!!.putString(getString(R.string.climb_day_key), climbDay)
                editor!!.commit()
            }
        }
    }

    fun getClimbDayPreference() {
        val defaultValue = "MONDAY"
        var preferedDay = sharedPreferences!!.getString(getString(R.string.climb_day_key), defaultValue)
        if(preferedDay == null){ preferedDay = defaultValue}
        val day = MaterialDayPicker.Weekday.valueOf(preferedDay)
        dayPicker!!.selectDay(day)
    }
}
