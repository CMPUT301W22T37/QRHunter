package com.example.qrhunter;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;



public class UserTest {


    @Test
    void testAddQR(){
        User user = new User("testUser","test123#gmail.com");
        assertTrue(user.addCode(new QRCode("ABC5DGW54")));
        assertEquals(3,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now three total
        assertFalse(user.addCode(new QRCode("ABC5DGW54")));


    }
    @Test
    void testRemoveQR(){
        User user = new User("testUser","test123#gmail.com");
        QRCode qr = new QRCode("ABC5DGW54");
        assertTrue(user.addCode(qr));
        assertEquals(3,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now three total
        assertTrue(user.removeQRCode(qr));
        assertEquals(2,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now two total
    }
    @Test
    void testGetTotalScore(){
        User user = new User("testUser","test123#gmail.com"); //initializes qr code with two qr codes of value 111 and 134
        assertEquals(245,user.getTotalScore());
    }
    @Test
    void testNumCodes(){
        User user = new User("testUser","test123#gmail.com");
        assertEquals(user.getNumCodes(),user.getAllCodes().size());
    }

}
