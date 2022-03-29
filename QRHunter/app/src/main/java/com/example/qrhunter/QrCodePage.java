package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * the activity called when a QR code on the user page is selected
 */
public class QrCodePage extends AppCompatActivity implements OnMapReadyCallback {
    private QRCode qrCode;
    private User user;
    private DataManagement dataManager;
    private TextView codeName;
    private TextView scoreText;
    private ImageView locationImage;
    private MapView mapView;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;



    /**
     * called when activity created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_page);
        Intent intent = getIntent();
        qrCode = (QRCode) intent.getSerializableExtra("QRCode");
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        codeName = findViewById(R.id.code_name_text);

        codeName.setText("QR Code#" + qrCode.getID());



        scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: "+qrCode.getScore());

        locationImage = findViewById(R.id.image_location);

        Bitmap bitmap = LocationImage.decodeImage(qrCode.getImage());
        if(bitmap==null){
            locationImage.setImageResource(android.R.color.transparent);
        }else{
            locationImage.setImageBitmap(bitmap);
        }
        latitude = qrCode.getLatitude();
        longitude = qrCode.getLongitude();
        mapView = findViewById(R.id.locationMap);
        if(latitude!=0 && longitude!=0){
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            mapView.getMapAsync(this);

        }



    }

    /**
     * Called when delete button hit, used to delete QR code from user's list
     * @param view
     *      the current view of the page
     */
    public void onDelete(View view){
        try{
            final User oldUser = this.user;
            dataManager.removeCode(qrCode, new CallBack() {
                @Override
                public void onCall(User user) {
                    Log.d("TAG", "Delete QR Code"+ qrCode.getID());

                    Context context = getApplicationContext();
                    if(user==null){
                        user = oldUser;
                    }
                    Intent intent =new Intent(context, MainMenu.class);
                    intent.putExtra("User",user);
                    startActivity(intent);
                }
            });

        } catch(Exception e){
            Log.d("TAG", "QR DNE");
        }
    }

    /**
     * sets the position of the map
     * @param googleMap
     *      the map we are using
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(googleMap==null){
            Log.d("GPS","google map is null");
            return;
        }else{
            Log.d("GPS","google not null");
        }
        this.mMap = googleMap;
        Log.d("GPS","map found");
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new PermissionChecker(QrCodePage.this);
        }
        LatLng position = new LatLng(this.latitude, this.longitude);
        mMap.addMarker(new MarkerOptions().position(position));

        MapsInitializer.initialize(this);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15);
        mMap.animateCamera(cameraUpdate);
    }
}