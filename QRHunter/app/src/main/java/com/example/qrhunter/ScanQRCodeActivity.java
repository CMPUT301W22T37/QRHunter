package com.example.qrhunter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

public class ScanQRCodeActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private User user;
    private DataManagement dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);
        setContentView(R.layout.activity_scan_qr_code);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanQRCodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        final QRCode qrCode = new QRCode(result.getText());

                        try{

                            dataManager.addCode(qrCode, new CallBack() {
                                @Override
                                public void onCall(User user) {
                                    Log.d("TAG", "Delete QR Code"+ qrCode.getCode());

                                    Context context = getApplicationContext();

                                    Intent intent =new Intent(context, ScanConfirmationPage.class);
                                    intent.putExtra("User",user);
                                    intent.putExtra("QRCode",qrCode);
                                    startActivity(intent);
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
}
