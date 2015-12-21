package com.asseco.sek.nik.assecozadatak.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Implementation of DAO interface that stores values in Shared Preferences.
 * Created by sekul on 21.12.2015..
 */
public class SharedPreferencesDao implements IDao {


    SharedPreferences sharedPref;


    /**
     * Constructor.
     *
     * @param activity ativity that uses DAO.
     */
    public SharedPreferencesDao(Activity activity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void storeHash(String url, String hash) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(url, hash);
        editor.commit();
    }




    @Override
    public String getHash(String url) {
        return sharedPref.getString(url, null);
    }


    @Override
    public void close() {
    }
}
