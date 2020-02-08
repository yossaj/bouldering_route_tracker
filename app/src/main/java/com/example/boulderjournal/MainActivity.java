package com.example.boulderjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.boulderjournal.data.AppDatabase;
import com.example.boulderjournal.data.RouteEntry;
import com.example.boulderjournal.notifications.ScheduleReminderUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class MainActivity extends AppCompatActivity implements RouteAdapter.ItemClickListener {

    private static final int ROUTE_LOADER_ID = 3;

    private RouteAdapter mUnfinishedAdapter;
    private RecyclerView mRecycleViewToDo;

    private RouteAdapter mFinishedAdapter;
    private RecyclerView mRecycleViewDone;
    private AppDatabase mDb;

    private MainViewModel viewModel;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScheduleReminderUtil.scheduleReminder(this, getString(R.string.shared_preference_key), getString(R.string.climb_day_key));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userName = currentUser.getDisplayName();

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
        returnToWorkingOn(mRecycleViewDone, mFinishedAdapter);

        mDb = AppDatabase.getInstance(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        retrieveUnfinishedRoutes();
        retrieveFinishedRoutes();

    }


    private void retrieveUnfinishedRoutes() {
        viewModel.getUnFinishedRoutes().observe(this, new Observer<List<RouteEntry>>() {
            @Override
            public void onChanged(List<RouteEntry> routeEntries) {
                mUnfinishedAdapter.setRoutes(routeEntries);
            }
        });

    }

    private void retrieveFinishedRoutes() {
        viewModel.getFinishedRoutes().observe(this, new Observer<List<RouteEntry>>() {
            @Override
            public void onChanged(List<RouteEntry> routeEntries) {
                mFinishedAdapter.setRoutes(routeEntries);
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
        }else if(id == R.id.preferences) {
            Intent launchPreferences = new Intent(MainActivity.this, AppPreferences.class);
            startActivity(launchPreferences);
        }else if(id == R.id.sign_out_menu){
            signOut();
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
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);
    }

    public void deleteWhenSwiped(RecyclerView recyclerView, final RouteAdapter adapter){


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);
    }


    public void returnToWorkingOn(RecyclerView recyclerView, final RouteAdapter adapter){

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
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
                        String status = "false";
                        route.setmComplete(status);
                        mDb.routeDao().updateRoute(route);
                    }
                });

            }
        }).attachToRecyclerView(recyclerView);

    }

    public void signOut() {
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(getBaseContext(),  userName + " : Signed Out", Toast.LENGTH_LONG).show();
            mAuth.signOut();
            Intent returnToSignIn = new Intent(MainActivity.this, LoginActivty.class);
            startActivity(returnToSignIn);
        } else {
            Toast.makeText(getBaseContext(), "Already signed out", Toast.LENGTH_LONG).show();
        }

    }

}
