package io.github.ytam.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.ytam.R;
import io.github.ytam.ui.fragment.PopulerMoviesFragment;
import io.github.ytam.ui.fragment.PopulerTvSeriesFragment;
import io.github.ytam.ui.fragment.TopRatedMoviesFragment;

public class Home extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PopulerMoviesFragment(), "Populer");
        adapter.addFragment(new TopRatedMoviesFragment(), "Top Rated");
        adapter.addFragment(new PopulerTvSeriesFragment(), "Tv Series");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    }
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {

            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(this, "Çıkmak için bir kez daha basınız", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce=false;
            }

        }, 2000);
    }

}