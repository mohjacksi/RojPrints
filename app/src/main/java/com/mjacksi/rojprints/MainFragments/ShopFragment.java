package com.mjacksi.rojprints.MainFragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumBoxSize;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    ImageButton simpleAlbum, photoPrint, homeDecor;

    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);


        simpleAlbum = view.findViewById(R.id.simple_album_button);
        photoPrint = view.findViewById(R.id.photo_print_button);
        homeDecor = view.findViewById(R.id.home_decor_button);

        simpleAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "simple album", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), SimpleAlbumBoxSize.class);
                startActivity(i);
            }
        });
        photoPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "photo print", Toast.LENGTH_SHORT).show();
            }
        });
        homeDecor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "home decor", Toast.LENGTH_SHORT).show();
            }
        });


        SliderLayout mDemoSlider  = (SliderLayout)view.findViewById(R.id.slider);;

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("description1",R.drawable.bigimage1);
        file_maps.put("description2",R.drawable.bigimage2);
        file_maps.put("description3",R.drawable.bigimage3);
        file_maps.put("description4", R.drawable.bigimage4);
        file_maps.put("description5", R.drawable.bigimage5);
        file_maps.put("description6", R.drawable.bigimage6);
        file_maps.put("description7", R.drawable.bigimage7);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
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
        return view;
    }

}
