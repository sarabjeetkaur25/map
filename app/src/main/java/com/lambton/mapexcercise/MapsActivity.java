package com.lambton.mapexcercise;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private Marker homeMarker;
    Location userLocation;
    PlaceDeo db;
    boolean markerIsSet = false;
    FavPlaces selectedPlace;
    boolean fromList = false;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        db = PlacesDatabase.getInstance(MapsActivity.this).getPlaceDao();
        mapFragment.getMapAsync(this);
        int Id = getIntent().getIntExtra("selected",-1);
        if(Id != -1){
            fromList = true;
        }
        else{
            fromList = false;
        }

    }

      @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       locationListener = location -> {
            userLocation = location;
            setHomeMarker(location);

        };

        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();

        mMap.setOnMapLongClickListener(latLng -> {
            if(!fromList){
                setMarker(latLng);
            }
        });

    }
    /*private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    private void setHomeMarker(Location location) {
        int Id = getIntent().getIntExtra("selected",-1);
        if(Id != -1 && !markerIsSet){
            fromList = true;
            selectedPlace = db.getAll().get(Id);
            LatLng latLng = new LatLng(selectedPlace.getLati(),selectedPlace.getLongi());
            setMarker(latLng);
            markerIsSet = true;

        }
        if (homeMarker != null){
            homeMarker.remove();
        }

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Your Location");
        if(!fromList){
            homeMarker = mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
        }

    }

    private void setMarker(LatLng latLng) {
        float[] distance = new float[1];
        Location.distanceBetween(latLng.latitude,latLng.longitude,userLocation.getLatitude(),userLocation.getLongitude(),distance);
        double dis = Math.round((distance[0]/1000) * 100.0) / 100.0;
        MarkerOptions options = new MarkerOptions().position(latLng)
                .title("Distance from user location = "+dis+" km")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .draggable(true);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        if(!fromList){
            saveToDB(latLng);
        }/*

    }

    private void saveToDB(LatLng latLng) {

        Toast.makeText(this, getAddress(latLng),
                Toast.LENGTH_LONG).show();
        FavPlaces place = new FavPlaces(latLng.latitude,latLng.longitude,new Date().getTime(),getAddress(latLng));
        db.insert(place);
    }
    String getAddress(LatLng latLng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
          return address + ", "+ city+", "+state+", Postal Code :- "+postalCode;
        } catch (IOException e) {
          return new Date().toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, getAddress(marker.getPosition()),
                Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(fromList){
            selectedPlace.setLati(marker.getPosition().latitude);
            selectedPlace.setLongi(marker.getPosition().longitude);
            selectedPlace.setLocation(getAddress(marker.getPosition()));
            db.update(selectedPlace);
        }

    }
}