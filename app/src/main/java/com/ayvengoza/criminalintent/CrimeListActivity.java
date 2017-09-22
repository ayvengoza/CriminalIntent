package com.ayvengoza.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by ayven on 09.09.2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
