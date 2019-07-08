package com.mjacksi.rojprints.SimpleAlbum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.mjacksi.rojprints.R;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import github.nisrulz.screenshott.ScreenShott;

public class LayoutImagesActivity extends AppCompatActivity {
    private static final int SINGLE_IMAGE_PICKER_REQ_CODE = 200;
    private static final String TAG = LayoutImagesActivity.class.getSimpleName();

    PhotoView image1, image2, image3, image4;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4,
            linearLayoutParent1, linearLayoutParent2,
            viewScreenshot;
    int imageSelected = 1;
    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().getBoolean("ratio",true)) {
            setContentView(R.layout.activity_layout_images_square);
        } else {
            setContentView(R.layout.activity_layout_images);
        }

        image = getIntent().getParcelableExtra("image");

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);


        View.OnClickListener onClickListener = (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image1:
                        imageSelected = 1;
                        break;
                    case R.id.image2:
                        imageSelected = 2;
                        break;
                    case R.id.image3:
                        imageSelected = 3;
                        break;
                    case R.id.image4:
                        imageSelected = 4;
                        break;
                }
                startPickPicture();
            }
        });
        image1.setOnClickListener(onClickListener);
        image2.setOnClickListener(onClickListener);
        image3.setOnClickListener(onClickListener);
        image4.setOnClickListener(onClickListener);


        linearLayout1 = findViewById(R.id.linear_layout1);
        linearLayout2 = findViewById(R.id.linear_layout2);
        linearLayout3 = findViewById(R.id.linear_layout3);
        linearLayout4 = findViewById(R.id.linear_layout3);
        linearLayoutParent1 = findViewById(R.id.linear_layout_parent1);
        linearLayoutParent2 = findViewById(R.id.linear_layout_parent2);
        viewScreenshot = findViewById(R.id.view_screenshot);
        toolbarSetup("Layout your photo");

        setToImageView(image);

        linearLayout2.setVisibility(View.GONE);
        linearLayout3.setVisibility(View.GONE);
        linearLayout4.setVisibility(View.GONE);
        linearLayoutParent2.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {

            Bitmap bitmap = ScreenShott.getInstance().takeScreenShotOfView(viewScreenshot);
            File file = null;
            try {
                file = ScreenShott.getInstance().saveScreenshotToPicturesFolder(this, bitmap, "");
                String bitmapFilePath = file.getAbsolutePath();
                Log.d(TAG, "onOptionsItemSelected: " + image.getPath());
                image.setPath(bitmapFilePath);
                Log.d(TAG, "onOptionsItemSelected: " + image.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }


            Intent resultIntent = new Intent();
            resultIntent.putExtra("image", image);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toolbarSetup(String title) {
        Toolbar toolbar = findViewById(R.id.order_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void changeLayout(View view) {
        switch (view.getId()) {
            case R.id.layout1:
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);
                linearLayoutParent2.setVisibility(View.GONE);
                break;
            case R.id.layout2:
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);
                linearLayoutParent2.setVisibility(View.GONE);
                break;
            case R.id.layout3:
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);
                linearLayoutParent2.setVisibility(View.VISIBLE);
                break;
            case R.id.layout4:
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.GONE);
                linearLayoutParent2.setVisibility(View.VISIBLE);
                break;
            case R.id.layout5:
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);
                linearLayoutParent2.setVisibility(View.VISIBLE);
                break;
        }
    }


    void startPickPicture() {

        ImagePicker.with(this)                //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(false)           //  Select multiple images or single image
                .setFolderMode(false)               //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage(getString(R.string._14_images_max))// Selection limit message
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(SINGLE_IMAGE_PICKER_REQ_CODE)            //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .setAlwaysShowDoneButton(false)
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SINGLE_IMAGE_PICKER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Image image = (Image) data.getParcelableArrayListExtra(Config.EXTRA_IMAGES).get(0);
            setToImageView(image);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setToImageView(Image image) {
        PhotoView imageViewSelected = image1;
        switch (imageSelected) {
            case 1:
                imageViewSelected = image1;
                break;
            case 2:
                imageViewSelected = image2;
                break;
            case 3:
                imageViewSelected = image3;
                break;
            case 4:
                imageViewSelected = image4;
                break;
        }

        Glide.with(this)
                .load(image.getPath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(imageViewSelected);
    }
}
