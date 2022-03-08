package com.example.qrhunter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class DataManagement  {
    private User user;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    public DataManagement(User user, FirebaseFirestore fireStore){
        this.user = user;
        this.db = fireStore;
        this.userRef = db.collection("Users");
    }
    public void removeCode(QRCode qrCode, CallBack myCall){
        final QRCode qrCodeFinal = qrCode;
        final CallBack myCallFinal = myCall;
        db.collection("Users")
                .whereEqualTo("User Name", user.getUsername())
                .whereArrayContains("QRCodes",qrCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Context context = getApplicationContext();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user.removeQRCode(qrCodeFinal);
                                updateData();
                                myCallFinal.onCall(user);
                                return;
                            }
                            myCallFinal.onCall(null); //if DNE

                        }
                    }
                });
    }
    public void addCode(QRCode qrCode, CallBack myCall){
        final QRCode qrCodeFinal = qrCode;
        final CallBack myCallFinal = myCall;
        db.collection("Users")
                .whereEqualTo("User Name", user.getUsername())
                .whereArrayContains("QRCodes",qrCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Context context = getApplicationContext();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                myCallFinal.onCall(null); //null if already exists
                                return; //already exists

                            }
                            user.addCode(qrCodeFinal);
                            updateData();
                            myCallFinal.onCall(user);

                        }
                    }
                });
    }


    public void updateData(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("User Name", user.getUsername());
        data.put("Email", user.getEmail());
        data.put("QRCodes", user.getAllCodes());
        userRef
                .document(user.getUsername())
                .set(data);
    }



}
