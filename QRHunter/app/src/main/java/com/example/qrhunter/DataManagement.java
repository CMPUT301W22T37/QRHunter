package com.example.qrhunter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

/**
 * This class is the interface between the database and the app
 */
public class DataManagement  {
    private User user;
    private FirebaseFirestore db;
    private CollectionReference userRef;


    /**
     * The constructor for the class
     * @param user
     *      the current user of the app
     * @param fireStore
     *      the Firestore database to reference
     */
    public DataManagement(User user, FirebaseFirestore fireStore){
        this.user = user;
        this.db = fireStore;
        this.userRef = db.collection("Users");
    }

    /**
     * This removes a qr code from the database
     * @param qrCode
     *      the qrCode to be removed
     * @param myCall
     *      a reference to an interface that will allow the calling function to update the user
     */
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
                                Log.d("TAG", "Delete QR Code"+ qrCodeFinal.getCode());
                                user.removeQRCode(qrCodeFinal);
                                Log.d("TAG", "In data management: length is "+ user.getAllCodes().size());
                                updateData();
                                myCallFinal.onCall(user);
                                return;
                            }
                            Log.d("TAG", "Failed: QR not present");
                            myCallFinal.onCall(null); //if DNE

                        }
                    }
                });
    }

    /**
     * Add a qr code to the database
     * @param qrCode
     *      the code to be added
     * @param myCall
     *      a reference to an interface that will allow the calling function to update the user
     */
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

    /**
     * updates the user on the database
     */
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
