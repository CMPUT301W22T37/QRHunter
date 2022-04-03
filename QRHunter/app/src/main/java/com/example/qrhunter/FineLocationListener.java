package com.example.qrhunter;

import android.location.Location;
import android.location.LocationListener;

/**
 * a listener to obtain the fine location of the device
 */
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

    /**
     * returns current location
     * @return
     */
    public Location getLocation(){
        return location;
    }



}
