package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by j_mei on 2018-03-26.
 */

public class imageImportBottomSheet extends BottomSheetDialogFragment implements Observer {

    private View view;

    /* get Model to send info */
    private ClothesModel clothesData;

    /* view param */
    private Button cameraButton, galleryButton, imageIgnoreButton, imageSetButton;
    private ImageView selectedImageView;
    private Spinner chooseImageClothTypeSpinner;
    private  ArrayAdapter<String> spinnerAdapter;

    /* user input param */
    private String selectedClothType;
    Bitmap tempPhoto;

    /* camera request number */
    private static final int REQUEST_CAMERA_IMAGE_CAPTURE = 2;
    private static final int REQUEST_GALLERY_IMAGE_SELECT = 3;
    private static final int REQUEST_PERMISSIONS = 4;


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        view = View.inflate(getContext(), R.layout.image_import_bottom_sheet, null);
        dialog.setContentView(view);

        initiateClothesInfo();
        initiateItemOnView();
        itemOnClickListener();
    }

    /**
     * Clothes section
     */


    private void initiateClothesInfo() {
        clothesData = ClothesModel.getInstance();
    }



    /**
     *  UI section
     */
    private void initiateItemOnView() {
        cameraButton = view.findViewById(R.id.cameraButton);
        galleryButton = view.findViewById(R.id.galleryButton);
        imageIgnoreButton = view.findViewById(R.id.imageImportIgnore);
        imageSetButton = view.findViewById(R.id.imageImportSet);
        selectedImageView = view.findViewById(R.id.selectedImageView);

        /* set spinner */
        chooseImageClothTypeSpinner = view.findViewById(R.id.chooseClothTypeList);
        spinnerAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cloth_type));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        chooseImageClothTypeSpinner.setAdapter(spinnerAdapter);
    }


    private void itemOnClickListener() {

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
                }else{
                    takePicture();
                }

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        imageIgnoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempPhoto = null;
                dismiss();
            }
        });

        imageSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedClothType = chooseImageClothTypeSpinner.getSelectedItem().toString();
                if (tempPhoto != null){
                    int check = clothesData.saveImage(selectedClothType, tempPhoto);
                    if (check == 1){
                        makeToast("saved image successfully");
                    }
                    else {
                        makeToast("can't find table name");
                    }
                }
                else{
                    makeToast("you didn't select photo");
                }
                dismiss();
            }
        });
    }


    /**
     * camera and gallery photo section
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* if camera -> show bitmap */
        if (requestCode == REQUEST_CAMERA_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            tempPhoto = (Bitmap) data.getExtras().get("data");
            updateSelectedImageView(tempPhoto);
        }
        /* if gallery -> get file, then show bitmap */
        if (requestCode == REQUEST_GALLERY_IMAGE_SELECT && resultCode == Activity.RESULT_OK){
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                tempPhoto = BitmapFactory.decodeStream(imageStream);
                tempPhoto = resizeImage(tempPhoto);
                updateSelectedImageView(tempPhoto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                makeToast("file not found");
            }
        }
    }

    /* resize image */
    private Bitmap resizeImage(Bitmap photo) {
        int hight = photo.getHeight();
        int width = photo.getWidth();
        int limit = 300;
        double scale = 0.0;
        if (hight > limit && width > limit){
            if (hight <= width){
                scale = hight/limit;
            }
            else{
                scale = width/limit;
            }
            hight = (int) Math.floor(hight/scale);
            width = (int) Math.floor(width/scale);
        }
        Bitmap resizedPhoto = Bitmap.createScaledBitmap(tempPhoto, width, hight, false);
        return resizedPhoto;
    }

    /*show the selected image to user*/
    private void updateSelectedImageView(Bitmap photo) {
        selectedImageView.setImageBitmap(photo);
    }

    /* open camera*/
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE_CAPTURE);
        }
    }

    /* open gallery*/
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_IMAGE_SELECT);
    }

    /**
     *  update function
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {

    }

    /* test dialog*/
    public AlertDialog.Builder testDialog(Context c) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(c);
        alertBuilder.setTitle("test");
        alertBuilder.setMessage("");

        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
            }
        });
        return alertBuilder;
    }

    private void makeToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }
}
