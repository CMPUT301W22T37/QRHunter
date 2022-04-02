package com.example.qrhunter;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", "Delete QR Code"+ qrCodeFinal.getID());

                                user.removeQRCode(qrCodeFinal);
                                removeFromQRDoc(qrCodeFinal);
                                updateData();
                                myCallFinal.onCall(user);
                                return;
                            }
                            Log.d("TAG", "Failed: QR not present");


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
        db.collection("QRCodes")
                .whereEqualTo("Hash",qrCode.getUniqueHash())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("DEBUG","found hash "+qrCode.getUniqueHash());
                            boolean found = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               found = true;
                                Log.d("DEBUG","hash exists ");
                               break;
                            }
//                            addToQRDoc(qrCode);
                            if(!found){
                                Log.d("DEBUG","hash does not exist ");
                                addQR(qrCodeFinal); //only add if it does not already exist
                            }
                            addToQRDoc(qrCode);
                        }
                    }
                });
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
        data.put("owner",user.getOwner());
        data.put("QRCodes", user.getAllCodes());
        data.put("QRIdentifiers", user.getAllHashes());

        userRef
                .document(user.getUsername())
                .set(data);
    }

    /**
     *
     */
    public void updateCodeComment(QRCode code, String comment){ ;
        DocumentReference qrRef = db.collection("QRCodes").document(code.getUniqueHash());
        qrRef.update("comments", FieldValue.arrayUnion(comment));
    }

    public void retrieveComments(QRCode code, CommentCall call){
        db.collection("QRCodes")
                .whereEqualTo("Hash",code.getUniqueHash())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> comments = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                comments = (List<String>)document.get("comments");

                            }
                            call.onCall(comments);
                        }
                    }
                });
    }

    /**
     * updates the QR code in the QRCodes collection
     * @param qrCode
     *      the QR code to be updated
     */
    public void updateQR(QRCode qrCode){
        qrCode.setImage(""); //never save a group image
        HashMap<String, Object> data = new HashMap<>();
        data.put("Hash", qrCode.getUniqueHash());
        data.put("code",qrCode);
        db.collection("QRCodes")
                .document(qrCode.getUniqueHash())
                .update(data);

    }

    /**
     * add the QR code to the QRCodes collection for the first time
     * @param qrCode
     *      the QR code to be added
     *
     */
    public void addQR(QRCode qrCode){
        qrCode.setImage(""); //never save a group image
        HashMap<String, Object> data = new HashMap<>();
        data.put("Hash", qrCode.getUniqueHash());
        data.put("code",qrCode);
        db.collection("QRCodes")
                .document(qrCode.getUniqueHash())
                .set(data);

    }

    /**
     * removes a user from the QR document
     * @param qrCode
     *      the qrCode to remove the user from
     */
    public void removeFromQRDoc(QRCode qrCode){
        DocumentReference qrRef = db.collection("QRCodes").document(qrCode.getUniqueHash());
        qrRef.update("users", FieldValue.arrayRemove(user.getUsername()));
    }

    /**
     * adds a user to the QR document
     * @param qrCode
     *      the QR code to add the user to
     */
    public void addToQRDoc(QRCode qrCode){
        DocumentReference qrRef = db.collection("QRCodes").document(qrCode.getUniqueHash());
        qrRef.update("users", FieldValue.arrayUnion(user.getUsername()));
    }

    public void retrievePeople(QRCode qrCode, UserCall myCall){

        db.collection("QRCodes")
                .whereEqualTo("Hash",qrCode.getUniqueHash())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users = (List<String>)document.get("users");

                            }
                            myCall.onCall(users);
                        }
                    }
                });
    }

    public void getCodes(CodeCall myCall){
        db.collection("QRCodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<QRCode> qrCodes= new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                HashMap codeMap = (HashMap)data.get("code");
                                Log.d("DEBUG","code: "+(String)codeMap.get("uniqueHash"));
                                QRCode qrCode = new QRCode(Integer.parseInt((String)codeMap.get("id")), (String)codeMap.get("uniqueHash"),
                                        (double)codeMap.get("latitude"), (double)codeMap.get("longitude"), (String)codeMap.get("image"));
////                                QRCode qrCode = (QRCode)data.get("code");
                                qrCodes.add(qrCode);
                            }
                            myCall.onCall(qrCodes);
                        }
                    }
                });
    }



}
