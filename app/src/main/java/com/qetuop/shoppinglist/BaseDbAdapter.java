package com.qetuop.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by brian on 10/11/15.
 */
public class BaseDbAdapter {

    protected static final String LOG = "BaseDbAdapter";

    public DatabaseHelper mDatabaseHelper; // not thread safe?
    //protected static DatabaseHelper mDatabaseHelper; // more thread safe?
    protected SQLiteDatabase mDb;
    protected final Context mCtx;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "shopingList.db";

    // Table Item
    public static final String TABLE_ITEM = "item";

    // Common Columns
    public static final String COLUMN_ID = "_id"; // use BaseColumns._ID or create class that implements it? Contract class?

    // Item Table
    public static final String COLUMN_ITEM_NAME = "name";


    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    // Table Create Statements

    //  User table create statement - create first since Exercise references it?
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + TABLE_ITEM+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ITEM_NAME + " text not null "
            + ")";

    //----------------------------------------------------------------------------------------------
    //
    //                              SQLiteOpenHelper
    //
    //----------------------------------------------------------------------------------------------
    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            Log.d(LOG, "ctor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG, "onCreate");

            db.execSQL(CREATE_TABLE_ITEM);

            // requires API 16
//            db.setForeignKeyConstraintsEnabled(true); // ?finish transactions ??
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(this.getClass().getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            // create backup first?

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);

            onCreate(db);
        }
    } // DatabaseHelper
    //----------------------------------------------------------------------------------------------
    //
    //                              SQLiteOpenHelper
    //
    //----------------------------------------------------------------------------------------------


    public BaseDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public BaseDbAdapter open() throws SQLException {
        Log.d(LOG, "open");

        mDatabaseHelper = new DatabaseHelper(mCtx);
        mDb = mDatabaseHelper.getWritableDatabase();

        return this;
    }

    /*    concurrent issue?
    public SQLiteDatabase open() throws SQLException {
        Log.d(LOG, "open");

        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(mCtx);
        }

        return mDatabaseHelper.getWritableDatabase();
    }*/



    public void close() {
        mDatabaseHelper.close();
    }

} // BaseDbAdapter
