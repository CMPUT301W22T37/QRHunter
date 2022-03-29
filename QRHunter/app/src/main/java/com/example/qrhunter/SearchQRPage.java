package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class SearchQRPage extends AppCompatActivity {
    private User user;
    private DataManagement dataManager;
    private Location currentLocation;
    private ListView nearbyList;
    private ArrayList<String> codesDisplay;
    private ArrayAdapter<String> codesAdapter;


    public static double MAX_DISTANCE = 20000;
    private ArrayList<QRCode> qrCodeLocations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qrpage);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        getCurrentLocation();
        getQRCodes();

    }

    public void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new PermissionChecker(SearchQRPage.this);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new FineLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        Log.d("GPS", "GPS Enabled");
        Location location;
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.d("GPS", "Location found");
                Log.d("GPS", "Lat: "+location.getLatitude()+" lon: "+location.getLongitude());
                currentLocation = location;

            }
        }
    }

    private void getQRCodes(){

        dataManager.getCodes(new CodeCall() {
            @Override
            public void onCall(ArrayList<QRCode> qrCodes) {
                Context context = getApplicationContext();

                for(QRCode qr: qrCodes){
                    if(qr.getLatitude()!=0 && qr.getLongitude()!=0){
                        Log.d("SEARCH","QR: "+qr.getScore());
                        qrCodeLocations.add(qr);

                    }
                }
                Collections.sort(qrCodeLocations, new SortByDistanceComparator(currentLocation));
                Log.d("SEARCH","Size: "+qrCodeLocations.size());
                setupListView();

            }
        });

    }
    public void setupListView(){
        codesDisplay = getDistStrings(qrCodeLocations);
        nearbyList = findViewById(R.id.Nearby_List_View);
        codesAdapter = new ArrayAdapter<String>(this, R.layout.distance_list, codesDisplay);
        nearbyList.setAdapter(codesAdapter);
    }
    private ArrayList<String> getDistStrings(ArrayList<QRCode> sortedQR){
        double dist;
        ArrayList<String> codeStrings = new ArrayList<>();
        if(sortedQR==null || sortedQR.size()==0){
            return codeStrings;
        }
        for(QRCode qr: sortedQR){
            dist = SortByDistanceComparator.getDistance(qr.getLatitude(),qr.getLongitude(),currentLocation);
            Log.d("SEARCH","QR: "+qr.getScore() + " Dist: "+dist);
            if(dist<=MAX_DISTANCE){
                codeStrings.add("Score: "+ qr.getScore()+ " Distance: "+Math.round(dist*100.0)/100.0 + "m"); // multiply and divide to ensure if <1m it doesn't round to 0
            }
        }
        return codeStrings;
    }




}