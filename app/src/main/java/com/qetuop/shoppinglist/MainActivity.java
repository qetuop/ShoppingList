package com.qetuop.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseSetup();

        hardcodedSetup();


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        long storeId=0;
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
                "ice cream", "apples", "chicken", "french fries"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Item item = new Item(s);
            long id = mItemDbAdapter.insert(item);

            // add to aisle
            Random randomGenerator = new Random();
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

        final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);

        String[] myStringArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        //String[] myStringArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> myAdapter = new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myStringArray);
        listview.setAdapter(myAdapter);

        //Cursor cursor = mItemDbAdapter.getAllCursor();

        // Setup cursor adapter using cursor from last step
        //WorkoutCursorAdapter workoutAdapter = new WorkoutCursorAdapter(this, workoutCursor,0);

        // Attach cursor adapter to the ListView
        //listview.setAdapter(workoutAdapter.);


    } // update


    // if text entry is blank, bring up selection list
    public void addItem(View view) {
        EditText itemEt = (EditText) findViewById(R.id.content_main_et_message);
        String s = itemEt.getText().toString();
        System.out.println("Text= " + s);

//        Intent intent = new Intent(this, WorkoutActivity.class);
//        //long l = 0l;
//        intent.putExtra(EXTRA_MESSAGE, 0l);
//        startActivity(intent);
    }
}
