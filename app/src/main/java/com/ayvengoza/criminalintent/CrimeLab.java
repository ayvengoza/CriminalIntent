package com.ayvengoza.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Singleton, crime List keeper
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
    }

    public void add(Crime c){
        mCrimes.add(c);
    }

    public void delete(UUID id) {
        Crime crime = getCrime(id);
        int index = mCrimes.indexOf(crime);
        if(index >= 0)
            mCrimes.remove(index);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime : getCrimes()){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
