package com.ayvengoza.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
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
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class UsualCrimeHolder extends CrimeHolder{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        public UsualCrimeHolder(View view) {
            super(view);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
        }

        @Override
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
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
