package com.asseco.sek.nik.assecozadatak.dao.database;

import android.provider.BaseColumns;

/**
 * Names of tables and columns in database.
 *
 * Sql statements for creating and deleting DB.
 *
 * Created by sekul on 21.12.2015..
 */
public class EntryContract implements BaseColumns {

    public static final String TABLE_NAME = "entry";
    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_HASH = "hash";

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntryContract.TABLE_NAME + " (" +
                    EntryContract.COLUMN_NAME_URL + TEXT_TYPE + " PRIMARY KEY " + COMMA_SEP +
                    EntryContract.COLUMN_NAME_HASH + TEXT_TYPE + COMMA_SEP +
                    "UNIQUE (" + COLUMN_NAME_URL + ")" + "ON CONFLICT IGNORE" +

                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntryContract.TABLE_NAME;
}
