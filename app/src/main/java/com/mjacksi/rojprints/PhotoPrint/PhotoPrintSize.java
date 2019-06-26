package com.mjacksi.rojprints.PhotoPrint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumImagesListActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoPrintSize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_print_size);

        final ArrayList<ImageSize> boxSizes = new ArrayList<>();

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();


        if(getIntent().getStringExtra("type").equals("photo_print")) {
            toolbarSetup(getString(R.string.photo_print_titile));
            boxSizes.add(new ImageSize(1, 1.5f, "10 X 15 cm", "2,500 per page",R.drawable.photo_print_10x15,2500));
            boxSizes.add(new ImageSize(1, 1.14f, "15 X 21 cm", "4,000 per page",R.drawable.photo_print_15x21,4000));
            boxSizes.add(new ImageSize(1, 1.38f, "13 X 18 cm", "3,000 per page",R.drawable.photo_print_13x18,3000));
            boxSizes.add(new ImageSize(1, 1.3f, "4.5 X 3.5 cm", "300 per page",R.drawable.photos4,300));
            boxSizes.add(new ImageSize(1, 1.3f, "4.5 X 3.5 cm", "300 per page",R.drawable.photos6,300));
            boxSizes.add(new ImageSize(1, 1, "5 X 5 cm", "300 per page",R.drawable.photos6_5x5,300));
            boxSizes.add(new ImageSize(1, 1.3f, "4.5 X 3.5 cm", "300 per page",R.drawable.photos6,300));
            boxSizes.add(new ImageSize(1, 1.3f, "4.5 X 3.5 cm", "300 per page",R.drawable.photos8,300));
            file_maps.put("1",R.drawable.photo_print1);
            file_maps.put("2",R.drawable.photo_print2);
            file_maps.put("3",R.drawable.photo_print3);
            file_maps.put("4",R.drawable.photo_print4);
            file_maps.put("5",R.drawable.photo_print5);

        }else{
            TextView description = findViewById(R.id.description);
            description.setText(getString(R.string.home_decor_desc));
            toolbarSetup(getString(R.string.hode_decor_title));
            boxSizes.add(new ImageSize(1, 1.5f, "20 X 30 cm", "8,000 per page",R.drawable._20x30,8000));
            boxSizes.add(new ImageSize(1, 1.5f, "30 X 45 cm", "15,000 per page",R.drawable._30x45,15000));
            boxSizes.add(new ImageSize(1, 2, "30 X 60 cm", "22,000 per page",R.drawable._30x60,22000));
            boxSizes.add(new ImageSize(1, 3, "30 X 90 cm", "26,000 per page",R.drawable._30x90,26000));
            boxSizes.add(new ImageSize(1, 1.5f, "40 X 60 cm", "36,000 per page",R.drawable._40x60,36000));
            boxSizes.add(new ImageSize(1, 1.5f, "50 X 75 cm", "45,000 per page",R.drawable._50x75,45000));
            boxSizes.add(new ImageSize(1, 1.5f, "60 X 90 cm", "60,000 per page",R.drawable._60x90,6000));

            file_maps.put("1",R.drawable.bigimage1);
            file_maps.put("2",R.drawable.bigimage2);
            file_maps.put("3",R.drawable.bigimage3);
            file_maps.put("4", R.drawable.bigimage4);
            file_maps.put("5", R.drawable.bigimage5);
            file_maps.put("6", R.drawable.bigimage6);
            file_maps.put("7", R.drawable.bigimage7);

        }
        ListView list = findViewById(R.id.box_size_listview);
        list.setAdapter(new BoxSizesAdapter(this,boxSizes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PhotoPrintSize.this, ChoosePhoto.class);
                i.putExtra("h",boxSizes.get(position).h);
                i.putExtra("w",boxSizes.get(position).w);
                i.putExtra("title",boxSizes.get(position).title);
                i.putExtra("price",boxSizes.get(position).price);
                startActivity(i);
            }
        });

        SliderLayout mDemoSlider  = (SliderLayout)findViewById(R.id.slider);;


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
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

    public class ImageSize {
        public float h;
        public float w;
        public String title;
        public String desc;
        public int image;
        public int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public ImageSize(float h, float w, String title, String desc, int image, int price) {
            this.h = h;
            this.w = w;
            this.title = title;
            this.desc = desc;
            this.image = image;
            this.price = price;
        }
    }
    public class BoxSizesAdapter extends ArrayAdapter<ImageSize> {
        public BoxSizesAdapter(Context context, ArrayList<ImageSize> sizes) {
            super(context, 0, sizes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ImageSize box = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_box_size, parent, false);
            }
            // Lookup view for data population
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
