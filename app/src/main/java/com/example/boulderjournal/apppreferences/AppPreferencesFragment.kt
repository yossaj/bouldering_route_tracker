package com.example.boulderjournal.apppreferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.example.boulderjournal.R
import com.example.boulderjournal.databinding.FragmentAppPreferencesBinding

class AppPreferencesFragment :  Fragment() {

    private lateinit var dayPicker: MaterialDayPicker
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var context = getContext();
        sharedPreferences = context!!.getSharedPreferences(getString(R.string.shared_preference_key), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val binding : FragmentAppPreferencesBinding   = DataBindingUtil.inflate(
                inflater, R.layout.fragment_app_preferences , container, false)
        binding.setLifecycleOwner(this)
        dayPicker = binding.dayPicker
        getClimbDayPreference()
        setDayPickerListener()

        return binding.root
    }

    fun setDayPickerListener() {
        dayPicker.daySelectionChangedListener = object : MaterialDayPicker.DaySelectionChangedListener {
            override fun onDaySelectionChanged(selectedDay: List<MaterialDayPicker.Weekday>) {
                if(selectedDay.size != 0){
                    val climbDay = selectedDay[0].toString()
                    editor.putString(getString(R.string.climb_day_key), climbDay)
                    editor.commit()
                }
            }
        }
    }

    fun getClimbDayPreference() {
        val defaultValue = "MONDAY"
        var preferedDay = sharedPreferences.getString(getString(R.string.climb_day_key), defaultValue)
        if(preferedDay == null){ preferedDay = defaultValue}
        val day = MaterialDayPicker.Weekday.valueOf(preferedDay)
        dayPicker.selectDay(day)
    }
}
