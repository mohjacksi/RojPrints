package com.mjacksi.rojprints;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mjacksi.rojprints.Cart.CartActivity;
import com.mjacksi.rojprints.MainFragments.ProjectsFragment;
import com.mjacksi.rojprints.MainFragments.SettingsFragment;
import com.mjacksi.rojprints.MainFragments.ShopFragment;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    final Fragment shopFragment = new ShopFragment();
    final Fragment projectsFragment = new ProjectsFragment();
    final Fragment settingsFragment = new SettingsFragment();
    Fragment active = shopFragment;
    final FragmentManager fm = getSupportFragmentManager();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    fm.beginTransaction().hide(active).show(shopFragment).commit();
                    active = shopFragment;
                    toolbar.setTitle(getString(R.string.rojprint_shop));
                    return true;
                case R.id.navigation_projects:
                    fm.beginTransaction().hide(active).show(projectsFragment).commit();
                    active = projectsFragment;
                    toolbar.setTitle(getString(R.string.rojprint_projects));
                    return true;
                case R.id.navigation_settings:
                    fm.beginTransaction().hide(active).show(settingsFragment).commit();
                    active = settingsFragment;
                    toolbar.setTitle(getString(R.string.rojprint_settings));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);



        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, settingsFragment, "3").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, projectsFragment, "2").hide(projectsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, shopFragment, "1").commit();
        toolbarSetup();
    }

    Toolbar toolbar;
    private void toolbarSetup() {
        toolbar = findViewById(R.id.order_toolbar);
        toolbar.setTitle(getString(R.string.rojprint_shop));
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if user select more than 14


        int id = item.getItemId();
        if (id == R.id.go_to_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
