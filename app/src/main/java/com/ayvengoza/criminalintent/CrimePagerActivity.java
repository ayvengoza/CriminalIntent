package com.ayvengoza.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Activity for swipe pages
 */

public class CrimePagerActivity extends AppCompatActivity {
    public static final String EXTRA_CRIME_ID =
            "com.ayvengoza.criminalintent.crime_id";
    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mStartPageButton;
    private Button mEndPageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mStartPageButton = (Button) findViewById(R.id.start_page_button);
        mEndPageButton = (Button) findViewById(R.id.end_page_button);

        mStartPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mEndPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size() > 0 ? mCrimes.size()-1 : 0);
            }
        });

        for(int i=0; i<mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                setButtonState(i);
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setButtonState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setButtonState(int position){
        if(position == 0){
            mStartPageButton.setVisibility(View.INVISIBLE);
        } else {
            mStartPageButton.setVisibility(View.VISIBLE);
        }
        if(position == mCrimes.size()-1){
            mEndPageButton.setVisibility(View.GONE);
        } else {
            mEndPageButton.setVisibility(View.VISIBLE);
        }
    }
}
