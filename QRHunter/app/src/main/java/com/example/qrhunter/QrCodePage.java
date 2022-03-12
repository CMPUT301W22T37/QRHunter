package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * the activity called when a QR code on the user page is selected
 */
public class QrCodePage extends AppCompatActivity {
    private QRCode qrCode;
    private User user;
    private DataManagement dataManager;
    private TextView codeName;

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
        codeName.setText(qrCode.getCode());
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
                    Log.d("TAG", "Delete QR Code"+ qrCode.getCode());

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context,"length of qr = "+Integer.toString(user.getCodesStrings().size()),duration);
                    toast.show();
                    if(user==null){
                        Log.d("DEBUG","user is null in QrCodePage");
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
}