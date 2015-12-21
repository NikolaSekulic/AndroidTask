package com.asseco.sek.nik.assecozadatak.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asseco.sek.nik.assecozadatak.dao.database.DBHelper;
import com.asseco.sek.nik.assecozadatak.dao.database.EntryContract;

/**
 *
 * Implementation of DAO for database.
 *
 * Created by sekul on 21.12.2015..
 */
public class DBDao implements IDao {


    DBHelper dbHelper;
    SQLiteDatabase db;

    /**
     * Constructor.
     * @param context application context
     */
    public DBDao(Context context) {
        dbHelper = new DBHelper(context);

        db = dbHelper.getWritableDatabase();
    }


    @Override
    public void storeHash(String url, String hash) {

        ContentValues values = new ContentValues();
        values.put(EntryContract.COLUMN_NAME_URL, url);
        values.put(EntryContract.COLUMN_NAME_HASH, hash);

        db.insert(EntryContract.TABLE_NAME, null, values);

    }


    @Override
    public String getHash(String url) {

        String[] projection = {EntryContract.COLUMN_NAME_HASH};

        String sql = "select " + EntryContract.COLUMN_NAME_HASH + " from " +
                EntryContract.TABLE_NAME  + " where " +
                EntryContract.COLUMN_NAME_URL + "=?";


        Cursor res = db.rawQuery(sql, new String[]{url});


        if (res.getCount() == 0) {
            return null;
        }

        res.moveToFirst();
        String hash = res.getString(res.getColumnIndex(EntryContract.COLUMN_NAME_HASH));

        return hash;
    }


    @Override
    public void close() {
        db.close();
    }
}
