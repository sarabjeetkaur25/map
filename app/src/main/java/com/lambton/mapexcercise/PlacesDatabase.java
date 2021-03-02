package com.lambton.mapexcercise;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { FavPlaces.class}, version = 1)
public abstract class PlacesDatabase extends RoomDatabase {
    public abstract PlaceDeo getPlaceDao();
    private static PlacesDatabase placeDB;
    public static PlacesDatabase getInstance(Context context) {
        if (null == placeDB) {
            placeDB = buildDatabaseInstance(context);
        }
        return placeDB;
    }
    private static PlacesDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                PlacesDatabase.class,
                "PlaceDB")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        placeDB = null;
    }

}
