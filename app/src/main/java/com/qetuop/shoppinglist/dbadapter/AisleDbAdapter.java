package com.qetuop.shoppinglist.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.qetuop.shoppinglist.pojo.Aisle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 4/23/16.
 */
public class AisleDbAdapter extends BaseDbAdapter
{
    private String[] projection = {
            COLUMN_ID,
            COLUMN_AISLE_NAME,
            COLUMN_AISLE_STORE_ID,
            COLUMN_AISLE_ITEM_ID
    };

    public AisleDbAdapter(Context ctx) {
        super(ctx);
    }

    public long insert(Aisle obj) {
        long id = 0;

        ContentValues values = new ContentValues();

        values.put(COLUMN_AISLE_NAME, obj.getName());
        values.put(COLUMN_AISLE_STORE_ID, obj.getStoreId());
        values.put(COLUMN_AISLE_ITEM_ID, obj.getItemId());

        id = mDb.insert(TABLE_AISLE, null, values);
        obj.setId(id);

        return id;
    }

    // convert a "ptr/cursor" of a table entry to an object
    public Aisle cursorToObject(Cursor cursor) {
        Aisle obj = new Aisle();

        obj.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // why can't get?
        obj.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AISLE_NAME)));
        obj.setStoreId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_AISLE_STORE_ID)));
        obj.setItemId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_AISLE_ITEM_ID)));

        return obj;
    }

    private Aisle get(String selection, String[] selectionArgs) {
        Aisle obj = new Aisle();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;

        Cursor cursor = mDb.query(
                TABLE_AISLE,  // The table to query
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
    public Aisle getId(long id) {
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        return get(selection, selectionArgs);
    }

    public Aisle getName(String name) {
        String selection = COLUMN_AISLE_NAME + "=?";
        String[] selectionArgs = {name};

        return get(selection, selectionArgs);
    }

    public Aisle getItem(long itemId) {
        String selection = COLUMN_AISLE_ITEM_ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};

        return get(selection, selectionArgs);
    }

    public List<Aisle> getAll() {
        List<Aisle> objs = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_AISLE,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Aisle obj = cursorToObject(cursor);
            objs.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return objs;
    }

    public Cursor getAllCursor() {

        Cursor cursor = mDb.query(
                TABLE_AISLE,
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
    public void update(long id, Aisle obj) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_AISLE_NAME, obj.getName());
        values.put(COLUMN_AISLE_STORE_ID, obj.getStoreId());
        values.put(COLUMN_AISLE_ITEM_ID, obj.getItemId());

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_AISLE,
                values,
                selection,
                selectionArgs);
    }

    private void remove(String[] selectionArgs) {
        // Define 'where' part of query.
        String selection = COLUMN_AISLE_NAME + " LIKE ?";

        // Issue SQL statement.
        mDb.delete(TABLE_AISLE, selection, selectionArgs);
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
        List<Aisle> objs = getAll();
        for ( Aisle obj : objs ) {
            removeId(obj.getId());
        }
    }

} // AisleDbAdapter
