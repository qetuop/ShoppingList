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

    protected static final String TAG = "BaseDbAdapter";

    public DatabaseHelper mDatabaseHelper; // not thread safe?
    //protected static DatabaseHelper mDatabaseHelper; // more thread safe?
    protected SQLiteDatabase mDb;
    protected final Context mCtx;

    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "shopingList.db";

    // Table Names
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_STORE = "store";
    public static final String TABLE_AISLE = "aisle";

    // Columns
    public static final String COLUMN_ID = "_id"; // use BaseColumns._ID or create class that implements it? Contract class?

    // Item Table Columns
    public static final String COLUMN_ITEM_NAME = "name";

    // Store Table Columns
    public static final String COLUMN_STORE_NAME = "name";

    // Aisle Table Columns
    public static final String COLUMN_AISLE_STORE_ID = "store_id";
    public static final String COLUMN_ITEM_STORE_ID = "item_id";

    //
    //private static final String TEXT_TYPE = " TEXT";
    //private static final String COMMA_SEP = ",";

    // Table Create Statements

    //  User table create statement - create first since Exercise references it?
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE "
            + TABLE_ITEM+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ITEM_NAME + " text not null "
            + ")";

    private static final String CREATE_TABLE_STORE = "CREATE TABLE "
            + TABLE_STORE+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_STORE_NAME + " text not null "
            + ")";

    private static final String CREATE_TABLE_AISLE = "CREATE TABLE "
            + TABLE_AISLE+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_AISLE_STORE_ID + " integer not null,"
            + COLUMN_ITEM_STORE_ID + " integer not null,"
            + " FOREIGN KEY ("+COLUMN_AISLE_STORE_ID+") REFERENCES "+TABLE_STORE+"("+COLUMN_ID+")  ON DELETE CASCADE, "
            + " FOREIGN KEY ("+COLUMN_ITEM_STORE_ID+")  REFERENCES "+TABLE_ITEM+"("+COLUMN_ID+")  ON DELETE CASCADE "

            + ")";

    //----------------------------------------------------------------------------------------------
    //
    //                              SQLiteOpenHelper
    //
    //----------------------------------------------------------------------------------------------
    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            Log.d(TAG, "ctor");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate");

            db.execSQL(CREATE_TABLE_ITEM);
            db.execSQL(CREATE_TABLE_STORE);
            db.execSQL(CREATE_TABLE_AISLE);

            // requires min API 16 - probably using min 15
            //db.setForeignKeyConstraintsEnabled(true); // ?finish transactions ??
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpgrade");

            Log.w(this.getClass().getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            // create backup first?

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AISLE);

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
        Log.d(TAG, "open");

        mDatabaseHelper = new DatabaseHelper(mCtx);
        mDb = mDatabaseHelper.getWritableDatabase();

        // Enable foreign key constraints
        if (!mDb.isReadOnly()) {
            mDb.execSQL("PRAGMA foreign_keys = ON;");
        }

        return this;
    }

    /*    concurrent issue?
    public SQLiteDatabase open() throws SQLException {
        Log.d(TAG, "open");

        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(mCtx);
        }

        return mDatabaseHelper.getWritableDatabase();
    }*/



    public void close() {
        mDatabaseHelper.close();
    }

} // BaseDbAdapter
