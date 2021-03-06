package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    @Test
    void testAddQR(){

        User user = new User("testUser","test123#gmail.com");
        assertTrue(user.addCode(new QRCode("BFG5DGW54", 150)));

        assertEquals(1,user.getAllCodes().size());

    }
    @Test
    void testRemoveQR(){

        User user = new User("testUser","test123#gmail.com");
        QRCode qr = new QRCode("BFG5DGW54", 150);
        assertTrue(user.addCode(qr));
        assertEquals(1,user.getAllCodes().size());
        user.removeQRCode(qr);
        assertEquals(0,user.getAllCodes().size());

    }
    @Test
    void testGetTotalScore(){
        User user = new User("testUser","test123#gmail.com");
        QRCode code=new QRCode("BFG5DGW54", 150);
        user.addCode(code);
        QRCode code1=new QRCode("DCFJFJFJ", 151);
        user.addCode(code1);

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
        assertEquals(user.getNextID(), 1);//2 codes initialized, thus next ID should be 3

        QRCode code = new QRCode("A", user.getNextID());
        user.addCode(code);
        assertEquals(2, user.getNextID());

        user.removeQRCode(code);
        assertEquals(user.getNextID(), 1);
    }

    @Test
    void testGetHighest(){
        User user = new User("testUser","test123#gmail.com");
        QRCode code=new QRCode("BFG5DGW54", 150);
        user.addCode(code);
        QRCode code1=new QRCode("DCFJFJFJ", 151);
        user.addCode(code1);
        int highest = user.getHighest();
        assertEquals(134, highest);

        //testing when there are no stored codes
        user.clearCodes();
        highest = user.getHighest();
        assertEquals(0, highest);
    }

    @Test
    void testGetLowest(){
        User user = new User("testUser","test123#gmail.com");
        QRCode code=new QRCode("BFG5DGW54", 150);
        user.addCode(code);
        QRCode code1=new QRCode("DCFJFJFJ", 151);
        user.addCode(code1);
        int lowest = user.getLowest();
        assertEquals(111, lowest);

        //testing when there are no stored codes
        user.clearCodes();
        lowest = user.getHighest();
        assertEquals(0, lowest);
    }

    @Test
    void updateQRCode(){
        User user = new User("testUser", "test123@gmail.com");
        QRCode code0=new QRCode("BFG5DGW54", 150);
        user.addCode(code0);
        QRCode code1=new QRCode("DCFJFJFJ", 151);
        user.addCode(code1);
        
        QRCode code = new QRCode(user.getNextID(), "f4ccd05b3271c386ee55d9876c7450012a3b361e5065c09dc22075e38b3cc35c", 0, 0, "");
        user.updateCode(1, code);
        int number = user.getHighest();//Should return updated QRCode since there is no re-sorting
        assertEquals(45, number);
    }
}
