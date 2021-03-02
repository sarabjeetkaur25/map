package com.lambton.mapexcercise;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDeo {
    @Query("SELECT * FROM FavPlaces WHERE :placeID")
    List<FavPlaces> getPlace(int placeID);

    @Query("SELECT * FROM FavPlaces ")
    List<FavPlaces> getAll();
    @Insert
    void insert(FavPlaces places);

    @Update
    void update(FavPlaces repos);

    @Delete
    void delete(FavPlaces place);

    @Delete
    void delete(FavPlaces... places);

}
