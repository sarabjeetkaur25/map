package com.lambton.mapexcercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button addNewPlace;
    fvAdapter madapter;
    List<FavPlaces> placesList;
    PlaceDeo db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        addNewPlace = findViewById(R.id.addNewPlace);
        db = PlacesDatabase.getInstance(MainActivity.this).getPlaceDao();
        placesList = db.getAll();
        addNewPlace.setOnClickListener(v->{
            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
        });
        madapter = new fvAdapter(this,placesList) {
            @Override
            public void deleteAddress(int i) {
                PlacesDatabase.getInstance(MainActivity.this).getPlaceDao().delete(placesList.get(i));
                placesList.remove(i);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onClick(int i) {
                Intent x = new Intent(getApplicationContext(),MapsActivity.class);
                x.putExtra("selected",i);
                startActivity(x);
                Toast.makeText(getApplicationContext(),placesList.get(i).getLocation(),Toast.LENGTH_SHORT).show();
            }
        };
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(madapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        madapter.places = db.getAll();
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}