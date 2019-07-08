package com.mjacksi.rojprints.SimpleAlbum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.ImageRealm;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.Utilises;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.yalantis.ucrop.UCrop;


import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;


import io.realm.Realm;
import io.realm.RealmList;
import needle.Needle;
import needle.UiRelatedProgressTask;


public class SimpleAlbumImagesListActivity extends AppCompatActivity {
    private static final String TAG = SimpleAlbumImagesListActivity.class.getSimpleName();
    private static final int LAYOUT_REQUEST_CODE = 300;
    private static final int HEADER_REQUEST_CODE = 400;
    final int MULTI_IMAGE_PICKER_REQ_CODE = 100;
    final int SINGLE_IMAGE_PICKER_REQ_CODE = 200;
    final String SAMPLE_CROPPED_IMAGE_NAME = "uCrop";
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    int albumSize = 20;

    int pos_image_changed = 0;
    ArrayList<Image> images = new ArrayList<>();

    ProgressDialog progressDialog;

    float[] ratio = {1, 1};
    String title, id;
    int pricePrePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_album_images_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("In progress...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_done);
        progressDialog.setCancelable(false);


        Bundle extras = getIntent().getExtras();
        isEditMode = getIntent().hasExtra("edit");
        if (isEditMode) {
            Realm realm = Realm.getDefaultInstance();
            id = extras.getString("album_id");
            Project project = realm.where(Project.class).equalTo("id", id).findFirst();
            title = project.getSize();
            images = project.getImagesAsImageObject();
            pricePrePage = project.getPrice();
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

    boolean isEditMode = false;

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


    public void add_images(View view) {
        startPickPictures("multi");
    }

    public void editImageAt(final int position) {
        final CharSequence[] items;
        if (position == 0) {
            items = new CharSequence[]{getString(R.string.change_image), getString(R.string.crop_and_rotate), getString(R.string.layout), "Header Typing"};
        } else {
            items = new CharSequence[]{getString(R.string.change_image), getString(R.string.crop_and_rotate), getString(R.string.layout)};

        }

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
                } else if (item == 2) {
                    Intent i = new Intent(SimpleAlbumImagesListActivity.this, LayoutImagesActivity.class);
                    i.putExtra("image", images.get(position));
                    i.putExtra("ratio", ratio[0] == ratio[1]);
                    startActivityForResult(i, LAYOUT_REQUEST_CODE);
                } else if (item == 3) {
                    Intent i = new Intent(SimpleAlbumImagesListActivity.this, HeaderActivity.class);
                    i.putExtra("image", images.get(position));
                    i.putExtra("ratio", ratio[0] == ratio[1]);
                    startActivityForResult(i, HEADER_REQUEST_CODE);
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
            finish();
            return true;
        } else if (id == R.id.next) {

            if (images.size() < 14) {
                Toast.makeText(this, getString(R.string.min_images_number_14), Toast.LENGTH_SHORT).show();
                return true;
            } else if (images.size() % 2 == 0) {
                Toast.makeText(this, getString(R.string.images_number_should_even), Toast.LENGTH_SHORT).show();
                return true;
            }

//            addToCart();
            project = saveAlbum();
            Intent intent = new Intent(SimpleAlbumImagesListActivity.this, DedicationActivity.class);
            intent.putExtra("album_id", project.getId());
            startActivity(intent);
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

    private Project saveAlbum() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Project project = null;
        if (isEditMode) {
            project = realm.where(Project.class).equalTo("id", id).findFirst();
            project.setImages(images);
        } else {
            String _id = UUID.randomUUID().toString();
            project = realm.createObject(Project.class, _id);
            project.setImages(images);
            project.setPrice(pricePrePage);
            project.setSize(title);
            project.setDate(Utilises.getCurrentTime());
            project.setSquare(ratio[0] == ratio[1]);
        }
        realm.commitTransaction();
        realm.close();

        return project;
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
            id = _id;
            project = realm.createObject(Project.class, _id);
            project.setImages(images);
            project.setPrice(pricePrePage);
            project.setSize(title);
            project.setType("Album");
            project.setDate(Utilises.getCurrentTime());
        }
        project.setInCart(true);
        realm.commitTransaction();
        realm.close();
        finish();
    }

    private void cropImages() {
        for (Image image :
                images) {


        }
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

        // result from LayoutImagesActivity
        if (requestCode == LAYOUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            images.remove(pos_image_changed);
            images.add(pos_image_changed, (Image) data.getParcelableExtra("image"));
            imageAdapter.setData(images);
        }
        // result from HeaderActivity
        if (requestCode == HEADER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Toast.makeText(this, "result ok!", Toast.LENGTH_SHORT).show();
            images.remove(pos_image_changed);
            images.add(pos_image_changed, (Image) data.getParcelableExtra("image"));
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

    Project project;

    public void preview(View view) {

        project = saveAlbum();
        uploadImages(project);
    }


    int images_counter = 0;
    int images_uploaded = 0;

    public void uploadImages(Project project) {
        images_counter = 0;
        images_uploaded = 0;
        for (int j = 0; j < images.size(); j++) {
            images_counter++;
        }


        progressDialog.setMax(images_counter);
        progressDialog.show();


        RealmList<ImageRealm> images = project.getImages();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("Mj");
        StorageReference storageRef = mStorageRef.child("M-" + project.getId());
        for (int j = 0; j < images.size(); j++) {
            ImageRealm image = images.get(j);
            if (!image.getUrl().equals("")) {
                updateProgress();
                continue;
            }

            Uri file = Uri.fromFile(new File(image.getpath()));
            StorageReference ref = storageRef.child(image.getName());

            UploadTask uploadTask = ref.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        updateProgress();

                        Log.d(TAG, "onComplete: " + images_uploaded);
                        Log.d(TAG, "onComplete: " + images_counter);
                        Uri downloadUri = task.getResult();
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        image.setUrl(downloadUri.toString());
                        realm.commitTransaction();
                        realm.close();

                        Log.d(TAG, "then: " + downloadUri);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }


        //realm.commitTransaction();
        //realm.close();

    }


    private void updateProgress() {
        images_uploaded++;
        progressDialog.setProgress(images_uploaded);

        // When all images uploaded
        if (images_uploaded == images_counter) {
            progressDialog.hide();
            new AlertDialog.Builder(SimpleAlbumImagesListActivity.this)
                    .setTitle(getString(R.string.successfully))
                    .setMessage(getString(R.string.thank_you))
                    .setIcon(R.drawable.ic_done)
            ;//.show();

            sendPost();
        }
    }

    private void sendPost() {

        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setVisibility(View.VISIBLE);
        RealmList<ImageRealm> images = project.getImages();

        String postData = "covercover=" + images.first().getUrl()
                + "&npages=" + images.size() + "&isrec=1.33";
        for (int i = 1; i < images.size(); i++) {
            int ii = i - 1;
            postData += "&page" + ii + "=" + images.get(i).getUrl();
        }
        webView.postUrl("http://rojprint.com/admin/preview/2018/preview/turnJs/samples/myalbums/index.php", postData.getBytes());

        Log.d(TAG, "sendPost: " + postData);
    }


//    void cropAllImages() {
//        Needle.onBackgroundThread().execute(new UiRelatedProgressTask<String, Integer>() {
//
//            @Override
//            protected void onProgressUpdate(Integer integer) {
//
//            }
//
//            @Override
//            protected String doWork() {
//                int result = 0;
//                for (Image image :
//                        images) {
//
//                }
//
//                return "The result is: " + result;
//            }
//
//            @Override
//            protected void thenDoUiRelatedWork(String s) {
//
//            }
//
//        });
//    }

}