package com.ayvengoza.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by ang on 22.09.17.
 */

public class PhotoFragment extends DialogFragment {
    private static final String KEY_PATH_TO_PHOTO = "photo_to_photo";
    private ImageView mPhotoView;

    public static PhotoFragment newInstance(String pathToPhoto){
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PATH_TO_PHOTO, pathToPhoto);
        photoFragment.setArguments(args);
        return photoFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_dialog_photo, null);
        mPhotoView = (ImageView) v.findViewById(R.id.photo_view);
        String path = (String) getArguments().getSerializable(KEY_PATH_TO_PHOTO);
        Bitmap bitmap = PictureUtils.getScaledBitmap(path, getActivity());
        mPhotoView.setImageBitmap(bitmap);
        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
        return ad;
    }
}
