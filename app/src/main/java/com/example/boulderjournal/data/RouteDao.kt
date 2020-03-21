package com.example.boulderjournal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RouteDao {

    @Query("SELECT * FROM route")
    fun loadAllRoutes(): LiveData<List<RouteEntry>>?

    @Insert
    fun insertRoute(routeEntry: RouteEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRoute(routeEntry: RouteEntry)

    @Delete
    fun deleteRoute(routeEntry: RouteEntry)

    @Query("SELECT * FROM route WHERE id = :id")
    fun loadRouteById(id: Int): RouteEntry

    @Query("SELECT * FROM route WHERE complete = 'false'")
    fun loadUnfinishedRoutes(): LiveData<List<RouteEntry>>

    @Query("SELECT * FROM route WHERE complete = 'true'")
    fun loadFinishedRoutes(): LiveData<List<RouteEntry>>?

}
