package com.example.boulderjournal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteEntry

class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    val finishedRoutes: LiveData<List<RouteEntry>>?
    val unFinishedRoutes : LiveData<List<RouteEntry>>?

    init {
        val routesDbDao = AppDatabase.getInstance(this.getApplication())?.routeDao()
        unFinishedRoutes = routesDbDao?.loadUnfinishedRoutes()
        finishedRoutes = routesDbDao?.loadFinishedRoutes()
    }


}
