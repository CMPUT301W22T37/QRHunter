package com.example.qrhunter;

import android.location.Location;
import android.location.LocationListener;

public class FineLocationListener implements LocationListener {
    private Location location;
    @Override
    public void onLocationChanged(Location loc) {

        location = loc;

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    public Location getLocation(){
        return location;
    }



}
