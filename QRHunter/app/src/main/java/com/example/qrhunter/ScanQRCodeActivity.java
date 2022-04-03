package com.example.qrhunter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

/**
 * activity to scan a QR code
 */
public class ScanQRCodeActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private User user;
    private DataManagement dataManager;
    /**
     * called when activity created
     * @param savedInstanceState
     *      the instance bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            new PermissionChecker(ScanQRCodeActivity.this);
        }
        setContentView(R.layout.activity_scan_qr_code);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("QRHunter");
        actionBar.setDisplayHomeAsUpEnabled(true);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final QRCode qrCode = new QRCode(result.getText(),user.getNextID());
                        final User oldUser = user;
                        try{
                            dataManager.addCode(qrCode, new CallBack() {
                                @Override
                                public void onCall(User user) {
                                    Context context = getApplicationContext();
                                   if(user==null){
                                       Toast.makeText(ScanQRCodeActivity.this, "Error: already scanned this code", Toast.LENGTH_LONG).show();
                                       Intent intent =new Intent(context,MainMenu.class);
                                       intent.putExtra("User",oldUser);
                                       startActivity(intent);
                                   }else {
                                       Intent intent = new Intent(context, ScanConfirmationPage.class);
                                       intent.putExtra("User", user);
                                       intent.putExtra("QRCode", qrCode);
                                       startActivity(intent);
                                   }
                                }
                            });

                        } catch(Exception e){
                            Log.d("TAG", "QR DNE");
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * allows users to go back to the main menu
     * @param item
     *      the item on the action bar
     * @return
     *      success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                intent.putExtra("User",user);
                startActivity(intent);
                return true;
        }
        return true;
    }
}
