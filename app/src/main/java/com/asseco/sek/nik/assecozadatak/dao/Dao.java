package com.asseco.sek.nik.assecozadatak.dao;

import android.app.Activity;
import static com.asseco.sek.nik.assecozadatak.Utils.*;

import com.asseco.sek.nik.assecozadatak.R;

/**
 *
 * Dao class that implements IDao inteface.
 *
 * Created by sekul on 21.12.2015..
 */
public class Dao implements IDao {



    IDao dbDao;
    IDao prefDao;
    Activity activity;


    /**
     * Creates new DAO.
     * @param activity activity that creates DAO.
     */
    public Dao(Activity activity) {
        this.activity = activity;
        dbDao = new DBDao(activity);
        prefDao = new SharedPreferencesDao(activity);
    }

    @Override
    public String getHash(String url) throws DaoException{
        String hash = prefDao.getHash(url);

        if(hash == null) {
            hash = dbDao.getHash(url);
        }

        return hash;
    }


    @Override
    public void storeHash(String url, String hash) throws DaoException{
        if(firstDigitEven(hash, activity)) {
            dbDao.storeHash(url, hash);
        } else {
            prefDao.storeHash(url, hash);
        }
    }


    @Override
    public void close() {
        dbDao.close();
    }
}
