package com.mcet.pandemichelper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private fr1_Fragment Volounteer;
    private fr2_Fragment relief;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        setSupportActionBar(toolbar);
        Volounteer=new fr1_Fragment();
        relief=new fr2_Fragment();
        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdapter adapter= new viewPagerAdapter(getSupportFragmentManager(), 0 );
        adapter.addFragment(Volounteer,"TAB1");
        adapter.addFragment(relief,"TAB2");


        viewPager.setAdapter(adapter);
    }
    private  class viewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments=new ArrayList<>();
        private List<String> fragmenttitle=new ArrayList<>();



        public viewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            fragmenttitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

}