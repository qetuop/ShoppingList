package com.qetuop.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSelectionActivity extends AppCompatActivity implements AlertDialog.OnMultiChoiceClickListener {
    protected static final String TAG = "ItemSelectionActivity";

    private AlertDialog alert;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Items");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(ItemSelectionActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        Cursor cursor = mItemDbAdapter.getAllCursor();
        ItemCursorAdapter itemAdapter = new ItemCursorAdapter(this, cursor, ItemCursorAdapter.OPTION.SELECTED.getValue());
        builder.setAdapter(itemAdapter, this);
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        final Cursor cursor = mItemDbAdapter.getAllCursor();

        // Set the dialog title
        builder.setTitle(R.string.select_items);
        builder.setMultiChoiceItems(cursor, BaseDbAdapter.COLUMN_ITEM_SELECTED, BaseDbAdapter.COLUMN_ITEM_NAME, this);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),ItemSelectionActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),ItemSelectionActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        Cursor cursor = (Cursor) alert.getListView().getAdapter().getItem(which);
        long id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ID));
        Item item = mItemDbAdapter.getId(id);
        item.setSelected((isChecked == true)? 1 : 0);
        mItemDbAdapter.update(id, item);
    }
}
