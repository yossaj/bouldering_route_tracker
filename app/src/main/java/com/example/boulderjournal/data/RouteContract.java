package com.example.boulderjournal.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RouteContract
{
    public static final String AUTHORITY = "com.example.boulderjournal";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_ROUTES = "route";

    public static final class RouteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTES).build();


        public static final String TABLE_NAME = "route";

        //  Still need to add date column
        public static final String COLUMN_ROUTE_NAME = "route_name";
        public static final String COLUMN_ROUTE_COLOUR = "route_colour";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_WALL = "wall";
        public static final String COLUMN_NOTE = "note";
    }
}
