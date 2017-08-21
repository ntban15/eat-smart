package com.annguyen.android.eatsmart.camera.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.annguyen.android.eatsmart.R;

public class ImageProcess extends AppCompatActivity {

    private byte[] bytes;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        Intent intent = getIntent();
        bytes = intent.getByteArrayExtra("image_bytes");

        //get bytes array sent through the intent and load it to the image view
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView = (ImageView) findViewById(R.id.imagePreview);

        //bytes arrays of images taken by the camera have reverse orientation.
        //so we have to rotate it back to right orientation
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        imageView.setImageBitmap(rotatedBitmap);

        //if the discard button clicked, navigate back to the camera fragment
        Button discardBtn = (Button) findViewById(R.id.discardBtn);
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //if the analyze button is clicked, the image will be analyzed
        Button analyzeBtn = (Button) findViewById(R.id.analyzeBtn);
        analyzeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ResultDialog resultDialog = ResultDialog.newInstance(bytes);
                resultDialog.show(fm, "result_dialog");
            }
        });

    }
}
