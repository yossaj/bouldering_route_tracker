package com.example.boulderjournal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.boulderjournal.data.AppDatabase;
import com.example.boulderjournal.data.RouteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<RouteEntry>> finishedRoutes;
    private LiveData<List<RouteEntry>> unFinishedRoutes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase routesDb = AppDatabase.getInstance(this.getApplication());
        unFinishedRoutes = routesDb.routeDao().loadUnfinishedRoutes();
        finishedRoutes = routesDb.routeDao().loadFinishedRoutes();

    }

    public LiveData<List<RouteEntry>> getUnFinishedRoutes() {
        return unFinishedRoutes;
    }

    public LiveData<List<RouteEntry>> getFinishedRoutes() {
        return finishedRoutes;
    }

}
