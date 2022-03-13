package com.example.qrhunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

/**
 * Represents the image taken at a location for the QR code
 */
public class LocationImage {

    /**
     * encode the image to a string to be saved in the database
     * @param bitmap
     *      the bitmap to be saved
     * @return
     *      the resulting string
     */
    public static String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if(baos.size()>65000){
            return null;
        }else{
            byte[] data = baos.toByteArray();
            String imageB64 = Base64.encodeToString(data, Base64.DEFAULT);
            return imageB64;

        }
    }

    /**
     * Decodes a string representation of an image (saved on the database) to a bitmap
     * @param imageB64
     *      the string representation of the image
     * @return
     *      the bitmap
     */
    public static Bitmap decodeImage(String imageB64){
        if(imageB64==null || imageB64.equals("")){
            return null;
        }
        byte[] imageByte = Base64.decode(imageB64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        return bitmap;
    }
}
