package com.example.qrhunter;

import android.location.Location;
import android.util.Log;

import java.util.Comparator;

/**
 * a comparator to sort QR codes by their distance to the current location of the user
 */
public class SortByDistanceComparator implements Comparator<QRCode> {
    private Location currentLocation;
    public SortByDistanceComparator(Location location){
        this.currentLocation = location;
    }
    @Override
    public int compare(QRCode code1, QRCode code2) {
        double dist1 = getDistance(code1.getLatitude(),code1.getLongitude(), currentLocation);
        double dist2 = getDistance(code2.getLatitude(),code2.getLongitude(), currentLocation);
        if (dist1 < dist2){
            return -1;
        }
        else if (dist1 > dist2){
            return 1;
        }
        else{
            return 0;
        }
    }

    /**
     * gets the distance between the current location and a point. Adapted from
     * https://www.geodatasource.com/developers/java
     * @param lat
     *      the latitude of the point
     * @param lon
     *      the longitude of the point
     * @return
     *      double, the distance between the current location and the point.
     */
    public static double getDistance(double lat, double lon,Location currentLocation){

        double theta = currentLocation.getLongitude() - lon;
        double dist = Math.sin(Math.toRadians(currentLocation.getLatitude())) * Math.sin(Math.toRadians(lat))
                + Math.cos(Math.toRadians(currentLocation.getLatitude())) * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;
        Log.d("GPS", "cur lat: "+currentLocation.getLatitude()+" cur lon: "+ currentLocation.getLongitude()+ " pt lat: "+lat + "pt lon"+lon);
        return dist;

    }
}
