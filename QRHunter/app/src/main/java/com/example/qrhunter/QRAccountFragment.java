package com.example.qrhunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

/**
 * Fragment to create the account sign in QR code
 */
public class QRAccountFragment extends DialogFragment {
    private User user;
    private Bitmap qr;

    /**
     * The constructor, requires both the current user and the bitmap of the QR code
     * @param user
     *      the current user
     * @param qr
     *      the bitmap of the account QR code
     */
    public QRAccountFragment(User user, Bitmap qr){
        this.user = user;
        this.qr = qr;
    }

    /**
     * called when created to set up QR image
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_qraccount_fragment,null);
        ImageView imageView = view.findViewById(R.id.account_qr_code);
        imageView.setImageBitmap(qr);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(user.getUsername())

                .setPositiveButton("OKAY",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        return;
                    }
                }).create();

    }

}