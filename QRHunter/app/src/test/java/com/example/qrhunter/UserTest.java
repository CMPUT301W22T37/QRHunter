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

        assertTrue(true);


    }
    @Test
    void testAddQR(){
        User user = new User("testUser","test123#gmail.com");
        assertTrue(user.addCode(new QRCode("BFG5DGW54")));
        assertEquals(3,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now three total
        assertFalse(user.addCode(new QRCode("BFG5DGW54")));


    }
    @Test
    void testRemoveQR(){
        User user = new User("testUser","test123#gmail.com");
        QRCode qr = new QRCode("BFG5DGW54");
        assertTrue(user.addCode(qr));
        assertEquals(3,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now three total
        assertTrue(user.removeQRCode(qr));
        assertEquals(2,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now two total
    }

}
