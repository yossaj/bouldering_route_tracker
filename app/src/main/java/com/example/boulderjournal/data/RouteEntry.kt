package com.example.boulderjournal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import java.util.Date

@Entity(tableName = "route")
class RouteEntry(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "routeName")
    var routeName: String? = null,
    @ColumnInfo(name = "routeColour")
    var routeColour: String? = null,
    @ColumnInfo(name = "room")
    var room: String? = null,
    @ColumnInfo(name = "wall")
    var wall: String? = null,
    @ColumnInfo(name = "note")
    var note: String? = null,
    var mImageLocation: String?,
    @ColumnInfo(name = "complete")
    var mComplete: String?,
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date? = null
){

    fun setmComplete(state : String?){
        mComplete = state;
    }

    fun setmImageLocation(location: String?){
        mImageLocation = location
    }

    fun getmImageLocation() : String?{
        return mImageLocation
    }
}
