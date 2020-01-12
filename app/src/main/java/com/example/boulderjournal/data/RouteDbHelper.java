package com.example.boulderjournal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_COLOUR;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_NOTE;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROOM;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_COLOUR;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_NAME;

import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_WALL;
import static com.example.boulderjournal.data.RouteContract.RouteEntry.TABLE_NAME;

public class RouteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "routeDb.db";
    private static final int VERSION = 14;

    RouteDbHelper(Context context){
        super(context,DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        final String CREATE_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                RouteContract.RouteEntry._ID  + " INTEGER PRIMARY KEY, " +
                COLUMN_ROUTE_NAME + " TEXT NOT NULL, " +
                COLUMN_ROUTE_COLOUR + " TEXT NOT NULL, " +
                COLUMN_ROOM + " TEXT NOT NULL, " +
                COLUMN_WALL + " TEXT NOT NULL, " +
                COLUMN_NOTE + " TEXT NOT NULL);"
                ;

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
