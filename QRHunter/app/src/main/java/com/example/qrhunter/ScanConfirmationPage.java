package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScanConfirmationPage extends AppCompatActivity {
    private User user;
    private QRCode qrCode;
    private TextView scoreText;
    private FusedLocationProviderClient fusedLocationClient;
    private Location geolocation;
    private static final int LOCATION_COURSE_CODE = 100;
    private static final int LOCATION_FINE_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_confirmation_page);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        qrCode = (QRCode) intent.getSerializableExtra("QRCode");


        int score = qrCode.getScore();
        scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: " + score);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_COURSE_CODE);
            checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_FINE_CODE);
            Log.d("DEBUG", "PERMISSION PROBLEMS");
            return;
        }
//
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d("DEBUG", "location is not null");
                    geolocation = location;
//                            geolocation  = new Geolocation(location.getLatitude(),location.getLongitude());
                } else {
                    Log.d("DEBUG", "location is null");
                }
            }
        });

    }



//    protected void createLocationRequest() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }

//    private Location getKnownLocation() {
//        createLocationRequest();
//        Criteria crit = new Criteria();
//        List<String> providers = locationManager.getProviders(crit,true);
//        Location bestLocation = null;
//        Log.d("DEBUG", "Providers: " + providers.size());
//
//
//            for (String provider : providers) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_COURSE_CODE);
//                    checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_FINE_CODE);
//                    Log.d("DEBUG", "PERMISSION PROBLEMS");
//                }
//                Location l = locationManager.getLastKnownLocation(provider);
//                Log.d("DEBUG","last known location, provider: "+provider +", location: "+l);
//
//                if (l == null) {
//                    continue;
//                }
//                if (bestLocation == null
//                        || l.getAccuracy() < bestLocation.getAccuracy()) {
//                    Log.d("DEBUG","found best last known location:" + l);
//                    bestLocation = l;
//                }
//            }
//
//
//        if (bestLocation == null) {
//            return null;
//        }
//        return bestLocation;
//    }
    public void checkLocationPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(ScanConfirmationPage.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(ScanConfirmationPage.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(ScanConfirmationPage.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("DEBUG","request code: "+requestCode);
        if (requestCode == LOCATION_COURSE_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(ScanConfirmationPage.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ScanConfirmationPage.this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onCamera(View view){
        CameraFragment camFrag = new CameraFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, camFrag).commit();
        Bitmap bitmap = camFrag.getImage();
        int index = user.getCodesStrings().indexOf(qrCode.getCode());
//        qrCode.setImg(bitmap);
        user.updateCode(index,qrCode);
    }

    public void OnConfirm(View view){
        boolean isChecked = false;
//        geolocation = getKnownLocation();
        if(geolocation!=null) { //if this is null the phone needs to acknowledge some kind of gps at some point like opening maps
            Log.d("DEBUG", "Latitude:" + geolocation.getLatitude() + ", Longitude:" + geolocation.getLongitude());
            Switch geoLocationSwitch = (Switch) findViewById(R.id.recordGeolocation);

            if (geoLocationSwitch != null) {
                isChecked = geoLocationSwitch.isChecked();
            }
        }

        if(isChecked){
            int index = user.getCodesStrings().indexOf(qrCode.getCode());
            qrCode.setGeolocation(geolocation.getLatitude(),geolocation.getLongitude());
            user.updateCode(index,qrCode);
            Log.d("DEBUG","lat: "+ user.getCode(index).getLatitude() + " lon: " + user.getCode(index).getLongitude());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DataManagement dataManager = new DataManagement(user,db);
            dataManager.updateData();
            Log.d("DEBUG","checked");
        }else{

            Log.d("DEBUG","not checked");
//
        }

        Intent intent =new Intent(this, MainMenu.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }



}

