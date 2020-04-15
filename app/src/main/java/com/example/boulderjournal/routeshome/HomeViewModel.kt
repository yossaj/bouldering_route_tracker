package com.example.boulderjournal.routeshome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.boulderjournal.AppExecutors
import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteEntry

class HomeViewModel(
        val mDb : AppDatabase?,
        application: Application): AndroidViewModel(application){

    val finishedRoutes: LiveData<List<RouteEntry>>?
    val unFinishedRoutes: LiveData<List<RouteEntry>>?
    val routesDb = AppDatabase.getInstance(this.getApplication())

    init {

        unFinishedRoutes = routesDb!!.routeDao.loadUnfinishedRoutes()
        finishedRoutes = routesDb!!.routeDao.loadFinishedRoutes()
    }

    fun swipeTo(action : String, recyclerView: RecyclerView?, adapter: RouteAdapter?) {

        val direction =
                when(action){
                    "Delete" -> ItemTouchHelper.LEFT
                    "MoveToDone" -> ItemTouchHelper.LEFT
                    "MoveToWorkingOn" -> ItemTouchHelper.RIGHT
                    else -> ItemTouchHelper.DOWN
                }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, direction) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                AppExecutors.instance!!.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val route = adapter!!.getRouteByPosition(position)
                    when(action){

                        "Delete" -> mDb!!.routeDao.deleteRoute(route)
                        "MoveToDone" -> {
                            route.setmComplete("true")
                            mDb!!.routeDao.updateRoute(route)
                        }
                        "MoveToWorkingOn" -> {
                            route.setmComplete("false")
                            mDb!!.routeDao.updateRoute(route)
                        }
                    }
                }
            }
        }).attachToRecyclerView(recyclerView)
    }

}