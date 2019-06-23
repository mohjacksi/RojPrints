package com.mjacksi.rojprints;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Realm.init(this);
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                if (user == null) {
//                    intent = new Intent(SplashActivity.this, MainActivity.class);
//                }else{
//                    intent = new Intent(SplashActivity.this, OrdersActivity.class);
//                }
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

}
