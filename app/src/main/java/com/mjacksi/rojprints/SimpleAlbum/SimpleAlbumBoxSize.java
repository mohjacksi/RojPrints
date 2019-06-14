package com.mjacksi.rojprints.SimpleAlbum;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleAlbumBoxSize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_album_box_size);

        final ArrayList<BoxSize> boxSizes = new ArrayList<>();
        boxSizes.add(new BoxSize(1,"15 X 15 cm","2,500 per page"));
        boxSizes.add(new BoxSize(2,"15 X 20 cm","3,000 per page"));
        boxSizes.add(new BoxSize(1,"20 X 20 cm","4,000 per page"));
        ListView list = findViewById(R.id.box_size_listview);
        list.setAdapter(new BoxSizesAdapter(this,boxSizes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SimpleAlbumBoxSize.this,
                        "size " + boxSizes.get(position).size
                        , Toast.LENGTH_SHORT).show();

                // Todo:
                Intent i = new Intent(SimpleAlbumBoxSize.this,SimpleAlbumImagesListActivity.class);
                i.putExtra("size",boxSizes.get(position).size);
                startActivity(i);
            }
        });

        SliderLayout mDemoSlider  = (SliderLayout)findViewById(R.id.slider);;

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("description1",R.drawable.bigimage1);
        file_maps.put("description2",R.drawable.bigimage2);
        file_maps.put("description3",R.drawable.bigimage3);
        file_maps.put("description4", R.drawable.bigimage4);
        file_maps.put("description5", R.drawable.bigimage5);
        file_maps.put("description6", R.drawable.bigimage6);
        file_maps.put("description7", R.drawable.bigimage7);

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

    public class BoxSize {
        public int size;
        public String title;
        public String desc;

        public BoxSize(int size, String title, String desc) {
            this.size = size;
            this.title = title;
            this.desc = desc;
        }
    }
    public class BoxSizesAdapter extends ArrayAdapter<BoxSize> {
        public BoxSizesAdapter(Context context, ArrayList<BoxSize> sizes) {
            super(context, 0, sizes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            BoxSize user = getItem(position);
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
