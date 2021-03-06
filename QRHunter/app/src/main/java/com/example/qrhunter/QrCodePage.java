package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

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
    private ArrayAdapter<String> commentsAdapter;
    private ListView commentsListView;
    private EditText addCommentEditText;
    private User currentUser;



    /**
     * called when activity created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        qrCode = (QRCode) intent.getSerializableExtra("QRCode");
        currentUser = (User) intent.getSerializableExtra("currentUser");//User that would be leaving comments
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        codeName = findViewById(R.id.code_name_text);
        codeName.setText("QR Code#" + qrCode.getID());

        scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: " + qrCode.getScore());

        //Setting Up The ListView and comment EditText
        addCommentEditText = findViewById(R.id.comment_edit_text);
        commentsListView = findViewById(R.id.comments_listview);
        getComments();

        locationImage = findViewById(R.id.image_location);

        Bitmap bitmap = LocationImage.decodeImage(qrCode.getImage());
        if(bitmap==null){
            locationImage.setImageResource(android.R.color.transparent);
        }else{
            locationImage.setImageBitmap(bitmap);
            locationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ImageDialogFragment(bitmap).show(getSupportFragmentManager(), "image");
                }
            });
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
     * Gets the comments on a QRCode from firebase and sets the listview
     */
    public void getComments(){
        dataManager.retrieveComments(qrCode ,new CommentCall() {
            @Override
            public void onCall(List<String> comments) {
                if (comments == null){
                    comments = new ArrayList<>();
                }
                commentsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.comment_list,comments);
                commentsListView.setAdapter(commentsAdapter);
            }
        });
    }

    /**
     * Called when the add comment button is clicked, only adds if there is nothing else there
     * @param view
     *      View for the add comment button
     */
    public void addComment(View view){
        hideKeyboard(view);
        if (addCommentEditText.getText().toString() != ""){
            //Gets and builds comment, then clears EditText
            String comment = currentUser.getUsername() + ": " + addCommentEditText.getText().toString();
            addCommentEditText.getText().clear();

            dataManager.updateCodeComment(qrCode, comment);
            getComments();
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

    /**
     * go to the social page of the QR code
     * @param view
     *      the current view
     */
    public void onSocialClick(View view){
        Intent intent =new Intent(this, QRSocialPage.class);
        intent.putExtra("User",currentUser);
        intent.putExtra("QRCode",qrCode);
        startActivity(intent);
    }

    /**
     * hides the keyboard after input received
     * @param view
     *      the current view
     */
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
    }

    /**
     * allows the user to utilize the back button
     * @param item
     *      the button pressed on the action bar
     * @return
     *      if it is successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.putExtra("User",currentUser);
                startActivity(intent);
                return true;
        }
        return true;
    }
}