package com.qetuop.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
    // TODO: remove
    private Long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseSetup();

        hardcodedSetup();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listview = (ListView) findViewById(R.id.content_main_lv_items);
        listview.setOnItemClickListener(OLC);

        //final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);
        Cursor cursor = mItemDbAdapter.getAllSelectedCursor();
        ItemCursorAdapter itemAdapter = new ItemCursorAdapter(this, cursor, ItemCursorAdapter.OPTION.COMPLETED.getValue());
        listview.setAdapter(itemAdapter);


        update();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    public void listViewClick(View view) {
        Log.d(TAG, "CLICK");
    }
    private AdapterView.OnItemClickListener OLC = new  AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(getApplicationContext());
            try {
                mItemDbAdapter.open();
            } catch (SQLException e) {
                Log.e(TAG, "item table open error");
            }

            Log.d(TAG, "OnItemClickListener:"+String.valueOf(position) +":"+String.valueOf(id));
            //final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);
            Cursor cursor = (Cursor) listview.getAdapter().getItem(position);
            long id2 = cursor.getInt(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ID));
            Item item = mItemDbAdapter.getId(id2);
            //item.setCompleted((isChecked == true)? 1 : 0);
            mItemDbAdapter.update(id2, item);
        }
    };

    private View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "HERE");
        }
    };


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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        tmp = new String[] { "Milk", "Butter", "Cheese", "cereal",
                "ice cream", "apples", "chicken", "french fries",
        "fruit", "steak", "pop corn", "corn", "bread"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Item item = new Item(s);
            long id = mItemDbAdapter.insert(item);

            Random randomGenerator = new Random();

            item.setSelected(randomGenerator.nextInt(1));

            // add to aisle

            for ( Long l : storeIds ) {
                Aisle aisle = new Aisle(l, item.getId(), String.valueOf(randomGenerator.nextInt(20)));
                mAisleDbAdapter.insert(aisle);
            }
        }

    } // hardcodedSetup

    private void update() {
        // all
        List<Item> objs = mItemDbAdapter.getAll();

        Log.v(TAG,"---All Items---");
        for (Item obj : objs) {
            Log.v(TAG, obj.getId() + " " + obj.getName());
        }
        Log.v(TAG,"--------------");

        //final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);
        Cursor cursor = mItemDbAdapter.getAllSelectedCursor();
        ItemCursorAdapter itemAdapter = new ItemCursorAdapter(this, cursor, ItemCursorAdapter.OPTION.COMPLETED.getValue());
        listview.setAdapter(itemAdapter);

    } // update


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
            mItemDbAdapter.insert(item);
            itemEt.setText("");

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
