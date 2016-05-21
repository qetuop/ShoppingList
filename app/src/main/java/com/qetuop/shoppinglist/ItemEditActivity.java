package com.qetuop.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private AlertDialog alert;
    private long itemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        Intent intent = getIntent();
        if ( intent.hasExtra(ItemCursorAdapter.EXTRA_MESSAGE) == true ) {
            itemId = intent.getLongExtra(ItemCursorAdapter.EXTRA_MESSAGE, 0);
        }
        Log.e(TAG, "itemId: " + String.valueOf(itemId));

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

        Item item = mItemDbAdapter.getId(itemId);
        Aisle aisle = mAisleDbAdapter.getItem(itemId);

        // Set the dialog title
        builder.setTitle(R.string.edit_items);



        //LayoutInflater inflater = LayoutInflater.from(builder.getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.content_item_edit, null);
        builder.setView(dialogView);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(inflater.inflate(R.layout.content_item_edit, null));

        EditText itemNameEt = (EditText) dialogView.findViewById(R.id.item_edit_name_et);
        EditText aisleNameEt = (EditText) dialogView.findViewById(R.id.item_edit_aisle_et);

        Log.d(TAG, "Item:"+ (item==null));
        Log.d(TAG, "itemNameEt:"+ (itemNameEt==null));

        itemNameEt.setText(item.getName());;
        aisleNameEt.setText(aisle.getName());

        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(itemNameEt, InputMethodManager.SHOW_IMPLICIT);
        //itemNameEt.requestFocus();


        //setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_S‌​TATE_VISIBLE);


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


        // Back button?
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
