package com.example.boulderjournal.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RouteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "routeDb.db";
    private static final int VERSION = 1;

    RouteDbHelper(Context context){
        super(context,DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + RouteContract.TaskEntry.TABLE_NAME + " (" +
                RouteContract.TaskEntry._ID                + " INTEGER PRIMARY KEY, " +
                RouteContract.TaskEntry.COLUMN_ROUTE_NAME + " TEXT NOT NULL, " +
                RouteContract.TaskEntry.COLUMN_ROUTE_COLOUR    + " TEXT NOT NULL, "  +
                RouteContract.TaskEntry.COLUMN_ROOM + " TEXT NOT NULL, " +
                RouteContract.TaskEntry.COLUMN_WALL + " TEXT NOT NULL, " +
                RouteContract.TaskEntry.COLUMN_NOTE + "TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
