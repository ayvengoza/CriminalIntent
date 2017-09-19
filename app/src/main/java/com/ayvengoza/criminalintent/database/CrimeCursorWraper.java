package com.ayvengoza.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ayvengoza.criminalintent.Crime;
import com.ayvengoza.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ang on 19.09.17.
 */

public class CrimeCursorWraper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWraper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }
}
