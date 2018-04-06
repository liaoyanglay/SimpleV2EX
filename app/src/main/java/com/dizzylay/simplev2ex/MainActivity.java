package com.dizzylay.simplev2ex;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Fragment> fragments;
    android.support.v4.app.FragmentManager fragmentManager;
    ViewPager viewPager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        initFragment();
        initViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LatestFragment());
        fragments.add(new HotFragment());
        fragmentManager = getSupportFragmentManager();
    }

    private void initViewPager() {
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(fragmentManager,fragments);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

    }
}
