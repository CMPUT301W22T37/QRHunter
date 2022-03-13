package com.example.qrhunter;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

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
                .whereArrayContains("QRIdentifiers",qrCode.getUniqueHash())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        Context context = getApplicationContext();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", "Delete QR Code"+ qrCodeFinal.getID());

                                user.removeQRCode(qrCodeFinal);
                                Log.d("TAG", "In data management: length is "+ user.getAllCodes().size());
                                updateData();
                                myCallFinal.onCall(user);
                                return;
                            }
                            Log.d("TAG", "Failed: QR not present");
                            ArrayList<String> allcodes = user.getCodesStrings();
                            for(int i = 0;i<allcodes.size();i++){
                                Log.d("TAG", "QRCode: "+ allcodes.get(i));
                            }
//                            myCallFinal.onCall(null); //if DNE

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
                .whereArrayContains("QRIdentifiers",qrCode.getUniqueHash())

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Manually checking if QRCode already exists
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                if (compareQRCodes((ArrayList<HashMap>)data.get("QRCodes"))){
                                    myCallFinal.onCall(null); //null if already exists
                                    return; //already exists
                                }
                            }
                            user.addCode(qrCodeFinal);
                            updateData();
                            myCallFinal.onCall(user);
                        }
                    }
                });
    }


    /**
     * Returns whether the given codeMaps contains a hash that is also present with the user
     * @param codeMaps
     *      HashMap Arraylist containing the HashMaps that are being searched
     * @return
     *      Boolean whether or not a code is shared between the codeMaps and the user
     */
    public boolean compareQRCodes(ArrayList<HashMap> codeMaps){
        ArrayList<String> userHashes = user.getAllHashes();
        for (HashMap code:codeMaps) {
            String hash = (String)code.get("hash");
            if (userHashes.contains(hash)){
                return false;
            }
        }
        return true;
    }

    /**
     * updates the user on the database
     */
    public void updateData(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("User Name", user.getUsername());
        data.put("Email", user.getEmail());
        data.put("QRCodes", user.getAllCodes());
        data.put("QRIdentifiers", user.getAllHashes());

        userRef
                .document(user.getUsername())
                .set(data);
    }



}
