package com.qetuop.shoppinglist.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.qetuop.shoppinglist.pojo.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 4/23/16.
 */
public class ItemDbAdapter extends BaseDbAdapter
{
    protected static final String TAG = "ItemDbAdapter";

    private String[] projection = {
            COLUMN_ID,
            COLUMN_ITEM_NAME,
            COLUMN_ITEM_SELECTED,
            COLUMN_ITEM_COMPLETED
    };

    public ItemDbAdapter(Context ctx) {
        super(ctx);
        Log.d(TAG, "ItemDbAdapter::ctor");
    }

    public long insert(Item value) {
        long id = 0;

        ContentValues args = new ContentValues();

        args.put(COLUMN_ITEM_NAME, value.getName());
        args.put(COLUMN_ITEM_SELECTED, value.getSelected());
        args.put(COLUMN_ITEM_COMPLETED, value.getCompleted());

        id = mDb.insert(TABLE_ITEM, null, args);
        value.setId(id);

        return id;
    }

    // convert a "ptr/cursor" of a table entry to an object
    public Item cursorToObject(Cursor cursor) {
        Item obj = new Item();

        obj.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // why can't get?
        obj.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME)));
        obj.setSelected(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_SELECTED)));
        obj.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_COMPLETED)));

        return obj;
    }

    private Item get(String selection, String[] selectionArgs) {
        Item obj = new Item();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;

        Cursor cursor = mDb.query(
                TABLE_ITEM,  // The table to query
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
    public Item getId(long id) {
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};

        return get(selection, selectionArgs);
    }

    public Item getName(String name) {
        String selection = COLUMN_ITEM_NAME + "=?";
        String[] selectionArgs = {name};

        return get(selection, selectionArgs);
    }

    public List<Item> getAll() {
        List<Item> objs = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_ITEM,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item obj = cursorToObject(cursor);
            objs.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return objs;
    }
    public List<String> getAllNames() {
        List<String> objs = new ArrayList<>();

        String[] projection = {
                COLUMN_ITEM_NAME
        };
        Cursor cursor = mDb.query(
                true,
                TABLE_ITEM,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            objs.add( cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME)) );
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return objs;
    }


    public List<Item> getAllCompleted() {
        List<Item> objs = new ArrayList<>();

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_ITEM_COMPLETED + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = mDb.query(
                TABLE_ITEM,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item obj = cursorToObject(cursor);
            objs.add(obj);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return objs;
    }

    public Cursor getAllCursor() {

        Cursor cursor = mDb.query(
                TABLE_ITEM,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();

        return cursor;
    }

    public Cursor getAllSelectedCursor() {

        // How you want the results sorted in the resulting Cursor
        String sortOrder = null;//COLUMN_USER_USER_NAME + " DESC";
        String selection = COLUMN_ITEM_SELECTED + "=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = mDb.query(
                TABLE_ITEM,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.moveToFirst();

        return cursor;
    }

    // UPDATE
    public void update(long id, Item obj) {
        ContentValues args = new ContentValues();

        args.put(COLUMN_ITEM_NAME, obj.getName());
        args.put(COLUMN_ITEM_SELECTED, obj.getSelected());
        args.put(COLUMN_ITEM_COMPLETED, obj.getCompleted());

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = mDb.update(
                TABLE_ITEM,
                args,
                selection,
                selectionArgs);
    }

    private void remove(String[] selectionArgs) {
        // Define 'where' part of query.
        String selection = COLUMN_ITEM_NAME + " LIKE ?";

        // Issue SQL statement.
        mDb.delete(TABLE_ITEM, selection, selectionArgs);
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
        List<Item> objs = getAll();
        for ( Item obj : objs ) {
            removeId(obj.getId());
        }
    }

} // ItemDbAdapter
