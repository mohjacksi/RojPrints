package com.mjacksi.rojprints.SimpleAlbum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.chrisbanes.photoview.PhotoView;
import com.mjacksi.rojprints.R;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;

import github.nisrulz.screenshott.ScreenShott;

public class HeaderActivity extends AppCompatActivity {
    private static final String TAG = HeaderActivity.class.getSimpleName();
    LinearLayout screenshot;
    EditText editTextTop, editTextBottom;
    Image image;
    PhotoView imageView;
    Button changeButton;
    int color = R.color.colorPrimary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().getBoolean("ratio",true)) {
            setContentView(R.layout.activity_header_sequare);
        } else {
            setContentView(R.layout.activity_header);
        }

        toolbarSetup("Header");

        image = getIntent().getParcelableExtra("image");
        imageView = findViewById(R.id.image);

        Glide.with(this)
                .load(image.getPath())
                .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                .into(imageView);

        screenshot = findViewById(R.id.screenshoot);
        editTextTop = findViewById(R.id.editTextTop);
        editTextBottom = findViewById(R.id.editTextBottom);
        changeButton = findViewById(R.id.changeButton);
        changeButton.setVisibility(View.GONE);

        editTextTop.setVisibility(View.GONE);
        editTextBottom.setVisibility(View.GONE);


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


    public void changeEditText(View v) {
        switch (v.getId()) {
            case R.id.non:
                editTextTop.setVisibility(View.GONE);
                editTextBottom.setVisibility(View.GONE);
                changeButton.setVisibility(View.GONE);
                break;
            case R.id.top:
                editTextTop.setVisibility(View.VISIBLE);
                editTextBottom.setVisibility(View.GONE);
                changeButton.setVisibility(View.VISIBLE);
                break;
            case R.id.bottom:
                editTextTop.setVisibility(View.GONE);
                editTextBottom.setVisibility(View.VISIBLE);
                changeButton.setVisibility(View.VISIBLE);
                break;
            case R.id.both:
                editTextTop.setVisibility(View.VISIBLE);
                editTextBottom.setVisibility(View.VISIBLE);
                changeButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void changeColor(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(getResources().getColor(R.color.colorPrimary))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void changeBackgroundColor(int selectedColor) {
        screenshot.setBackgroundColor(selectedColor);
        color = selectedColor;
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

            Bitmap bitmap = ScreenShott.getInstance().takeScreenShotOfView(screenshot);
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

            SharedPreferences sp = getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putInt("color", color);

            editor.apply();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("image", image);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
