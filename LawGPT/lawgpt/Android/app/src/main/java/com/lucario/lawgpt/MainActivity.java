package com.lucario.lawgpt;

import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class MainActivity extends AppCompatActivity implements MyPagerAdapter.ManageDots{
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dots_indicator);

        MyPagerAdapter adapter = new MyPagerAdapter(this, this, this);
        viewPager.setAdapter(adapter);
        // Connect the viewPager with the dotsIndicator
        dotsIndicator.attachTo(viewPager);
    }

    @Override
    public void removeDots() {
        dotsIndicator.setVisibility(View.GONE);
    }

    @Override
    public void addDots() {
        dotsIndicator.setVisibility(View.VISIBLE);
    }
}