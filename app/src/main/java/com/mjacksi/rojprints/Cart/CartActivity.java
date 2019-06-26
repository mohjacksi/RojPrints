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

import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.Utilises;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CartActivity extends AppCompatActivity {
    CartAdapter cartAdapter;
    TextView total;
    RealmResults<Project> projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Realm realm = Realm.getDefaultInstance();
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
        for (Project project:projects
             ) {
            price += project.getTotalPrice();
        }
        total.setText( "total("+count+") " + price + " IQD");
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
        projects.get(position).deleteFromRealm();

        realm.commitTransaction();
        realm.close();

        calculateTotal();
    }

    public void orderButton(View view) {
        Intent i = new Intent(CartActivity.this,OrderActivity.class);
        startActivity(i);
    }
}
