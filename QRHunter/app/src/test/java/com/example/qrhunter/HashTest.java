package com.example.qrhunter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTest {
    @Test
    void testScore() {
        QRCode code=new QRCode("BFG5DGW54");
        assertEquals(111,code.calculateScore("BFG5DGW54"));

        QRCode code1=new QRCode("DCFJFJFJ");
        assertEquals(134,code1.calculateScore("DCFJFJFJ"));
    }


}
