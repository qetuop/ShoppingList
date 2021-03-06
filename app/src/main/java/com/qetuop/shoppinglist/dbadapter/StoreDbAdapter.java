package com.qetuop.shoppinglist.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.qetuop.shoppinglist.pojo.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 4/23/16.
 */
public class StoreDbAdapter extends BaseDbAdapter
{
    private String[] projection = {
            COLUMN_ID,
            COLUMN_STORE_NAME
    };

    public StoreDbAdapter(Context ctx) {
        super(ctx);
    }

    public long insert(Store value) {
        long id = 0;

        ContentValues args = new ContentValues();

        args.put(COLUMN_STORE_NAME, value.getName());

        id = mDb.insert(TABLE_STORE, null, args);
        value.setId(id);

        return id;
    }

    // convert a "ptr/cursor" of a table entry to an object
    public Store cursorToObject(Cursor cursor) {
        Store obj = new Store();

        obj.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // why can't get?
        obj.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STORE_NAME)));

        return obj;
    }

    private Store get(String selection, String[] selectionArgs) {
        Store obj = new Store();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;

        Cursor cursor = mDb.query(
                TABLE_STORE,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        if ( cursor.moveToFirst() == true ) {// not empty
            obj = cursorToObject(cursor);
        }

        cursor.close();

        return obj;
    }

    // READ
    public Store getId(long id) {
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        return get(selection, selectionArgs);
    }

    public Store getName(String name) {
        String selection = COLUMN_STORE_NAME + "=?";
        String[] selectionArgs = {name};

        return get(selection, selectionArgs);
    }

    public List<Store> getAll() {
        List<Store> objs = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_STORE,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Store obj = cursorToObject(cursor);
            objs.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return objs;
    }

    public Cursor getAllCursor() {

        Cursor cursor = mDb.query(
                TABLE_STORE,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();

        return cursor;
    }

    // UPDATE
    public void update(long id, Store obj) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_STORE_NAME, obj.getName());

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_STORE,
                values,
                selection,
                selectionArgs);
    }

    private void remove(String[] selectionArgs) {
        // Define 'where' part of query.
        String selection = COLUMN_STORE_NAME + " LIKE ?";

        // Issue SQL statement.
        mDb.delete(TABLE_STORE, selection, selectionArgs);
    }

    // DESTROY - name
    public void removeName(String value) {
        String[] selectionArgs = {value};
        remove(selectionArgs);
    }

    // DESTROY - id
    public void removeId(long value) {
        String[] selectionArgs = {String.valueOf(value)};
        remove(selectionArgs);
    }

    public void removeAll() {
        List<Store> objs = getAll();
        for ( Store obj : objs ) {
            removeId(obj.getId());
        }
    }

} // StoreDbAdapter
