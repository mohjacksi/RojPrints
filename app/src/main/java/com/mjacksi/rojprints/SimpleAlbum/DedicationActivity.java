package com.mjacksi.rojprints.SimpleAlbum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mjacksi.rojprints.MainActivity;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.ImageRealm;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.Utilises;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.UUID;

import github.nisrulz.screenshott.ScreenShott;
import io.realm.Realm;
import io.realm.RealmList;

public class DedicationActivity extends AppCompatActivity {
    private static final String TAG = DedicationActivity.class.getSimpleName();
    Project project;
    ConstraintLayout screenshot;

    int images_counter = 0;
    int images_uploaded = 0;

    ProgressDialog progressDialog;
    boolean hasFooter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedication);
        toolbarSetup("Dedication");

        Realm realm = Realm.getDefaultInstance();
        String id = getIntent().getExtras().getString("album_id");
        project = realm.where(Project.class).equalTo("id", id).findFirst();

        ConstraintLayout constraintLayout = findViewById(R.id.dedication_cons);
        screenshot = findViewById(R.id.footer_layout);

        SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        int color = prefs.getInt("color", R.color.colorPrimary);
        screenshot.setBackgroundColor(color);

        if (!project.isSquare()) {
            ConstraintSet set = new ConstraintSet();
            set.clone(constraintLayout);
            set.setDimensionRatio(screenshot.getId(), String.format("H,%f:%f", 1.0f, 1.3f));
            set.applyTo(constraintLayout);
        }

        final TextView textView = findViewById(R.id.dedication_tv);
        EditText editText = findViewById(R.id.dedicationEditText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s.toString());
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_done);
        progressDialog.setCancelable(false);

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


    public void preview(View view) {
        if (!hasFooter) {
            saveAlbum();
            uploadImages(project);
        }

    }

    private void saveAlbum() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        String footer_path = screenshot();
        if (footer_path != null) {
            RealmList<ImageRealm> images = project.getImages();
            String name = UUID.randomUUID().toString();
            ImageRealm image = new ImageRealm(name, footer_path);
            image.setUrl("");
            images.add(image);
            //project.setImages(images);
            realm.commitTransaction();
            realm.close();

        }
    }


    String screenshot() {
        Bitmap bitmap = ScreenShott.getInstance().takeScreenShotOfView(screenshot);
        File file = null;
        try {
            file = ScreenShott.getInstance().saveScreenshotToPicturesFolder(this, bitmap, "");
            String bitmapFilePath = file.getAbsolutePath();
            return bitmapFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadImages(Project project) {
        images_counter = 0;
        images_uploaded = 0;
        RealmList<ImageRealm> images = project.getImages();

        for (int j = 0; j < images.size(); j++) {
            images_counter++;
        }

        Log.d(TAG, "uploadImages: " + images_counter);


        progressDialog.setMax(images_counter);
        progressDialog.show();


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
            new AlertDialog.Builder(DedicationActivity.this)
                    .setTitle(getString(R.string.successfully))
                    .setMessage(getString(R.string.thank_you))
                    .setIcon(R.drawable.ic_done)
            ;//.show();

            preview();
        }
    }

    private void preview() {
        Intent intent = new Intent(DedicationActivity.this, PreviewActivity.class);
        intent.putExtra("album_id", project.getId());
        startActivity(intent);
    }


    public void addToCart(View view) {
        if (!hasFooter) {
            saveAlbum();
        }
        Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        project.setInCart(true);
        realm.commitTransaction();
        realm.close();
        Intent intent = new Intent(DedicationActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
