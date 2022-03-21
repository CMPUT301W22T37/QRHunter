package com.example.qrhunter;

import java.util.ArrayList;

/**
 * interface to interact with all QR codes stored on the database
 */
public interface CodeCall {
    /**
     * an action to be performed upon calling
     * @param qrCodes
     *      the QR codes from the database
     */
    void onCall(ArrayList<QRCode> qrCodes);
}
