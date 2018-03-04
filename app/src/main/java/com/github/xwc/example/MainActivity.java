package com.github.xwc.example;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

//    ShapeView shapeView;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    private  List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"demo1", "demo2","demo3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);

        list = new ArrayList<>();
        list.add(new ExampleFragment(R.layout.fragment_example1) );
        list.add(new ExampleFragment(R.layout.fragment_example2));
        list.add(new ExampleFragment(R.layout.fragment_example3));
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        shapeView = findViewById(R.id.heartShapeView);
//        shapeView.setClickListener(new com.github.xwc.view.View.ClickListener() {
//            @Override
//            public void onClick(View var1) {
//                Toast.makeText(MainActivity.this,"点击了",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("xwc","点击了");
//            }
//        });

//        shapeView.setHeartRadian(0.5f);
//        shapeView.invalidate();
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
