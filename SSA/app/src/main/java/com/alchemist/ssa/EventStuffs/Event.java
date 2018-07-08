package com.alchemist.ssa.EventStuffs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

public class Event extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    CalendarView simpleCalendarView;
    //started at 2018/07/08
    // set red color for the week number

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        //tabLayout.setTabTextColors(R.color.tabColor,R.color.changeTabColor);
        tabLayout.setupWithViewPager(viewPager,false);
        simpleCalendarView= (CalendarView) findViewById(R.id.calendarView);
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(),month+dayOfMonth+"",Toast.LENGTH_SHORT).show();
            }
        });
//        /simpleCalendarView.setWeekNumberColor(Color.RED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            simpleCalendarView.setWeekDayTextAppearance(Color.RED);
        }

    }

    private void setupViewPager(ViewPager viewPager) {

        //Fragment call

        ViewPageAdapter viewPageAdapter=new ViewPageAdapter(getSupportFragmentManager());
        HolidayEventFragment holidayEventFragment=new HolidayEventFragment();
        NormalEventFragment normalEventFragment=new NormalEventFragment();

        viewPageAdapter.addFragment(normalEventFragment,"Normal");
        viewPageAdapter.addFragment(holidayEventFragment,"Holiday");

        viewPager.setAdapter(viewPageAdapter);
    }

    private class ViewPageAdapter extends FragmentPagerAdapter{
        List<Fragment> fragmentList=new ArrayList<>();
        List<String> stringList=new ArrayList<>();


        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();

        }
        public void addFragment(Fragment fragment,String title){
            fragmentList.add(fragment);
            stringList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }
    }

}
