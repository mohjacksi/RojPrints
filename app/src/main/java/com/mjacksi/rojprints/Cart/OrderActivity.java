package com.mjacksi.rojprints.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.Utilises.InternetConnection;

import io.realm.Realm;
import io.realm.RealmResults;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = OrderActivity.class.getSimpleName();
    TextView phoneTv, nameTv, addressTv, notesTv, totalTv;
    Spinner citySp;
    Switch giftSw, deliverySw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        phoneTv = findViewById(R.id.order_phone_number);
        nameTv = findViewById(R.id.order_name);
        addressTv = findViewById(R.id.order_address);
        notesTv = findViewById(R.id.order_notes);
        totalTv = findViewById(R.id.order_total);
        citySp =  findViewById(R.id.order_city);
        giftSw = findViewById(R.id.order_gift_switch);
        deliverySw = findViewById(R.id.order_delivery_switch);


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
        for (Project project : projects
        ) {
            price += project.getTotalPrice();
        }
        totalTv.setText(price + " IQD");
    }

    public void order(View view) {
        if(!InternetConnection.checkConnection(this)){
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            return;
        }

        sendPost();

    }


    public void sendPost(){
        String phone, name, address, notes, total, city, gift, delivery;
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
                || gift.isEmpty() || delivery.isEmpty()){
            Toast.makeText(this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();
            return;
        }



        Log.d(TAG, "sendPost: " + phone + name + address + notes + total + city + gift + delivery);
    }

    public void uploadImages(){

    }
}
