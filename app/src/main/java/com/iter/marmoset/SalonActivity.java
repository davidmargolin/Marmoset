package com.iter.marmoset;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;

public class SalonActivity extends AppCompatActivity {
    String name;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_salon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        getSupportActionBar().setTitle(name);
        ArrayList<String> slideshow = getIntent().getStringArrayListExtra("slideshow");
        Log.e("urls", slideshow.toString());
        SliderLayout sliderLayout = findViewById(R.id.promo_slider);
        for (String url: slideshow){
            TextSliderView sliderView = new TextSliderView(this);
            sliderView.image(url).setScaleType(BaseSliderView.ScaleType.Fit);
            sliderLayout.addSlider(sliderView);
        }

        TextView aboutView = findViewById(R.id.description);
        aboutView.setText(getIntent().getStringExtra("description"));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), BillingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
