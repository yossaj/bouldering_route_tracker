package com.example.boulderjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.boulderjournal.data.AppDatabase;
import com.example.boulderjournal.data.RouteEntry;

import java.util.List;


public class MainActivity extends AppCompatActivity implements RouteAdapter.ItemClickListener {

    private static final int ROUTE_LOADER_ID = 3;

    private RouteAdapter mUnfinishedAdapter;
    private RecyclerView mRecycleViewToDo;

    private RouteAdapter mFinishedAdapter;
    private RecyclerView mRecycleViewDone;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleViewToDo = (RecyclerView)findViewById(R.id.recyclerRoutesToDo);
        mRecycleViewToDo.setLayoutManager(new LinearLayoutManager(this));
        mUnfinishedAdapter = new RouteAdapter(this, this);
        mRecycleViewToDo.setAdapter(mUnfinishedAdapter);

        mRecycleViewDone = (RecyclerView)findViewById(R.id.recyclerRoutesDone);
        mFinishedAdapter = new RouteAdapter(this,this);
        mRecycleViewDone.setLayoutManager(new LinearLayoutManager(this));
        mRecycleViewDone.setAdapter(mFinishedAdapter);

        moveToDoneWhenSwiped(mRecycleViewToDo, mUnfinishedAdapter);
        deleteWhenSwiped(mRecycleViewDone, mFinishedAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveUnfinishedRoutes();
        retrieveFinishedRoutes();
    }

    private void retrieveUnfinishedRoutes() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<RouteEntry> unfinishedRoutes = mDb.routeDao().loadUnfinishedRoutes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUnfinishedAdapter.setRoutes(unfinishedRoutes);
                    }
                });
            }
        });
    }

    private void retrieveFinishedRoutes() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<RouteEntry> finishedRoutes = mDb.routeDao().loadFinishedRoutes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            mFinishedAdapter.setRoutes(finishedRoutes);
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_route_menu_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            Intent addNewRoute = new Intent(MainActivity.this, AddRouteActivity.class);
            startActivity(addNewRoute);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, AddRouteActivity.class);
        intent.putExtra(AddRouteActivity.EXTRA_ROUTE_ID, itemId);
        startActivity(intent);

    }

    public void moveToDoneWhenSwiped(RecyclerView recyclerView, final RouteAdapter adapter){


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(final RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<RouteEntry> routeEntries = adapter.getRoutes();
                        RouteEntry route = routeEntries.get(position);
                        String status = "true";
                        route.setmComplete(status);
                        mDb.routeDao().updateRoute(route);
                        retrieveUnfinishedRoutes();
                        retrieveFinishedRoutes();
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);
    }

    public void deleteWhenSwiped(RecyclerView recyclerView, final RouteAdapter adapter){


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(final RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<RouteEntry> routeEntries = adapter.getRoutes();
                        RouteEntry route = routeEntries.get(position);
                        mDb.routeDao().deleteRoute(route);
                        retrieveFinishedRoutes();
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);
    }
}
