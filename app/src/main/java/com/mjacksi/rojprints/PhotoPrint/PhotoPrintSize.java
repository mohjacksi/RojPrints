package com.mjacksi.rojprints.PhotoPrint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumImagesListActivity;

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
            boxSizes.add(new ImageSize(1, 1.5f, "10 X 15 cm", "2,500 per page"));
            boxSizes.add(new ImageSize(1, 1.15f, "13 X 15 cm", "3,000 per page"));
            boxSizes.add(new ImageSize(1, 1.14f, "15 X 21 cm", "4,000 per page"));
            boxSizes.add(new ImageSize(1, 1, "5 X 5 cm", "3000 per page"));
            boxSizes.add(new ImageSize(1, 1.3f, "4.5 X 3.5 cm", "3000 per page"));
            file_maps.put("description1",R.drawable.printedimage1);
            file_maps.put("description2",R.drawable.printedimage2);
            file_maps.put("description3",R.drawable.printedimage3);

        }else{
            boxSizes.add(new ImageSize(1, 1.5f, "20 X 30 cm", "8,000 per page"));
            boxSizes.add(new ImageSize(1, 1.5f, "30 X 45 cm", "15,000 per page"));
            boxSizes.add(new ImageSize(1, 2, "30 X 60 cm", "22,000 per page"));
            boxSizes.add(new ImageSize(1, 3, "30 X 90 cm", "26,000 per page"));
            boxSizes.add(new ImageSize(1, 1.5f, "40 X 60 cm", "36,000 per page"));
            boxSizes.add(new ImageSize(1, 1.5f, "50 X 75 cm", "45,000 per page"));
            boxSizes.add(new ImageSize(1, 1.5f, "60 X 90 cm", "60,000 per page"));

            file_maps.put("description1",R.drawable.bigimage1);
            file_maps.put("description2",R.drawable.bigimage2);
            file_maps.put("description3",R.drawable.bigimage3);
            file_maps.put("description4", R.drawable.bigimage4);
            file_maps.put("description5", R.drawable.bigimage5);
            file_maps.put("description6", R.drawable.bigimage6);
            file_maps.put("description7", R.drawable.bigimage7);

        }
        ListView list = findViewById(R.id.box_size_listview);
        list.setAdapter(new BoxSizesAdapter(this,boxSizes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PhotoPrintSize.this, ChoosePhoto.class);
                i.putExtra("h",boxSizes.get(position).h);
                i.putExtra("w",boxSizes.get(position).w);
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

    public class ImageSize {
        public float h;
        public float w;
        public String title;
        public String desc;
        public ImageSize(float h, float w, String title, String desc) {
            this.h = h;
            this.w = w;
            this.title = title;
            this.desc = desc;
        }
    }
    public class BoxSizesAdapter extends ArrayAdapter<ImageSize> {
        public BoxSizesAdapter(Context context, ArrayList<ImageSize> sizes) {
            super(context, 0, sizes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ImageSize user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_box_size, parent, false);
            }
            // Lookup view for data population
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            // Populate the data into the template view using the data object
            tvTitle.setText(user.title);
            tvDesc.setText(user.desc);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
