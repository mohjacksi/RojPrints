package com.mjacksi.rojprints.SimpleAlbum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mjacksi.rojprints.MainActivity;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.Utilises;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import io.realm.Realm;
import io.realm.RealmResults;


public class SimpleAlbumImagesListActivity extends AppCompatActivity {
    private static final String TAG = SimpleAlbumImagesListActivity.class.getSimpleName();
    final int MULTI_IMAGE_PICKER_REQ_CODE = 100;
    final int SINGLE_IMAGE_PICKER_REQ_CODE = 200;
    final String SAMPLE_CROPPED_IMAGE_NAME = "uCrop";
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    int albumSize = 20;

    int pos_image_changed = 0;
    ArrayList<Image> images = new ArrayList<>();

    float[] ratio = {1, 1};
    String title, id;
    int pricePrePage;

    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_album_images_list);
        Bundle extras = getIntent().getExtras();
        isEditMode = getIntent().hasExtra("edit");
        if (isEditMode) {
            Realm realm = Realm.getDefaultInstance();
            id = extras.getString("album_id");
            Project project = realm.where(Project.class).equalTo("id", id).findFirst();
            title = project.getSize();
            images = project.getImagesAsImageObject();
            pricePrePage = project.getPricePerPage();
        } else {
            images = new ArrayList<>();
            title = extras.getString("title");
            pricePrePage = extras.getInt("price_per_page");
            startPickPictures("multi");
        }


        toolbarSetup(title);
        if (title.equals("15 X 20 cm"))
            ratio[1] = 1.3f;
        imageAdapter = new ImageAdapter(this, albumSize, SimpleAlbumImagesListActivity.this, ratio);
        imageAdapter.setData(images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
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

    void startPickPictures(String type) {

        // new ImagePicker.Builder(this).galleryOnly().start();
        boolean isMulti = type == "multi";
        int reqCode;
        if (isMulti) {
            reqCode = MULTI_IMAGE_PICKER_REQ_CODE;
        } else {
            reqCode = SINGLE_IMAGE_PICKER_REQ_CODE;
        }

        ImagePicker.with(this)                //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(isMulti)           //  Select multiple images or single image
                .setFolderMode(false)               //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("20 images is max")// Selection limit message
                .setMaxSize(albumSize + 1)          //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setSelectedImages(images)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(reqCode)            //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .setAlwaysShowDoneButton(false)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ImagePicker, multi
        if (requestCode == MULTI_IMAGE_PICKER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            for (Image image :
                    images) {
                Log.d(TAG, "onActivityResult: " + image.getPath());
            }
            imageAdapter.setData(images);

            // ImagePicker, single
        } else if (requestCode == SINGLE_IMAGE_PICKER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images.remove(pos_image_changed);
            images.add(pos_image_changed, (Image) data.getParcelableArrayListExtra(Config.EXTRA_IMAGES).get(0));
            imageAdapter.setData(images);
        }

        // uCrop
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            images.get(pos_image_changed).setPath(resultUri.toString().replace("file://", ""));

            imageAdapter.setData(images);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void add_images(View view) {
        startPickPictures("multi");
    }

    public void editImageAt(final int position) {
        final CharSequence[] items = {getString(R.string.change_image), getString(R.string.crop_and_rotate)};


        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleAlbumImagesListActivity.this);
        builder.setTitle(getString(R.string.chose_action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(SimpleAlbumImagesListActivity.this, "Name: " + items[item], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                pos_image_changed = position;
                if (item == 0) {
                    startPickPictures("single");
                } else if (item == 1) {
                    Uri uri = Uri.fromFile(new File(images.get(position).getPath()));
                    String shortId = RandomStringUtils.randomAlphanumeric(4);
                    File tempCropped = new File(getCacheDir(), shortId + images.get(position).getName());
                    Uri destinationUri = Uri.fromFile(tempCropped);

                    UCrop uCrop = UCrop.of(uri, destinationUri);
                    uCrop.withAspectRatio(ratio[0], ratio[1])
                            .start(SimpleAlbumImagesListActivity.this);
                }
            }
        }).show();
    }

    public void deleteImageAt(int position) {
        images.remove(position);
        imageAdapter.setData(images);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if user select more than 14


        int id = item.getItemId();
        if (id == R.id.save_album) {
            saveAlbum();
            return true;
        } else if (id == R.id.add_to_cart_album) {
            if (images.size() < 14) {
                Toast.makeText(this, getString(R.string.min_images_number_14), Toast.LENGTH_SHORT).show();
                return true;
            } else if (images.size() % 2 == 0) {
                Toast.makeText(this, getString(R.string.images_number_should_even), Toast.LENGTH_SHORT).show();
                return true;
            }
            addToCart();
            return true;
        } else if (id == R.id.delete_album) {
            deleteAlbum();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAlbum() {


        if (isEditMode) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Project project = realm.where(Project.class).equalTo("id", id).findFirst();
            project.deleteFromRealm();
            realm.commitTransaction();
            realm.close();

        }
        finish();
    }

    private void saveAlbum() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (isEditMode) {
            Project project = realm.where(Project.class).equalTo("id", id).findFirst();
            project.setImages(images);
        } else {
            String _id = UUID.randomUUID().toString();
            Project project = realm.createObject(Project.class, _id);
            project.setImages(images);
            project.setPricePerPage(pricePrePage);
            project.setSize(title);
            project.setDate(Utilises.getCurrentTime());
        }
        realm.commitTransaction();
        realm.close();
        finish();
    }

    private void addToCart() {
        Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Project project;
        if (isEditMode) {
            project = realm.where(Project.class).equalTo("id", id).findFirst();
            project.setImages(images);
        } else {
            String _id = UUID.randomUUID().toString();
            project = realm.createObject(Project.class, _id);
            project.setImages(images);
            project.setPricePerPage(pricePrePage);
            project.setSize(title);
            project.setType("album");
            project.setDate(Utilises.getCurrentTime());
        }
        project.setInCart(true);
        realm.commitTransaction();
        realm.close();
        finish();
    }
}