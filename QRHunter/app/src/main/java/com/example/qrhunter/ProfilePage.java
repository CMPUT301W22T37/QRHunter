package com.example.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Class to represent a user's profile
 */
public class ProfilePage extends AppCompatActivity {
    private Button SignInQRCodeBtn;
    private Button createAccountBtn;
    private User user;
    private TextView userNameTextView;
    private TextView emailTextView;
    private ImageView imageView;
    private final static int QRCodeSize = 500;
    private String username;

    /**
     * Called when the page is created
     * @param savedInstanceState
     *      Bundle representing the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        //Getting intent from MainMenu
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("User");
        username = user.getUsername();

        //Finding Views and creating needed creator
        SignInQRCodeBtn = findViewById(R.id.btnQRScanner);
        createAccountBtn = findViewById(R.id.btnCreateAccount);
        userNameTextView = findViewById(R.id.username_textview);
        emailTextView = findViewById(R.id.email_textview);
        imageView = findViewById(R.id.account_qr_code);
        setText();

        SignInQRCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScanLoginCodeActivity.class));
            }
        });

        //Creating the Game QR Code
        try {
            imageView.setImageBitmap(TextToImageEncode(username));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the text for the username and Email Address Textviews
     */
    public void setText(){
        userNameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
    }

    /**
     * Returns a bitmap of the given string for displaying as an image
     * @param Value
     *      String to turn into a QR Code
     * @return
     *      Bitmap to turn into a QR Code
     * @throws WriterException
     *      Can throw a writerException with invalid string
     */
    public Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeSize, QRCodeSize, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void onCreateAccount(View view){
        startActivity(new Intent(getApplicationContext(),CreateAccount.class));
    }
}