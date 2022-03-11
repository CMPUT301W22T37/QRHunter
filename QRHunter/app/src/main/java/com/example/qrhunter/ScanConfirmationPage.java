package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class ScanConfirmationPage extends AppCompatActivity {
    private User user;
    private DataManagement dataManager;
    private QRCode qrCode;
    private TextView scoreText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_confirmation_page);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("User");
        qrCode = (QRCode)intent.getSerializableExtra("QRCode");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);

        int score = qrCode.getScore();
        scoreText = findViewById(R.id.score_text);
        scoreText.setText("Score: "+score);
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
        Intent intent =new Intent(this, MainMenu.class);
        intent.putExtra("User",user);
        startActivity(intent);
    }
}