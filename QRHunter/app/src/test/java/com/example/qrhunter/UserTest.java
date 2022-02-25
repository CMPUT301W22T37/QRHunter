package com.example.qrhunter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class UserTest {

    private FirebaseFirestore getDatabase(){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        return db;

    }
//    @Test
//    void testAddUser(){
//        assertTrue(true);
////        FirebaseFirestore db = getDatabase();
////        User testUser = new User("test","test123@gmail.com");
////        HashMap<String, String> data = new HashMap<>();
////        data.put(testUser.getUsername(), testUser.getEmail());
////        final CollectionReference colRef = db.collection("Users");
////        colRef
////                .document(testUser.getUsername())
////                .set(data);
////
////        Query query = colRef.whereEqualTo("Users", true);
////        colRef.whereArrayContains("test", null);
////        DocumentReference docIdRef = db.collection("Users").document("test");
////        assertEquals(null,docIdRef);
//
//    }

}
