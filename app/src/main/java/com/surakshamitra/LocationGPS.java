package com.surakshamitra;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LocationGPS extends Service implements LocationListener {

    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdates();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    private void requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, // interval in milliseconds
                    10,   // minimum distance in meters
                    this
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Save the location to your local database (SQLite or Room)
        saveLocationToDatabase(location);
    }

    private void saveLocationToDatabase(Location location) {
        // Implement database insertion logic here
        // You can use SQLiteOpenHelper, Room, or any other database library
        // Example using Room:
        // MyDatabase.getInstance(this).locationDao().insertLocation(new LocationEntity(location.getLatitude(), location.getLongitude()));
    }

    // Implement other LocationListener methods if needed
}
