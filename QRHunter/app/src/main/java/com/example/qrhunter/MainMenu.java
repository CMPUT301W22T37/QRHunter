package com.example.qrhunter;

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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
/**
 * The main menu activity. This is page the user sees first when they are signed in
 */
public class MainMenu extends AppCompatActivity {
    private ArrayAdapter<String> codesAdapter;
    private ListView codesListView;
    private User user;
    private DataManagement dataManager;
    private ArrayList<String> codesDisplay;
    private TextView totalScore;
    private TextView totalScanned;


    /**
     * called when creating the activity
     * @param savedInstanceState
     *      the instance bundle
     */
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
        totalScore.setText("Total Score: "+ Integer.toString(user.getTotalScore()));

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
            }
        });


    }

    /**
     * sets the user of the main menu
     * @param user
     *      the user to be changed to
     */
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

    /**
     * To be completed
     * @param view
     */
    public void onScan(View view){
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}