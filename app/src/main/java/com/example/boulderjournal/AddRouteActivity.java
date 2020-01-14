package com.example.boulderjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boulderjournal.data.AppDatabase;
import com.example.boulderjournal.data.RouteEntry;

import java.util.Date;


public class AddRouteActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    private static final int DEFAULT_TASK_ID = -1;

    private EditText mRouteName;
    private EditText mRouteColor;
    private EditText mRoom;
    private EditText mWall;
    private EditText mNotes;

    private Button mUpdateButton;

    private int mRouteId = DEFAULT_TASK_ID;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        initViews();
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mRouteId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent =  getIntent();
        if(intent != null & intent.hasExtra(EXTRA_TASK_ID )){

            final int routeID = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID) ;

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final RouteEntry route = mDb.routeDao().loadRouteById(routeID);
                    populateUI(route);
                }
            });

        }
    }

     public void onAddRoute(){

            String routeName = mRouteName.getText().toString();
            String routeColour = mRouteColor.getText().toString();
            String room = mRoom.getText().toString();
            String wall = mWall.getText().toString();
            String notes = mNotes.getText().toString();
            Date date = new Date();


         if (routeName.length() == 0 || routeColour.length() == 0 || room.length() == 0 || wall.length() == 0 || notes.length() == 0  ) {
             Toast.makeText(getBaseContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
             return;
         }

            final RouteEntry routeEntry = new RouteEntry(routeName, routeColour, room, wall, notes, date);

         AppExecutors.getInstance().diskIO().execute(new Runnable() {
             @Override
             public void run() {
                 mDb.routeDao().insertRoute(routeEntry);
                 finish();
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
            onAddRoute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initViews(){
        mRouteName = (EditText) findViewById(R.id.input_route_name);
        mRouteColor = (EditText)findViewById(R.id.input_route_colour);
        mRoom = (EditText)findViewById(R.id.input_room);
        mWall = (EditText)findViewById(R.id.input_wall);
        mNotes = (EditText)findViewById(R.id.route_note);

        mUpdateButton = (Button)findViewById(R.id.updateButton);

    }

    public void populateUI(RouteEntry routeEntry){
        mRouteName.setText(routeEntry.getRouteName());
        mRouteColor.setText(routeEntry.getRouteColour());
        mRoom.setText(routeEntry.getRoom());
        mWall.setText(routeEntry.getWall());
        mNotes.setText(routeEntry.getNote());
        mUpdateButton.setVisibility(View.VISIBLE);
    }
;}
