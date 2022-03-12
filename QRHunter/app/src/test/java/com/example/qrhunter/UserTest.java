package com.example.qrhunter;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    @Test
    void testAddQR(){
        User user = new User("testUser","test123#gmail.com");
        assertTrue(user.addCode(new QRCode("BFG5DGW54", 150)));

        assertEquals(3,user.getAllCodes().size()); //currently we init user with 2 qr codes already for testing, hence why now three total
        assertFalse(user.addCode(new QRCode("ABC5DGW54", 151)));

    }
    @Test
    void testRemoveQR(){
        User user = new User("testUser","test123#gmail.com");
        QRCode qr = new QRCode("BFG5DGW54", 150);

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

    @Test
    void testNextID(){
        User user = new User("testUser","test123#gmail.com");
        assertEquals(user.getNextID(), 3);//2 codes initialized, thus next ID should be 3

        QRCode code = new QRCode("A", user.getNextID());
        user.addCode(code);
        assertEquals(4, user.getNextID());

        user.removeQRCode(code);
        assertEquals(user.getNextID(), 3);
    }

    @Test
    void testGetHighest(){
        User user = new User("testUser","test123#gmail.com");
        int highest = user.getHighest();
        assertEquals(134, highest);
    }

    @Test
    void testGetLowest(){
        User user = new User("testUser","test123#gmail.com");
        int lowest = user.getLowest();
        assertEquals(111, lowest);
    }
}
