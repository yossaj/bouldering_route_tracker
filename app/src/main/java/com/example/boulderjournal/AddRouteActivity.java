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
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulderjournal.data.AppDatabase;
import com.example.boulderjournal.data.RouteEntry;

import java.util.Date;


public class AddRouteActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTE_ID = "extraTaskId";
    public static final String INSTANCE_ROUTE_ID = "instanceTaskId";
    private static final int DEFAULT_ROUTE_ID = -1;

    private EditText mRouteName;
    private EditText mRouteColour;
    private EditText mRoom;
    private EditText mWall;
    private EditText mNotes;

    private TextView mRouteNameTV;
    private TextView mRouteColourTV;
    private TextView mRoomTV;
    private TextView mWallTV;
    private TextView mNotesTV;


    private Button mUpdateButton;

    private int mRouteId = DEFAULT_ROUTE_ID;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        initViews();
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ROUTE_ID)) {
            mRouteId = savedInstanceState.getInt(INSTANCE_ROUTE_ID, DEFAULT_ROUTE_ID);

        }

        Intent intent =  getIntent();
        if(intent != null & intent.hasExtra(EXTRA_ROUTE_ID)){
            initStaticViews();

            final int routeID = intent.getIntExtra(EXTRA_ROUTE_ID, DEFAULT_ROUTE_ID) ;

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
            String routeColour = mRouteColour.getText().toString();
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
        mRouteColour = (EditText)findViewById(R.id.input_route_colour);
        mRoom = (EditText)findViewById(R.id.input_room);
        mWall = (EditText)findViewById(R.id.input_wall);
        mNotes = (EditText)findViewById(R.id.route_note);
    }

    public void initStaticViews(){


        mRouteNameTV = (TextView)findViewById(R.id.view_route_name);
        mRouteColourTV = (TextView)findViewById(R.id.view_route_colour);
        mRoomTV = (TextView)findViewById(R.id.view_room);
        mWallTV = (TextView)findViewById(R.id.view_wall);
        mNotesTV = (TextView)findViewById(R.id.view_route_notes);
        mUpdateButton = (Button)findViewById(R.id.updateButton);
    }

    public void populateUI(RouteEntry routeEntry){

        mRouteNameTV.setText(routeEntry.getRouteName());
        mRouteNameTV.setVisibility(View.VISIBLE);
        mRouteName.setVisibility(View.GONE);

        mRouteColourTV.setVisibility(View.VISIBLE);
        mRouteColourTV.setText(routeEntry.getRouteColour());
        mRouteColour.setVisibility(View.GONE);


        mRoomTV.setText(routeEntry.getRoom());
        mRoomTV.setVisibility(View.VISIBLE);
        mRoom.setVisibility(View.GONE);


        mWallTV.setText(routeEntry.getWall());
        mWallTV.setVisibility(View.VISIBLE);
        mWall.setVisibility(View.GONE);


        mNotesTV.setText(routeEntry.getNote());
        mNotesTV.setVisibility(View.VISIBLE);
        mNotes.setVisibility(View.GONE);


    }
;}
