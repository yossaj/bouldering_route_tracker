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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddRouteActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTE_ID = "extraTaskId";
    public static final String INSTANCE_ROUTE_ID = "instanceTaskId";
    private static final int DEFAULT_ROUTE_ID = -1;

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    private EditText mRouteName;
    private EditText mRouteColour;
    private EditText mRoom;
    private EditText mWall;
    private EditText mNotes;

    private TextView mDateTV;
    private TextView mRouteNameTV;
    private TextView mRouteColourTV;
    private TextView mRoomTV;
    private TextView mWallTV;
    private TextView mNotesTV;

    private String routeName;
    private String routeColour;
    private String room;
    private String wall;
    private String notes;
    private Date date;

    private boolean editMenuCheck = false;
    private boolean readyUpdateCheck = false;

    private MenuItem editMenuItem;
    private MenuItem addMenuItem;

    private RouteEntry route;


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
            editMenuCheck = true;
            initStaticViews();

            final int routeID = intent.getIntExtra(EXTRA_ROUTE_ID, DEFAULT_ROUTE_ID) ;

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                     route = mDb.routeDao().loadRouteById(routeID);
                    populateStaticUI(route);
                }
            });

        }
    }

    public void setValues(){
        routeName = mRouteName.getText().toString();
        routeColour = mRouteColour.getText().toString();
        room = mRoom.getText().toString();
        wall = mWall.getText().toString();
        notes = mNotes.getText().toString();
        if(date == null){  date = new Date();}


        if (routeName.length() == 0 || routeColour.length() == 0 || room.length() == 0 || wall.length() == 0 || notes.length() == 0  ) {
            Toast.makeText(getBaseContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

       if(route == null){
           route = new RouteEntry(routeName, routeColour, room, wall, notes, date);}
       else{
           route.setRouteName(routeName);
           route.setRouteColour(routeColour);
           route.setRoom(room);
           route.setWall(wall);
           route.setNote(notes);
       }
    }

     public void onAddRoute(){

         setValues();

         AppExecutors.getInstance().diskIO().execute(new Runnable() {
             @Override
             public void run() {
                 mDb.routeDao().insertRoute(route);
                 finish();
             }
         });
     }

     public void onUpdateRoute(){

        setValues();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.routeDao().updateRoute(route);
                finish();
            }
        });

     }




    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        editMenuItem = menu.findItem(R.id.edit_menu_item);
        addMenuItem = menu.findItem(R.id.add);

        if(editMenuCheck){
        editMenuItem.setVisible(editMenuCheck);
        addMenuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
        }else if(id == R.id.edit_menu_item && editMenuCheck){
            populateEditableUI(route);
        }else if(id == R.id.edit_menu_item && readyUpdateCheck){
            Toast.makeText(getBaseContext(), "Ready", Toast.LENGTH_LONG).show();
            onUpdateRoute();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initViews(){

        String newFormattedDate = dateFormat.format(new Date());
        mDateTV  = (TextView)findViewById(R.id.date_added);
        mDateTV.setText(newFormattedDate);

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

    public void populateStaticUI(RouteEntry routeEntry){

        String formattedDate = dateFormat.format(routeEntry.getUpdatedAt());
        mDateTV.setText(formattedDate);

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

    public void populateEditableUI(RouteEntry routeEntry){

        mRouteName.setText(routeEntry.getRouteName());
        mRouteName.setVisibility(View.VISIBLE);
        mRouteNameTV.setVisibility(View.GONE);

        mRouteColourTV.setVisibility(View.GONE);
        mRouteColour.setText(routeEntry.getRouteColour());
        mRouteColour.setVisibility(View.VISIBLE);


        mRoom.setText(routeEntry.getRoom());
        mRoom.setVisibility(View.VISIBLE);
        mRoomTV.setVisibility(View.GONE);


        mWall.setText(routeEntry.getWall());
        mWall.setVisibility(View.VISIBLE);
        mWallTV.setVisibility(View.GONE);


        mNotes.setText(routeEntry.getNote());
        mNotes.setVisibility(View.VISIBLE);
        mNotesTV.setVisibility(View.GONE);

        editMenuCheck = false;
        readyUpdateCheck = true;
        editMenuItem.setTitle(R.string.update);
    }


;}
