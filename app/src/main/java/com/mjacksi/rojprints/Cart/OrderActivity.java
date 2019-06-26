package com.mjacksi.rojprints.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;

import io.realm.Realm;
import io.realm.RealmResults;

public class OrderActivity extends AppCompatActivity {

    TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        total = findViewById(R.id.total_order);

        toolbarSetup("Order");
        calculateTotal();





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
        for (Project project:projects
        ) {
            price += project.getTotalPrice();
        }
        total.setText(price + " IQD");
    }
}
