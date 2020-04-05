package com.example.boulderjournal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteEntry

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val finishedRoutes: LiveData<List<RouteEntry>>?
    val unFinishedRoutes: LiveData<List<RouteEntry>>?

    init {
        val routesDb = AppDatabase.getInstance(this.getApplication())
        unFinishedRoutes = routesDb!!.routeDao().loadUnfinishedRoutes()
        finishedRoutes = routesDb!!.routeDao().loadFinishedRoutes()
    }

}
