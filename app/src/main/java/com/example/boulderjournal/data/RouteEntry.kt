package com.example.boulderjournal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import java.util.Date

@Entity(tableName = "route")
data class RouteEntry(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "route_name")
    var routeName: String? = "Default",

    @ColumnInfo(name = "route_colour")
    var routeColour: String? = "PINK",

    @ColumnInfo(name = "room")
    var room: String? = "Default",

    @ColumnInfo(name = "wall")
    var wall: String? = "Default",

    @ColumnInfo(name = "note")
    var note: String? = "Default",

    @ColumnInfo(name = "image_location")
    var mImageLocation: String?,

    @ColumnInfo(name = "complete")
    var mComplete: String?,

    @ColumnInfo(name = "updated_at")
    var updatedAt: Date? = null
){

    fun setmComplete(state : String?){
        mComplete = state;
    }
}
