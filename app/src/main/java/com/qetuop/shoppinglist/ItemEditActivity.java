package com.qetuop.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.qetuop.shoppinglist.dbadapter.AisleDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;

import java.sql.SQLException;

public class ItemEditActivity extends AppCompatActivity {
    protected static final String TAG = "ItemEditActivity";

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        AisleDbAdapter mAisleDbAdapter = new AisleDbAdapter(this);
        try {
            mAisleDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "aisle table open error");
        }

        final Cursor cursor = mItemDbAdapter.getAllCursor();

        // Set the dialog title
        builder.setTitle(R.string.edit_items);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.content_item_edit, null));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Intent intent = new Intent(getApplicationContext(),ItemSelectionActivity.class);
                //setResult(RESULT_OK, intent);
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Intent intent = new Intent(getApplicationContext(),ItemSelectionActivity.class);
                //setResult(RESULT_CANCELED, intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            Log.d(TAG, "onActivityResult");//
            //update();
        }
    } // onActivityResult
}
