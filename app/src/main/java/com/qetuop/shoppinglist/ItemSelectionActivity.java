package com.qetuop.shoppinglist;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSelectionActivity extends AppCompatActivity {
    protected static final String TAG = "ItemSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Items");

        ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        List<Item> items = mItemDbAdapter.getAll();

       /* // THE DESIRED COLUMNS TO BE BOUND
        String[] columns = new String[] { BaseDbAdapter.COLUMN_ITEM_NAME};
        // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
        int[] to = new int[] {android.R.id.text1};

        // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor, columns, to, 0);*/

        List<String> typeNames = new ArrayList<>();
        final CharSequence[] tn = typeNames.toArray(new CharSequence[typeNames.size()]);
        builder.setItems(tn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ;//setType(String.valueOf(tn[item]));
                // toggle item selection

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
