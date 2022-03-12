package com.example.qrhunter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

/**
 * The camera fragment for the QRCode
 */
public class CameraFragment extends Fragment {
    private static final int RESULT_OK = -1; //-1 if yes
    private static final int RESULT_CANCELED = 0; //0 if no
    private ImageView img;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment_layout, container, false);

        img = (ImageView)view.findViewById(R.id.captured_image);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CAMERA", "in activity "+requestCode+" Result code: "+resultCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);
                ((ScanConfirmationPage)getActivity()).setBitmap(bitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Log.d("CAMERA", "cancelled");
//                Toast.makeText(CameraFragment.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
