package com.example.qrhunter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Checks the permissions necessary for the application
 * references: https://developer.android.com/training/location/retrieve-current
 */
public class PermissionChecker extends AppCompatActivity {
    private Context context;
    private static final int LOCATION_COURSE_CODE = 100;
    private static final int LOCATION_FINE_CODE = 101;
    private static final int CAMERA_CODE = 102;

    /**
     * Constructor
     * @param context
     *      the context of the calling application
     */
    public PermissionChecker(Context context){
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_FINE_CODE);
            checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_COURSE_CODE);

            Log.d("DEBUG", "PERMISSION PROBLEMS");

        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            checkLocationPermission(Manifest.permission.CAMERA, CAMERA_CODE);
            Log.d("DEBUG", "PERMISSION PROBLEMS");

        }
    }

    /**
     * requests permissions from the user
     * @param permission
     *      the current permission provided by the user
     * @param requestCode
     *      the code for the permission being sought after
     */
    public void checkLocationPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions((Activity) context, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Acquire permission from user
     * @param requestCode
     *      the code of the requested permission
     * @param permissions
     *      permissions of the user
     * @param grantResults
     *      the results that have been granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("DEBUG","request code: "+requestCode);
        if (requestCode == LOCATION_COURSE_CODE || requestCode == LOCATION_FINE_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(context, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
