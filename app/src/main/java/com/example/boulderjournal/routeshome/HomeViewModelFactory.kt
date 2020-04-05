package com.example.boulderjournal.routeshome

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.boulderjournal.data.RouteDao

class HomeViewModelFactory(
        private val dataSource: RouteDao?, private val application: Application) : ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
                    return HomeViewModel(application) as T
                }
            throw IllegalArgumentException("Unknown ViewModel class")
    }

}