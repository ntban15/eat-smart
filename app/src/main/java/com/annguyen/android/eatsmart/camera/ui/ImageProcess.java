package com.annguyen.android.eatsmart.camera.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.annguyen.android.eatsmart.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageProcess extends AppCompatActivity {

    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        Intent intent = getIntent();
        bytes = intent.getByteArrayExtra("image_bytes");

        //get bytes array sent through the intent and load it to the image view
        final ImageView imageView = (ImageView) findViewById(R.id.imagePreview);
        Glide.with(this).load(bytes).into(imageView);

        //if the discard button clicked, navigate back to the camera fragment
        Button discardBtn = (Button) findViewById(R.id.discardBtn);
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ImageProcess.this).clear(imageView);
                finish();
            }
        });
    }
}
