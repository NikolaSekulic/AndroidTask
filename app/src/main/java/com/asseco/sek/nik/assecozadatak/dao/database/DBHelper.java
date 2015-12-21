package com.asseco.sek.nik.assecozadatak.dao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.asseco.sek.nik.assecozadatak.dao.database.EntryContract.*;

/**
 * Created by sekul on 21.12.2015..
 */
public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    /**
     * Databse name
     */
    public static final String DATABASE_NAME = "AssecoTask.db";

    /**
     * Creates new DB helper.
     *
     * @param context application context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method that is called when DB is created. Executes sql for  creating tables.
     *
     * @param db database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Method that is called when DB is upgraded. Executes sql that deletes tables.
     *
     * @param db database
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * Method that is called when DB is donwgraded. Same as method upgraded.
     *
     * @param db database
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
