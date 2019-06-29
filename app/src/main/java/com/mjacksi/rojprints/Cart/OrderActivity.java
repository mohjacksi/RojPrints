package com.mjacksi.rojprints.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.ImageRealm;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.InternetConnection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = OrderActivity.class.getSimpleName();
    TextView phoneTv, nameTv, addressTv, notesTv, totalTv;
    Spinner citySp;
    Switch giftSw, deliverySw;
    String phone, name, address, notes, total, city, gift, delivery;
    private StorageReference mStorageRef;

    RealmResults<Project> projects;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        phoneTv = findViewById(R.id.order_phone_number);
        nameTv = findViewById(R.id.order_name);
        addressTv = findViewById(R.id.order_address);
        notesTv = findViewById(R.id.order_notes);
        totalTv = findViewById(R.id.order_total);
        citySp = findViewById(R.id.order_city);
        giftSw = findViewById(R.id.order_gift_switch);
        deliverySw = findViewById(R.id.order_delivery_switch);


        toolbarSetup("Order");
        calculateTotal();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Mj");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
        Realm realm = Realm.getDefaultInstance();
        projects = realm.where(Project.class).equalTo("isInCart", true).findAll();


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("In progress...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setIcon(R.drawable.ic_done);
        progressDialog.setCancelable(false);


    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
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


    private void calculateTotal() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Project> projects = realm.where(Project.class).equalTo("isInCart", true).findAll();

        int count = projects.size();
        int price = 0;
        for (Project project : projects
        ) {
            price += project.getTotalPrice();
        }
        totalTv.setText(price + " IQD");
    }

    public void order(View view) {
        if (!InternetConnection.checkConnection(this)) {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return;
        }


        phone = phoneTv.getText().toString();
        name = nameTv.getText().toString();
        address = addressTv.getText().toString();
        notes = notesTv.getText().toString();
        total = totalTv.getText().toString();
        city = String.valueOf(citySp.getSelectedItem());
        gift = String.valueOf(giftSw.isChecked());
        delivery = String.valueOf(deliverySw.isChecked());


        if (phone.isEmpty() || name.isEmpty() || address.isEmpty()
                || notes.isEmpty() || total.isEmpty() || city.isEmpty()
                || gift.isEmpty() || delivery.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
            return;
        }



        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInAnonymously();
        }
        uploadImages();
    }


    public void sendPost() {

        Realm realm = Realm.getDefaultInstance();
        Gson gson = new Gson();




        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);

            Project project1 = realm.copyFromRealm(project); //detach from Realm, copy values to fields
            String json = gson.toJson(project1);
            Log.d(TAG, "sendPost: " + json);

            RealmList<ImageRealm> images = project.getImages();
            for (int j = 0; j < images.size(); j++) {
                ImageRealm image = images.get(j);


            }
        }


        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("AuthKey", "authkeyavrazauthkey29")
                .add("NotificationId", "") // TODO ?
                .add("PhoneNumber", phone)
                .add("UserName", name)
                .add("Address", address)
                .add("City", city)
                .add("isDelivery",delivery)
                .add("friendPhone", "") // TODO ?
                .add("isGift", gift)
                .add("Payment", "pay when receive") // TODO !
                .add("Note", notes)
                .add("TotalePrice", total)
                .add("cards", "") // TODO
                .build();
        Request request = new Request.Builder()
                .url("http://rojprint.com/admin/ajax/ajax_orders.php")
                .post(formBody)
                .build();

            // TODO
//        try {
//             Response response = client.newCall(request).execute();
//
//            // Do something with the response.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //TODO: delete all projects after upload

    }

    List<ImageRealm> images = new ArrayList<>();
    int images_counter, images_uploaded;

    public void uploadImages() {


//        Project project = realm.where(Project.class).equalTo("id", id).findFirst();
//        project.increaseCount();

        images_counter = 0;
        images_uploaded = 0;
        for (int i = 0; i < projects.size(); i++) {
            RealmList<ImageRealm> images = projects.get(i).getImages();
            for (int j = 0; j < images.size(); j++) {
                images_counter++;
            }
        }

        progressDialog.setMax(images_counter);
        progressDialog.show();


        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            RealmList<ImageRealm> images = project.getImages();
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
            new AlertDialog.Builder(OrderActivity.this)
                    .setTitle(getString(R.string.successfully))
                    .setMessage(getString(R.string.thank_you))
                    .setIcon(R.drawable.ic_done)
                    .show();

            sendPost();
        }
    }
}
