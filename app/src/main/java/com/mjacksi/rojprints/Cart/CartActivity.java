package com.mjacksi.rojprints.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.Utilises;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = CartActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    CartAdapter cartAdapter;
    TextView total;
    RealmResults<Project> projects;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        projects = realm.where(Project.class).equalTo("isInCart", true).findAll();

        RecyclerView recyclerView = findViewById(R.id.cart_recycler_view);

        cartAdapter = new CartAdapter(this, projects, CartActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        total = findViewById(R.id.total);
        calculateTotal();
        toolbarSetup("Cart");
    }

    private void calculateTotal() {
        int count = projects.size();
        int price = 0;
        for (Project project : projects
        ) {
            price += project.getTotalPrice();
        }
        total.setText("total(" + count + ") " + price + " IQD");
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

    public void minusAt(int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        //Project project = realm.where(Project.class).equalTo("id", id).findFirst();
        //project.decreaseCount();
        projects.get(position).decreaseCount();

        realm.commitTransaction();
        realm.close();

        cartAdapter.updateAt(position);
        calculateTotal();
    }

    public void plusAt(int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

//        Project project = realm.where(Project.class).equalTo("id", id).findFirst();
//        project.increaseCount();
        projects.get(position).increaseCount();

        realm.commitTransaction();
        realm.close();

        cartAdapter.updateAt(position);
        calculateTotal();
    }

    public void cancelProjectAt(int position) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

//        Project project = realm.where(Project.class).equalTo("id", id).findFirst();
//        project.increaseCount();
        Log.d(TAG, "cancelProjectAt: " + position);
        projects.get(position).deleteFromRealm();

        realm.commitTransaction();
        realm.close();

        calculateTotal();
        cartAdapter.setData(projects);
    }

    public void orderButton(View view) {
        if (projects.size() == 0) {
            Toast.makeText(this, "no items in cart", Toast.LENGTH_SHORT).show();
            return;
        }
        //FirebaseAuth.getInstance().signOut();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            signIn();
        } else {
            Intent i = new Intent(CartActivity.this, OrderActivity.class);
            startActivity(i);
        }
    }

    void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Failed login, retry again!", Snackbar.LENGTH_LONG)
                            .setAction("Failed", null).show();

                } else {
                    Intent i = new Intent(CartActivity.this, OrderActivity.class);
                    startActivity(i);
                }
                // ...
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView()
                        , "Failed login, retry again!", Snackbar.LENGTH_LONG)
                        .setAction("Failed", null).show();
            }
        }
    }
}
