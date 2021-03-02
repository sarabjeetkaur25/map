package com.lambton.mapexcercise;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class FavPlaces {
        @PrimaryKey(autoGenerate = true)
        private int place_id;
        private double lati;
        private double longi;
        private long date_added;
        private String location;

    public FavPlaces(double lati, double longi, long date_added, String location) {
        this.lati = lati;
        this.longi = longi;
        this.date_added = date_added;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }
}
