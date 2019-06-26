package com.mjacksi.rojprints.MainFragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mjacksi.rojprints.MainActivity;
import com.mjacksi.rojprints.Models.Setting;
import com.mjacksi.rojprints.PhotoPrint.ChoosePhoto;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.SettingsActivities.AboutActivity;
import com.mjacksi.rojprints.SettingsActivities.MapActivity;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumImagesListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        LinearLayout copyRights = view.findViewById(R.id.copy_rights);

        copyRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOpenWebPage("https://www.smartangle.net/");
            }
        });


        final ArrayList settings = new ArrayList();
        settings.add(new Setting(R.drawable.phone, getContext().getString(R.string.call_us)));
        settings.add(new Setting(R.drawable.language, getContext().getString(R.string.language)));
        settings.add(new Setting(R.drawable.about, getContext().getString(R.string.about)));
        settings.add(new Setting(R.drawable.email, getContext().getString(R.string.contact_us)));
        settings.add(new Setting(R.drawable.rate, getContext().getString(R.string.rate_us)));
        settings.add(new Setting(R.drawable.location, getContext().getString(R.string.our_address)));


        final ListView list = view.findViewById(R.id.settings_listview);
        SettingsAdapter adapter;
        adapter = new SettingsAdapter(getContext(), settings);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:07507162442"));
                        Dexter.withActivity(getActivity())
                                .withPermission(Manifest.permission.CALL_PHONE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        startActivity(callIntent);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                                }).check();
                        break;
                    case 1:
                        final CharSequence[] items = {"English", "عربي", "كوردى"};
                        //"Write on image", "Filters",

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getContext().getString(R.string.chose_language));
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                switch (item){
                                    case 0:
                                        setLocale("en");
                                        break;
                                    case 1:
                                        setLocale("ar");
                                        break;
                                    case 2:
                                        setLocale("ku");
                                                                                break;

                                }
                            }
                        }).show();
                        break;
                    case 2:
                            Intent intent = new Intent(getActivity(), AboutActivity.class);
                            startActivity(intent);
                        break;
                    case 3:
                        String url = "http://www.rojprint.com";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 4:
                        rateUs();
                        break;
                    case 5:
                        String lat = "36.8619654", lng = "42.9907304", mTitle = "Roj print";
                        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + mTitle + ")";
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                        startActivity(mapIntent);
                }
            }
        });

        return view;
    }

    public class SettingsAdapter extends ArrayAdapter<Setting> {
        public SettingsAdapter(Context context, ArrayList<Setting> settings) {
            super(context, 0, settings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Setting setting = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_setting, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.setting_icon);
            Glide.with(getContext())
                    .load(setting.getIcon())
                    .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(imageView);
            TextView title = convertView.findViewById(R.id.settings_title);
            title.setText(setting.getTitle());


            // Return the completed view to render on screen
            return convertView;
        }

        public void swapItems(List<Project> items) {

            notifyDataSetChanged();
        }
    }


    public boolean startOpenWebPage(String url) {
        boolean result = false;
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            startActivity(intent);
            result = true;
        } catch (Exception e) {
            if (url.startsWith("http://")) {
                startOpenWebPage(url.replace("http://", "https://"));
            }
        }
        return result;
    }

    void rateUs() {
        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

    public void setLocale(String lang) {

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor e = getPrefs.edit();
        e.putString("language", lang);
        e.apply();

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        getActivity().finish();
    }

}
