package com.alchemist.ssa.StartingStuffs;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alchemist.ssa.R;
import com.alchemist.ssa.ScheduleStuffs.Schedule;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private IntroPageAdapter introPageAdapter;
    private Button skipBtn,nextBtn;
    private int slideLayouts[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        slideLayouts=new int[]{R.layout.intro_slide1,R.layout.intro_slide2};
        skipBtn=findViewById(R.id.skipSlide);
        nextBtn=findViewById(R.id.nextSlide);
        viewPager=findViewById(R.id.introViewPager);

        introPageAdapter=new IntroPageAdapter();
        viewPager.setAdapter(introPageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==(slideLayouts.length-1)){
                    nextBtn.setText("Got it");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCurrentSlide()<slideLayouts.length){
                    //if the slide is still present
                    if(getCurrentSlide()==(slideLayouts.length-1)){
                        //if the slide is last one
                        nextBtn.setText("Got it");

                    }
                    viewPager.setCurrentItem(getCurrentSlide());
                }
                else{
                    startActivity(new Intent(getApplicationContext(),Schedule.class));

                }
            }
        });


        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Schedule.class));
            }
        });

    }
    private int getCurrentSlide() {
        return viewPager.getCurrentItem()+1;
    }


    public class IntroPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return slideLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        //Inner adapter class for the viewpager

            public IntroPageAdapter() {

            }



            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(slideLayouts[position], container, false);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        }

}
