package com.example.boulderjournal.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "route")
public class RouteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mRouteName;
    private String mRouteColour;
    private String mRoom;
    private String mWall;
    private String mNote;
    @ColumnInfo(name ="updated_at")
    private Date updatedAt;


    @Ignore
    public RouteEntry(String routeName, String routeColour, String room, String wall, String note , Date updatedAt){
        this.mRouteName = routeName;
        this.mRouteColour = routeColour;
        this.mRoom = room;
        this.mWall = wall;
        this.mNote = note;
        this.updatedAt = updatedAt;
    }

    public RouteEntry(int id, String routeName, String routeColour, String room, String wall, String note , Date updatedAt){
        this.id = id;
        this.mRouteName = routeName;
        this.mRouteColour = routeColour;
        this.mRoom = room;
        this.mWall = wall;
        this.mNote = note;
        this.updatedAt = updatedAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteName() {
        return mRouteName;
    }

    public void setRouteName(String routeName) {
        this.mRouteName = routeName;
    }

    public String getRouteColour() {
        return mRouteColour;
    }

    public void setRouteColour(String routeColour) {
        this.mRouteColour = routeColour;
    }

    public String getRoom(){
        return mRoom;
    }

    public void setRoom(String room){
        this.mRoom = room;
    }

    public String getWall() {
        return mWall;
    }

    public void setWall(String wall) {
        this.mWall = wall;
    }

    public String getNote(){
        return mNote;
    }

    public void setNote(String note){
        this.mNote = note;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}
