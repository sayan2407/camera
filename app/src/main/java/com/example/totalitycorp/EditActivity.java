package com.example.totalitycorp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class EditActivity extends AppCompatActivity {

    private ImageView img;
    private Uri file;
    final int PIC_CROP = 1;
    private Bitmap bitmap;
    private String ImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mapping();

        Intent intent = getIntent();
        bitmap = (Bitmap)intent.getParcelableExtra("uri");
        img.setImageBitmap(bitmap);
        file = getImageUri(getApplicationContext(), bitmap);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                bitmap = extras.getParcelable("data");

                img.setImageBitmap(bitmap);
            }
        }
    }

    public void mapping(){
        img = findViewById(R.id.img);
    }

    public void crop(View view) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(file, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, "errorMessage", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(EditActivity.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }

    public void download(View view) {
//        drawable = getResources().getDrawable(R.drawable.demo_image);
//
//        bitmap = ((BitmapDrawable)drawable).getBitmap();
          bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();

        ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "demo_image",
                "demo_image"
        );

        file = Uri.parse(ImagePath);

        Toast.makeText(EditActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();

    }

    public void rotate(View view) {
//     img.setRotation(90);
        roateImage(img);
    }
    public void roateImage(ImageView imageView) {
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX); //required
        matrix.postRotate((float) 180f, imageView.getDrawable().getBounds().width()/2,    imageView.getDrawable().getBounds().height()/2);
        imageView.setImageMatrix(matrix);
    }
}