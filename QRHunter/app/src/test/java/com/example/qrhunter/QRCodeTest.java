package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QRCodeTest {
    @Test
    void testScore() {

        QRCode code=new QRCode("BFG5DGW54", 150);
        assertEquals(111,code.getScore());

        QRCode code1=new QRCode("DCFJFJFJ", 151);


        assertEquals(134,code1.getScore());
    }

    @Test
    void testGetID(){
        QRCode code = new QRCode("BFG5DGW54", 150);
        assertEquals(150, Integer.parseInt(code.getID()));
    }



}
