package com.example.qrhunter;

import java.io.Serializable;

public class Geolocation implements Serializable {
    private double latitude;
    private double longitude;
    public Geolocation(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }
    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
