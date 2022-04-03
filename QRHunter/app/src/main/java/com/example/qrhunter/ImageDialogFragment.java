package com.example.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * a fragment for the image of a QR code
 */
public class ImageDialogFragment extends DialogFragment {
    Bitmap img;
    public ImageDialogFragment(Bitmap img){
        this.img = img;
    }
    /**
     * called when created to set up QR image
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_fragment,null);
        ImageView imageView = view.findViewById(R.id.qr_image);
        imageView.setImageBitmap(img);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .create();

    }
}
