package com.mjacksi.rojprints.SimpleAlbum;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.mjacksi.rojprints.PhotoPrint.PhotoPrintSize;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleAlbumBoxSize extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_album_box_size);

        final ArrayList<BoxSize> boxSizes = new ArrayList<>();
        boxSizes.add(new BoxSize(1,"15 X 15 cm","25,000",R.drawable._15x15,25000));
        boxSizes.add(new BoxSize(2,"15 X 20 cm","30,000",R.drawable._15x20,30000));
        boxSizes.add(new BoxSize(3,"20 X 20 cm","40,000",R.drawable._20x20,40000));
        ListView list = findViewById(R.id.box_size_listview);
        list.setAdapter(new BoxSizesAdapter(this,boxSizes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SimpleAlbumBoxSize.this,SimpleAlbumImagesListActivity.class);
                i.putExtra("title",boxSizes.get(position).title);
                i.putExtra("price_per_page",boxSizes.get(position).pricePrePage);
                startActivity(i);
            }
        });


        // Start Slider
        List<Integer> imagesPager = new ArrayList<>();
        imagesPager.add(R.drawable.album1);
        imagesPager.add(R.drawable.album2);
        imagesPager.add(R.drawable.album3);
        imagesPager.add(R.drawable.album4);
        imagesPager.add(R.drawable.album5);
        imagesPager.add(R.drawable.album6);

        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(this,imagesPager));
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        // End Slider
//
//        for(String name : file_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit);
//            //.setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);

        toolbarSetup();
    }
    private void toolbarSetup() {
        Toolbar toolbar = findViewById(R.id.order_toolbar);
        toolbar.setTitle(getString(R.string.new_album));
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
    public class BoxSize {
        public int size;
        public String title;
        public String desc;
        public int image;
        public int pricePrePage;
        public BoxSize(int size, String title, String desc, int image, int pricePrePage) {
            this.size = size;
            this.title = title;
            this.desc = desc;
            this.image = image;
            this.pricePrePage = pricePrePage;
        }
    }
    public class BoxSizesAdapter extends ArrayAdapter<BoxSize> {
        public BoxSizesAdapter(Context context, ArrayList<BoxSize> sizes) {
            super(context, 0, sizes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            BoxSize box = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_box_size, parent, false);
            }
//            // Lookup view for data population
//            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//            TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
//            // Populate the data into the template view using the data object
//            tvTitle.setText(user.title);
//            tvDesc.setText(user.desc);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageResource(box.image);
            TextView textView = (TextView) convertView.findViewById(R.id.price);
            textView.setText(box.desc);

            // Return the completed view to render on screen
            return convertView;
        }
    }

}
