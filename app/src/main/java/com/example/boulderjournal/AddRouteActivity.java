package com.example.boulderjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



//import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_COLOUR;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_NOTE;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROOM;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_COLOUR;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_NAME;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_WALL;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.CONTENT_URI;

public class AddRouteActivity extends AppCompatActivity {

    private String mRouteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
    }

     public void onAddRoute(){

         String mRouteName = ((EditText) findViewById(R.id.input_route_name)).getText().toString();
         String mRouteColor = ((EditText) findViewById(R.id.input_route_colour)).getText().toString();
         String mRoom = ((EditText) findViewById(R.id.input_room)).getText().toString();
         String mWall = ((EditText) findViewById(R.id.input_wall)).getText().toString();
         String mNotes = ((EditText) findViewById(R.id.route_note)).getText().toString();

         if (mRouteName.length() == 0 || mRouteColor.length() == 0 || mRoom.length() == 0 || mWall.length() == 0 || mNotes.length() == 0  ) {
             Toast.makeText(getBaseContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
             return;
         }

         ContentValues contentValues = new ContentValues();
         contentValues.put(COLUMN_ROUTE_NAME, mRouteName);
         contentValues.put(COLUMN_ROUTE_COLOUR, mRouteColor);
         contentValues.put(COLUMN_ROOM, mRoom);
         contentValues.put(COLUMN_WALL, mWall);
         contentValues.put(COLUMN_NOTE, mNotes);

         Uri uri = getContentResolver().insert(CONTENT_URI,contentValues);

         if(uri != null) {
             Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
         }

         finish();

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
}
