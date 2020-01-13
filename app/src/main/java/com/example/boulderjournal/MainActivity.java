package com.example.boulderjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity {

    private static final int ROUTE_LOADER_ID = 3;

    private RouteAdapter mAdapter;
    private RecyclerView mRecycleView;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView)findViewById(R.id.recyclerRoutesToDo);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RouteAdapter(this);
        mRecycleView.setAdapter(mAdapter);
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                int id = (int) viewHolder.itemView.getTag();
//
//                String stringId = Integer.toString(id);
//                Uri uri = CONTENT_URI;
//                uri = uri.buildUpon().appendPath(stringId).build();
//                getContentResolver().delete(uri, null, null);
//
//            }
//        }).attachToRecyclerView(mRecycleView);

        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
       AppExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               final List<RouteEntry> routes = mDb.routeDao().loadAllRoutes();

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mAdapter.setRoutes(routes);
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

}
