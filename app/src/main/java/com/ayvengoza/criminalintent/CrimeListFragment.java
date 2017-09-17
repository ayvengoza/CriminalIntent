package com.ayvengoza.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;


public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle_visible";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE, false);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleView = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleView.setTitle(R.string.hide_subtitle);
        } else {
            subtitleView.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).add(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        int crimeCount = CrimeLab.get(getActivity()).getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private abstract class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected Crime mCrime;
        public CrimeHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }
        public abstract void bind(Crime crime);

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class UsualCrimeHolder extends CrimeHolder{
        private static final String dateFormatStr = "EEEE, dd MMMM, yyyy";
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        public UsualCrimeHolder(View view) {
            super(view);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        @Override
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            String date = DateFormat.format(dateFormatStr, mCrime.getDate()).toString();
            mDateTextView.setText(date);
            mSolvedImageView.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    private class PoliceCrimeHolder extends CrimeHolder{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mCallPoliceButton;
        public PoliceCrimeHolder(View view) {
            super(view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mCallPoliceButton = (Button) itemView.findViewById(R.id.call_police_button);
            mCallPoliceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Police called!",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mCallPoliceButton.setEnabled(mCrime.isRequiresPolice());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        public static final int USUAL_ITEM = 0;
        public static final int CALL_POLICE_ITEM = 1;
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType){
                case USUAL_ITEM:
                    return new UsualCrimeHolder(layoutInflater.inflate(R.layout.list_item_crime, parent, false));
                case CALL_POLICE_ITEM:
                    return new PoliceCrimeHolder(layoutInflater.inflate(R.layout.list_item_police_crime, parent, false));
                default:
                    return new UsualCrimeHolder(layoutInflater.inflate(R.layout.list_item_crime, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            boolean isRequiresPolice = mCrimes.get(position).isRequiresPolice();
            if(isRequiresPolice){
                return CALL_POLICE_ITEM;
            } else {
                return USUAL_ITEM;
            }
        }

    }
}
