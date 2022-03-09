package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainMenu extends AppCompatActivity {
    private ArrayAdapter<String> codesAdapter;
    private ListView codesListView;
    private User user;
    private DataManagement dataManager;
    private ArrayList<String> codesDisplay;
    private TextView totalScore;
    private TextView totalScanned;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Getting user
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dataManager = new DataManagement(user,db);


        codesListView = findViewById(R.id.QRCode_List_View);
        totalScanned = findViewById(R.id.num_scanned_text);
        totalScore = findViewById(R.id.total_score_text);
        totalScanned.setText("Codes Scanned: " + Integer.toString(user.getAllCodes().size()));
        totalScore.setText("Total Score: "+Integer.toString(user.getTotalScore()));


        //Creating Listview for rolls
        codesDisplay = user.getCodesStrings();
        codesAdapter = new ArrayAdapter<String>(this, R.layout.qr_list, codesDisplay);
        codesListView.setAdapter(codesAdapter);

        codesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int i, long l) {
                String selected =(String) (codesListView.getItemAtPosition(i));
                QRCode qrCode = user.getCode(user.getCodesStrings().indexOf(selected));
                Context context = getApplicationContext();
                Intent intent =new Intent(context, QrCodePage.class);
                intent.putExtra("QRCode",qrCode);
                intent.putExtra("User",user);
                startActivity(intent);
//                try{
//                    QRCode qrCode = user.getCode(user.getCodesStrings().indexOf(selected));
//
////                dataManager.removeCode(qrCode);
//                    dataManager.removeCode(qrCode, new CallBack() {
//                        @Override
//                        public void onCall(User user) {
//                            Log.d("TAG", "length of qr = "+Integer.toString(user.getCodesStrings().size()));
//                            int duration = Toast.LENGTH_LONG;
//                            Context context = getApplicationContext();
//                            Toast toast = Toast.makeText(context,"length of qr = "+Integer.toString(user.getCodesStrings().size()),duration);
//                            toast.show();
//                            setUser(user);
//                        }
//                    });
//                    codesAdapter.notifyDataSetChanged();
//                } catch(Exception e){
//                    Log.d("TAG", "QR DNE");
//                }
            }
        });


    }
    public void setUser(User user){
        if(user==null){
            Log.d("TAG", "User DNE");
            return;
        }
        this.user = user;

        this.codesDisplay = this.user.getCodesStrings();
        this.codesListView.setAdapter(this.codesAdapter);
        codesAdapter.notifyDataSetChanged();
        Log.d("TAG", "New user length"+Integer.toString(user.getCodesStrings().size()));
    }

    public void onScan(View view){
        startActivity(new Intent(this,ScanQRCodeActivity.class));
    }
}