package com.example.qrhunter;

import android.provider.Settings;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * a test user for intent and unit tests that initializes the user with two codes
 */
public class TestUser extends User{
    private String deviceID;
    public TestUser(String username, String email,boolean owner) {
        super(username, email);
        setOwner(owner);
        setup(username);


    }
    public TestUser(String username, String email) {
        super(username, email);
        setup(username);
    }
    public void setup(String username){
        //Testing Purposes only
        QRCode code1 = new QRCode("BFG5DGW54",1);
        QRCode code2 = new QRCode("DCFJFJFJ", 2);
        addCode(code1);
        addCode(code2);
        deviceID = Settings.Secure.getString(MainActivity.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DataManagement manager = new DataManagement(this, db);
        manager.updateData();
        manager.addCode(code1, new CallBack() {
            @Override
            public void onCall(User user) {
                return;
            }
        });
        manager.addCode(code2, new CallBack() {
            @Override
            public void onCall(User user) {
                return;
            }
        });
//        manager.addToQRDoc(code1);
//        manager.addToQRDoc(code2);
        //Adding Dummy Device ID
        HashMap<String, String> ID = new HashMap<>();
        ID.put("User Name", username);
        ID.put("ID", deviceID);
        db.collection("ID's").document(deviceID)
                .set(ID);//No onSuccess or onFailure Listeners

    }
}
