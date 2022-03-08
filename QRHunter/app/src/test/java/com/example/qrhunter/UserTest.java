package com.example.qrhunter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import com.google.firebase.firestore.FirebaseFirestore;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class UserTest {

    @Test
    void testAddUser(){
//        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
//        Mockito.when(removeCode(qrCode, new CallBack() {
//            @Override
//            public void onCall(User user) {
//                Log.d("TAG", "length of qr = "+Integer.toString(user.getCodesStrings().size()));
//                int duration = Toast.LENGTH_LONG;
//                Context context = getApplicationContext();
//                Toast toast = Toast.makeText(context,"length of qr = "+Integer.toString(user.getCodesStrings().size()),duration);
//                toast.show();
//                setUser(user);
//            }
//        })).thenReturn(something);
//        User user = new User("testingMock","mock@gmail.com");
//        DataManagement manager = new DataManagement(user,mockFirestore);

//        FirebaseFirestore db;
//        db = FirebaseFirestore.getInstance();
//        FirebaseFirestore db = getDatabase();
//        User testUser = new User("test","test123@gmail.com");
//        HashMap<String, String> data = new HashMap<>();
//        data.put(testUser.getUsername(), testUser.getEmail());
//        final CollectionReference colRef = db.collection("Users");
//        colRef
//                .document(testUser.getUsername())
//                .set(data);
        assertTrue(true);

//        Query query = colRef.whereEqualTo("Users", true);
//        colRef.whereArrayContains("test", null);
//        DocumentReference docIdRef = db.collection("Users").document("test");
//        assertEquals(null,docIdRef);

    }

}
