package com.qetuop.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.qetuop.shoppinglist.dbadapter.AisleDbAdapter;
import com.qetuop.shoppinglist.dbadapter.BaseDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.dbadapter.StoreDbAdapter;
import com.qetuop.shoppinglist.pojo.Aisle;
import com.qetuop.shoppinglist.pojo.Item;
import com.qetuop.shoppinglist.pojo.Store;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";

    // Database accessors
    private BaseDbAdapter mBaseDbAdapter;
    private ItemDbAdapter mItemDbAdapter;
    private StoreDbAdapter mStoreDbAdapter;
    private AisleDbAdapter mAisleDbAdapter;

    private  ListView listview;
    //private ItemCursorAdapter itemCursorAdapter;

    // TODO: remove
    private Long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseSetup();

        // TODO: remove
        hardcodedSetup();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listview = (ListView) findViewById(R.id.content_main_lv_items);

        //Cursor cursor = mBaseDbAdapter.getItemAisleCursor(storeId);
        //Log.d(TAG, "***Cursor for store: " + String.valueOf(storeId) + ":"+String.valueOf(cursor.getCount()));
        //itemCursorAdapter = new ItemCursorAdapter(this, cursor, 0);
        //listview.setAdapter(itemCursorAdapter);


        update();

    }

    public void update() {
        // all
        List<Item> objs = mItemDbAdapter.getAll();

        Log.v(TAG,"---All Items---(storeId):" + storeId);
        for (Item obj : objs) {
            Log.v(TAG, obj.getId() + " " + obj.getName() + " " + obj.getSelected());
        }
        Log.v(TAG,"--------------");
        //if ( itemCursorAdapter != null ) Log.d(TAG, "update, itemCursorAdapter size[1]: " + itemCursorAdapter.getCount());
        //final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);
        //itemCursorAdapter.notifyDataSetChanged();
        //listview.invalidateViews();


        // TODO:  should i be setting this every update?
        Cursor cursor = mBaseDbAdapter.getItemAisleCursor(storeId);
        ItemCursorAdapter itemCursorAdapter = new ItemCursorAdapter(this, cursor, 0);
        listview.setAdapter(itemCursorAdapter);
        //listview.refreshDrawableState();

    } // update

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.clear_completed:
                // get completed list
                ArrayList<Item> completed = (ArrayList<Item>)mItemDbAdapter.getAllCompleted();

                // clear completed bool
                for ( Item completedItem : completed ){
                    completedItem.setCompleted(0);
                    completedItem.setSelected(0);
                    mItemDbAdapter.update(completedItem.getId(), completedItem);
                }

                // update
                update();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void databaseSetup() {
        mBaseDbAdapter = new BaseDbAdapter(this);
        try {
            mBaseDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "BaseDbAdapter open error");
        }

        // TODO: REMOVE THIS
        mBaseDbAdapter.removeAll();

        Log.v(TAG,"--------------");

        mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "item table open error");
        }

        mStoreDbAdapter = new StoreDbAdapter(this);
        try {
            mStoreDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "store table open error");
        }

        mAisleDbAdapter = new AisleDbAdapter(this);
        try {
            mAisleDbAdapter.open();
        } catch (SQLException e) {
            Log.e(TAG, "aisle table open error");
        }
    }

    private void hardcodedSetup() {
        String[] tmp;
        ArrayList<String> list;
        ArrayList<Long> storeIds = new ArrayList<>();

        // Stores
        tmp = new String[] { "Giant", "Safeway"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Store store = new Store(s);
            long id = mStoreDbAdapter.insert(store);
            storeId = id;
            storeIds.add(id);
        }

        // Create Items
/*        tmp = new String[] { "Milk", "Butter", "Cheese", "cereal",
                "ice cream", "apples", "chicken", "french fries",
        "fruit", "steak", "pop corn", "corn", "bread"};*/

        tmp = new String[] { "Cereal", "Apple", "Bread"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Item item = new Item(s);

            Random randomGenerator = new Random();
            item.setSelected(randomGenerator.nextInt(2));
            //item.setCompleted(randomGenerator.nextInt(2));

            long id = mItemDbAdapter.insert(item);

            // add to aisle

            for ( Long l : storeIds ) {
                String aisle_name = "";
                int rand = randomGenerator.nextInt(8);
                //Log.d(TAG, "RAND:"+ String.valueOf(rand));
                if (rand == 0 ) {
                    aisle_name = "";
                }
                else if ( rand == 1 ) {
                    aisle_name = "Deli";
                }
                else  {
                    aisle_name = String.valueOf(rand);
                }
                //Aisle aisle = new Aisle(l, item.getId(), String.valueOf(randomGenerator.nextInt(20)));
                Aisle aisle = new Aisle(l, item.getId(), aisle_name);
                mAisleDbAdapter.insert(aisle);
            }
        }
/*
        Cursor cursor = mBaseDbAdapter.getItemAisleCursor(storeId);
        Log.d(TAG, "Cursor for store: " + String.valueOf(storeId) + ":"+String.valueOf(cursor.getCount()));*/
    } // hardcodedSetup



    // if text entry is blank, bring up selection list
    public void addItem(View view) {
        EditText itemEt = (EditText) findViewById(R.id.content_main_et_message);
        String s = itemEt.getText().toString();
        System.out.println("Text= " + s);

        if ( itemEt.length() == 0 ) {
            Intent intent = new Intent(this, ItemSelectionActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, 0l);
            //startActivity(intent);

            int REQUEST_CODE = 0; // set it to ??? a code to identify which activity is returning?
            startActivityForResult(intent, REQUEST_CODE);
        }
        else {
            Item item = new Item(s);
            item.setSelected(1);
            long itemId = mItemDbAdapter.insert(item);

            // need to add aisle for every store.
            List<Store> stores = mStoreDbAdapter.getAll();
            for ( Store store : stores ) {
                Aisle aisle = new Aisle();
                aisle.setItemId(itemId);
                aisle.setStoreId(store.getId());
                mAisleDbAdapter.insert(aisle);
            }

            itemEt.setText("");
            Log.d(TAG, "Item added bout to update: " + itemId + ":" + item.getId() + ":" + item.getName());
            update();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            Log.d(TAG, "onActivityResult");//
            update();
        }
    } // onActivityResult
}
