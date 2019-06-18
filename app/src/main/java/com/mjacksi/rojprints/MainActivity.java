package com.mjacksi.rojprints;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import com.mjacksi.rojprints.MainFragments.ProjectsFragment;
import com.mjacksi.rojprints.MainFragments.SettingsFragment;
import com.mjacksi.rojprints.MainFragments.ShopFragment;

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
                    return true;
                case R.id.navigation_projects:
                        fm.beginTransaction().hide(active).show(projectsFragment).commit();
                        active = projectsFragment;
                    return true;
                case R.id.navigation_settings:
                    fm.beginTransaction().hide(active).show(settingsFragment).commit();
                    active = settingsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, settingsFragment, "3").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, projectsFragment, "2").hide(projectsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, shopFragment, "1").commit();

    }
}
