package com.example.boulderjournal.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM route")
    LiveData<List<RouteEntry>> loadAllRoutes();

    @Insert
    void insertRoute(RouteEntry routeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRoute(RouteEntry routeEntry);

    @Delete
    void deleteRoute(RouteEntry routeEntry);

    @Query("SELECT * FROM route WHERE id = :id")
    RouteEntry loadRouteById(int id);

    @Query("SELECT * FROM route WHERE complete = 'false'")
    LiveData<List<RouteEntry>> loadUnfinishedRoutes();

    @Query("SELECT * FROM route WHERE complete = 'true'")
    LiveData<List<RouteEntry>> loadFinishedRoutes();

}
