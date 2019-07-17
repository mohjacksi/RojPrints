package com.mjacksi.rojprints.MainFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.mjacksi.rojprints.PhotoPrint.PhotoPrintSize;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumBoxSize;
import com.mjacksi.rojprints.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    ImageButton simpleAlbum, photoPrint, homeDecor;

    RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);


    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        animation.setFillAfter(true);
        animation.setDuration(250);

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Start Slider

        List<Integer> imagesPager = new ArrayList<>();
        imagesPager.add(R.drawable.slidshow1);
        imagesPager.add(R.drawable.slidshow2);
        imagesPager.add(R.drawable.slidshow3);
        imagesPager.add(R.drawable.slidshow4);

        SliderView sliderView = view.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(getContext(),imagesPager));
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        // End Slider

        simpleAlbum = view.findViewById(R.id.simple_album_button);
        photoPrint = view.findViewById(R.id.photo_print_button);
        homeDecor = view.findViewById(R.id.home_decor_button);

        simpleAlbum.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), SimpleAlbumBoxSize.class);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startActivity(i);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            v.startAnimation(animation);

        });
        photoPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PhotoPrintSize.class);
                i.putExtra("type", "photo_print");
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(i);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                v.startAnimation(animation);
            }
        });
        homeDecor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PhotoPrintSize.class);
                i.putExtra("type", "home_decor");
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(i);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                v.startAnimation(animation);

            }
        });

        return view;
    }

}
