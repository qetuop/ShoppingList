package com.qetuop.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.qetuop.shoppinglist.dbadapter.BaseDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.dbadapter.StoreDbAdapter;
import com.qetuop.shoppinglist.pojo.Item;
import com.qetuop.shoppinglist.pojo.Store;

import java.sql.SQLException;

public class StoreSelectionActivity extends AppCompatActivity implements AlertDialog.OnClickListener {
    protected static final String TAG = "ItemSelectionActivity";

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selection);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        StoreDbAdapter mStoreDbAdapter = new StoreDbAdapter(this);
        try {
            mStoreDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "store table open error");
        }

        final Cursor cursor = mStoreDbAdapter.getAllCursor();

        // Set the dialog title
        builder.setTitle(R.string.select_store);
        builder.setSingleChoiceItems(cursor, -1, BaseDbAdapter.COLUMN_STORE_NAME, this);

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
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });


        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        alert = builder.create();
        alert.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        StoreDbAdapter mStoreDbAdapter = new StoreDbAdapter(this);
        try {
            mStoreDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "store table open error");
        }

        Cursor cursor = (Cursor) alert.getListView().getAdapter().getItem(which);
        long id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ID));
        Store store = mStoreDbAdapter.getId(id);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("mStoreId", id);

        Log.d(TAG, "Store cliked: " + id + ":" + store.getName());

        // Commit the edits!
        editor.commit();
    }
}
