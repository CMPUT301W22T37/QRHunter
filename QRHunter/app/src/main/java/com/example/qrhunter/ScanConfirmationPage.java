package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Page to confirm scan and choose location and photo options
 */
public class ScanConfirmationPage extends AppCompatActivity {
    private User user;
    private QRCode qrCode;
    private TextView scoreText;
    private FusedLocationProviderClient fusedLocationClient;
    private Location geolocation;
    private Bitmap bitmap;


    /**
     * called when activity created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_confirmation_page);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        qrCode = (QRCode) intent.getSerializableExtra("QRCode");


        int score = qrCode.getScore();
        scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: " + score);


    }

    /**
     * obtains the location of the QR code
     */
    public void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new PermissionChecker(ScanConfirmationPage.this);
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
                geolocation = location;
            }
        }
    }

    /**
     * Sets the image of the code
     * @param bitmap
     *      the bitmap representation of an image
     */
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    /**
     * To be called when camera is selected
     * @param view
     *      the current view
     */
    public void onCamera(View view){
        CameraFragment camFrag = new CameraFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, camFrag).commit();


    }

    /**
     * Saves the result of the camera (if any) to the QRCode on the database
     */
    public void getCameraResult(){
        if(bitmap == null){
            Log.d("DEBUG","no image selected");
            return;
        }
        String imageB64 = LocationImage.encodeImage(bitmap);
        if(imageB64==null){
            return; //error
        }

        int index = user.getAllHashes().indexOf(qrCode.getUniqueHash());
        qrCode.setImage(imageB64);
        user.updateCode(index,qrCode);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DataManagement dataManager = new DataManagement(user,db);
        dataManager.updateData();
        return;


    }

    /**
     * Actions to be completed upon confirming code
     * @param view
     *      the current view
     */
    public void OnConfirm(View view){
        boolean isChecked = false;
        Switch geoLocationSwitch = (Switch) findViewById(R.id.recordGeolocation);
        if (geoLocationSwitch != null) {
            isChecked = geoLocationSwitch.isChecked();
        }

        if(isChecked){
            getLocation();
            if(geolocation!=null) { //if this is null the phone needs to acknowledge some kind of gps at some point like opening maps
                Log.d("DEBUG", "Latitude:" + geolocation.getLatitude() + ", Longitude:" + geolocation.getLongitude());
                int index = user.getAllHashes().indexOf(qrCode.getUniqueHash());
                qrCode.setGeolocation(geolocation.getLatitude(),geolocation.getLongitude());
                user.updateCode(index,qrCode);
                Log.d("DEBUG","lat: "+ user.getCode(index).getLatitude() + " lon: " + user.getCode(index).getLongitude());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DataManagement dataManager = new DataManagement(user,db);
                dataManager.updateData();
                dataManager.updateQR(qrCode); //only update if location has actually changed
                Log.d("DEBUG","checked");
            }

        }else{

            Log.d("DEBUG","not checked");
//
        }
        getCameraResult();

        Intent intent =new Intent(this, MainMenu.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }

    /**
     * allows a scan to be canceled
     * @param view
     *      the current view
     */
    public void onCancel(View view){
        user.removeQRCode(qrCode);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DataManagement dataManager = new DataManagement(user,db);
        dataManager.updateData();
        dataManager.removeFromQRDoc(qrCode);
        Intent intent =new Intent(this, MainMenu.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }



}

