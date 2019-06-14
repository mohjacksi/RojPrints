package com.mjacksi.rojprints.SimpleAlbum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mjacksi.rojprints.MainActivity;
import com.mjacksi.rojprints.R;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;


import java.util.ArrayList;
import java.util.Collections;


public class SimpleAlbumImagesListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    int albumSize = 14;
    private ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_album_images_list);

        images = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, albumSize, SimpleAlbumImagesListActivity.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
                //define span size for this position
                //some example for your first three items
//                if(position == 0) {
//                    return 2; //item will take 1/3 space of row
//                }
                //return 1;
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(images, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(images, i, i - 1);
                    }
                }

                imageAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                imageAdapter.setData(images);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        imageAdapter.setData(images);
//                    }
//                }, 500);
            }


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int fromPosition = viewHolder.getAdapterPosition();
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(imageAdapter);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new ItemOffsetDecoration(
                getResources().getDimensionPixelSize(R.dimen.photos_list_spacing)));

        startPickPictures();
    }


    void startPickPictures() {

        // new ImagePicker.Builder(this).galleryOnly().start();

        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(false)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("14 images is max")    // Selection limit message
                .setMaxSize(albumSize + 1)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .setAlwaysShowDoneButton(false)
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            imageAdapter.setData(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void add_images(View view) {
        startPickPictures();
    }

    public void editImageAt(int position) {
        final CharSequence[] items = { "Crop & rotate", "Write on image", "Filters" };

        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleAlbumImagesListActivity.this);
        builder.setTitle("Chose an action:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(SimpleAlbumImagesListActivity.this, "Name: " + items[item], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).show();
    }


    // Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_album_image_list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.next) {
            if (images.size() < 5) {
                Toast.makeText(this, "Album should have 4 images at lest", Toast.LENGTH_SHORT).show();
            } else if (images.size() % 2 == 0) {
                Toast.makeText(this, "Album should have even number of images", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "OK!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "" + images.size(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}