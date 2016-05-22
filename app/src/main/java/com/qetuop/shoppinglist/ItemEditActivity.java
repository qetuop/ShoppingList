package com.qetuop.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qetuop.shoppinglist.dbadapter.AisleDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.pojo.Aisle;
import com.qetuop.shoppinglist.pojo.Item;

import java.sql.SQLException;

public class ItemEditActivity extends AppCompatActivity {
    protected static final String TAG = "ItemEditActivity";

    private long itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        // get the Item Id to be edited
        Intent intent = getIntent();
        if ( intent.hasExtra(ItemCursorAdapter.EXTRA_MESSAGE) == true ) {
            itemId = intent.getLongExtra(ItemCursorAdapter.EXTRA_MESSAGE, 0);
        }
        Log.e(TAG, "itemId: " + String.valueOf(itemId));

        final ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        final AisleDbAdapter mAisleDbAdapter = new AisleDbAdapter(this);
        try {
            mAisleDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "aisle table open error");
        }

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        long storeId = settings.getLong("storeId", 0l);

        final Item item = mItemDbAdapter.getId(itemId);
        final Aisle aisle = mAisleDbAdapter.getItemFromStore(itemId, storeId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_items);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.content_item_edit, null);
        builder.setView(dialogView);

        final EditText itemNameEt = (EditText) dialogView.findViewById(R.id.item_edit_name_et);
        final EditText aisleNameEt = (EditText) dialogView.findViewById(R.id.item_edit_aisle_et);

        itemNameEt.setText(item.getName());;
        aisleNameEt.setText(aisle.getName());

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                aisle.setName(aisleNameEt.getText().toString());
                item.setName(itemNameEt.getText().toString());

                mItemDbAdapter.update(item.getId(), item);
                mAisleDbAdapter.update(aisle.getId(), aisle);

                Intent intent = new Intent(getApplicationContext(),ItemEditActivity.class);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        // Back button?
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
