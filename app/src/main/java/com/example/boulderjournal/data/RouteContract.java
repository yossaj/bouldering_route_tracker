package com.example.boulderjournal.data;

import android.provider.BaseColumns;

public class RouteContract {

    public static final class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "routes";

        public static final String COLUMN_ROUTE_NAME = "route_name";
        public static final String COLUMN_ROUTE_COLOUR = "route_colour";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_WALL = "wall";
        public static final String COLUMN_NOTE = "note";
    }
}
