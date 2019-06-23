package com.mjacksi.rojprints.PhotoPrint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumImagesListActivity;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;

public class ChoosePhoto extends AppCompatActivity {
    final int SINGLE_IMAGE_PICKER_REQ_CODE = 200;
    final String SAMPLE_CROPPED_IMAGE_NAME = "uCrop";

    float w, h;
    boolean ratioChanged = false;
    Image image;
    ImageView imageView;
    CardView frameLayout;
    ConstraintLayout constraintLayout;
    ConstraintSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        imageView = findViewById(R.id.single_image_view);
        frameLayout = findViewById(R.id.card);
        constraintLayout = findViewById(R.id.constratin_single_image);
        set = new ConstraintSet();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"change image", "Crop & rotate"};
                //"Write on image", "Filters",

                AlertDialog.Builder builder = new AlertDialog.Builder(ChoosePhoto.this);
                builder.setTitle("Chose an action:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(ChoosePhoto.this, "Name: " + items[item], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if (item == 0) {
                            startPickPicture();
                        } else if (item == 1) {
                            startCrop();
                        }
                    }
                }).show();
            }
        });


        h = getIntent().getFloatExtra("h", 1);
        w = getIntent().getFloatExtra("w", 1);

        set.clone(constraintLayout);
        set.setDimensionRatio(frameLayout.getId(), String.format("H,%f:%f", w, h));
        set.applyTo(constraintLayout);

        startPickPicture();
        String title = getIntent().getExtras().getString("size");
        toolbarSetup(title);
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

    private void startCrop() {
        Uri uri = Uri.fromFile(new File(image.getPath()));
        String shortId = RandomStringUtils.randomAlphanumeric(4);
        File tempCropped = new File(getCacheDir(), shortId + image.getName());
        Uri destinationUri = Uri.fromFile(tempCropped);
        UCrop uCrop = UCrop.of(uri, destinationUri);
        if (ratioChanged) {
            uCrop.withAspectRatio(h, w);
        }else{
            uCrop.withAspectRatio(w, h);
        }
        uCrop.start(ChoosePhoto.this);
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
                .setLimitMessage("14 images is max")// Selection limit message
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
            image = (Image) data.getParcelableArrayListExtra(Config.EXTRA_IMAGES).get(0);
            startCrop();
            updateUi();
        }

        // uCrop
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            image.setPath(resultUri.toString().replace("file://", ""));
            updateUi();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUi() {
        Glide.with(this)
                .load(image.getPath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(imageView);

    }

    public void changeRatio(View view) {
        set.clone(constraintLayout);
        if (ratioChanged)
            set.setDimensionRatio(frameLayout.getId(), String.format("W,%f:%f", h, w));
        else
            set.setDimensionRatio(frameLayout.getId(), String.format("H,%f:%f", h, w));
        set.applyTo(constraintLayout);
        ratioChanged = !ratioChanged;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_menu, menu);
        MenuItem item = menu.findItem(R.id.save_album);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_to_cart_album) {
            addToCart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToCart() {
    }
}
