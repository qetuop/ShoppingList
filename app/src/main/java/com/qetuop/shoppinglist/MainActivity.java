package com.qetuop.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.qetuop.shoppinglist.dbadapter.AisleDbAdapter;
import com.qetuop.shoppinglist.dbadapter.BaseDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.dbadapter.StoreDbAdapter;
import com.qetuop.shoppinglist.fileselector.FileOperation;
import com.qetuop.shoppinglist.fileselector.FileSelector;
import com.qetuop.shoppinglist.fileselector.FileSelectorActivity;
import com.qetuop.shoppinglist.fileselector.OnHandleFileListener;
import com.qetuop.shoppinglist.pojo.Aisle;
import com.qetuop.shoppinglist.pojo.Item;
import com.qetuop.shoppinglist.pojo.Store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";

    public static final String PREFS_NAME = "preferences";

    public static final int STORE_SELECTION = 1;
    public static final int IMPORT_FILE = 2;
    public static final int EXPORT_FILE = 3;


    // Database accessors
    private BaseDbAdapter mBaseDbAdapter;
    private ItemDbAdapter mItemDbAdapter;
    private StoreDbAdapter mStoreDbAdapter;
    private AisleDbAdapter mAisleDbAdapter;

    private  ListView listview;
    //private ItemCursorAdapter itemCursorAdapter;

    // TODO: remove?
    private Long mStoreId;
    private Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseSetup();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mStoreId = settings.getLong("storeId", 0l);

        firstTime = settings.getBoolean("firstTime", true);
        Log.d(TAG, "first Time = " + firstTime.toString());

        // TODO: remove
        //hardcodedSetup();
        if ( firstTime == true ) {
            firstTime();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listview = (ListView) findViewById(R.id.content_main_lv_items);

        //Cursor cursor = mBaseDbAdapter.getItemAisleCursor(mStoreId);
        //Log.d(TAG, "***Cursor for store: " + String.valueOf(mStoreId) + ":"+String.valueOf(cursor.getCount()));
        //itemCursorAdapter = new ItemCursorAdapter(this, cursor, 0);
        //listview.setAdapter(itemCursorAdapter);

        /*AutoCompleteTextView actv;
        actv = (AutoCompleteTextView) findViewById(R.id.content_main_et_add);
        ArrayList<String> itemNames = (ArrayList<String>) mItemDbAdapter.getAllNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemNames);
        actv.setAdapter(adapter);*/

        update();

    }

    private void firstTime() {
        Store store = new Store("My Store");
        long id = mStoreDbAdapter.insert(store);
        mStoreId = id;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstTime", false);
        editor.putLong("storeId", mStoreId);

        // Commit the edits!
        editor.commit();

    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("storeId", mStoreId);

        // Commit the edits!
        editor.commit();
    }


    public void update() {
        // all
/*        List<Item> objs = mItemDbAdapter.getAll();

        Log.v(TAG,"---All Items---(mStoreId):" + mStoreId + ":" +mStoreDbAdapter.getId(mStoreId).getName());
        for (Item obj : objs) {
            Log.v(TAG, obj.getId() + " " + obj.getName() + " " + obj.getSelected());
        }
        Log.v(TAG,"--------------");*/

        //if ( itemCursorAdapter != null ) Log.d(TAG, "update, itemCursorAdapter size[1]: " + itemCursorAdapter.getCount());
        //final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);
        //itemCursorAdapter.notifyDataSetChanged();
        //listview.invalidateViews();


        // TODO:  should i be setting this every update?
        Cursor cursor = mBaseDbAdapter.getItemAisleCursor(mStoreId);
        ItemCursorAdapter itemCursorAdapter = new ItemCursorAdapter(this, cursor, 0);
        listview.setAdapter(itemCursorAdapter);
        //listview.refreshDrawableState();

        AutoCompleteTextView actv;
        actv = (AutoCompleteTextView) findViewById(R.id.content_main_et_add);
        ArrayList<String> itemNames = (ArrayList<String>) mItemDbAdapter.getAllNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemNames);
        actv.setAdapter(adapter);

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

        Intent intent;
        int REQUEST_CODE;

        final String[] mFileFilter = {  "*.*", "*.db" }; // if*.db is listed first, won't display any

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

            case R.id.menu_select_store:
                Log.d(TAG, "SELECT STORE");

                intent = new Intent(this, StoreSelectionActivity.class);
                REQUEST_CODE = STORE_SELECTION; // set it to ??? a code to identify which activity is returning?
                startActivityForResult(intent, REQUEST_CODE);

                return true;

            case R.id.menu_export:
                Log.d(TAG, "EXPORT");
                new FileSelector(this, FileOperation.SAVE, mSaveFileListener, mFileFilter).show();
                return true;

            case R.id.menu_import:
                Log.d(TAG, "IMPORT");
                new FileSelector(this, FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            Log.d(TAG, "HEEEERRRRR");
            Toast.makeText(getApplicationContext(), "Load: " + filePath, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "NOW DO STUFFFF");
            // copy the file to dir
            String toPath = "/data/data/" + getPackageName() + "/databases/" + BaseDbAdapter.DATABASE_NAME;  // Your application path
            copyFile(filePath, toPath);  // in -> out

            // re open database
            databaseSetup();
        }
    };

    OnHandleFileListener mSaveFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            Toast.makeText(getApplicationContext(), "Save: " + filePath, Toast.LENGTH_SHORT).show();
Log.d(TAG, "NOW DO STUFFFF");
            // copy the file to current dir
            String fromPath = "/data/data/" + getPackageName() + "/databases/" + BaseDbAdapter.DATABASE_NAME;  // Your application path
            copyFile(fromPath, filePath); // in -> out
        }
    };

    private void copyFile(String inputPath, String outputPath) {
        Log.d(TAG, "IN:  " + inputPath);
        Log.d(TAG, "OUT: " + outputPath);

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
           /* File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }*/


            in = new FileInputStream(inputPath );
            //out = new FileOutputStream(outputPath + "/databases/" + BaseDbAdapter.DATABASE_NAME);
            out = new FileOutputStream(outputPath);


            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            //new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e(TAG, fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
        //mBaseDbAdapter.removeAll();

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
        tmp = new String[] { "Giant"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Store store = new Store(s);
            long id = mStoreDbAdapter.insert(store);
            mStoreId = id;
            storeIds.add(id);
        }

        // Create Items
        tmp = new String[] { "Milk", "Butter", "Cheese", "cereal",
                "ice cream", "apples", "chicken", "french fries",
        "fruit", "steak", "pop corn", "corn", "bread"};

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
            for ( Long tmpStoreId : storeIds ) {

                String aisle_name = "";

                int rand = randomGenerator.nextInt(8);

                if (rand == 0 ) {
                    aisle_name = "";
                }
                else if ( rand == 1 ) {
                    aisle_name = "Deli";
                }
                else  {
                    aisle_name = String.valueOf(rand);
                }

                long tmpId = 0;
                Store store = mStoreDbAdapter.getId(tmpStoreId);
                String storeName = store.getName();
                if ( storeName.equals("Giant"))
                    tmpId = 0;
                else
                    tmpId = 1;


                //Aisle aisle = new Aisle(l, item.getId(), String.valueOf(randomGenerator.nextInt(20)));
                //Aisle aisle = new Aisle(tmpStoreId, item.getId(), aisle_name);
                Aisle aisle = new Aisle(tmpStoreId, item.getId(), String.valueOf(tmpId));
                mAisleDbAdapter.insert(aisle);
            } // for each store

        }
/*
        Cursor cursor = mBaseDbAdapter.getItemAisleCursor(mStoreId);
        Log.d(TAG, "Cursor for store: " + String.valueOf(mStoreId) + ":"+String.valueOf(cursor.getCount()));*/
    } // hardcodedSetup



    // if text entry is blank, bring up selection list
    public void addItem(View view) {
        EditText itemEt = (EditText) findViewById(R.id.content_main_et_add);
        String itemName = itemEt.getText().toString();
        System.out.println("Text= " + itemName);

        if ( itemEt.length() == 0 ) {
            Intent intent = new Intent(this, ItemSelectionActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, 0l);
            //startActivity(intent);

            int REQUEST_CODE = 0; // set it to ??? a code to identify which activity is returning?
            startActivityForResult(intent, REQUEST_CODE);
        }
        else {
            // does item already exist
            ArrayList<String> itemNames = (ArrayList<String>) mItemDbAdapter.getAllNames();
            Log.d(TAG, "Found:" + String.valueOf(ContainsCaseInsensitive(itemNames, itemName)));
            boolean found = ContainsCaseInsensitive(itemNames, itemName);

            if ( ContainsCaseInsensitive(itemNames, itemName) ) {
                Item item = mItemDbAdapter.getNameNoCase(itemName);
                Log.d(TAG, "MATCHED ITEM: " + item.getName());
                item.setSelected(1);
                mItemDbAdapter.update(item.getId(), item);
            }
            else {
                Item item = new Item(itemName);
                item.setSelected(1);
                long itemId = mItemDbAdapter.insert(item);

                // need to add aisle for every store.
                List<Store> stores = mStoreDbAdapter.getAll();
                for (Store store : stores) {
                    Aisle aisle = new Aisle();
                    aisle.setItemId(itemId);
                    aisle.setStoreId(store.getId());
                    mAisleDbAdapter.insert(aisle);
                }
            }

            itemEt.setText("");
            //Log.d(TAG, "Item added bout to update: " + itemId + ":" + item.getId() + ":" + item.getName());
            update();
        }
    }

    public static boolean ContainsCaseInsensitive(ArrayList<String> searchList, String searchTerm)
    {
        for (String item : searchList)
        {
            if (item.equalsIgnoreCase(searchTerm))
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + requestCode +":" + resultCode);//

        if (resultCode == RESULT_OK && requestCode == 0) {
            Log.d(TAG, "onActivityResult");//
            update();
        }

        if (resultCode == RESULT_OK && requestCode == STORE_SELECTION) {
            Log.d(TAG, "update store");//

            // Set in the StoreSelectionActivity
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            mStoreId = settings.getLong("storeId", 0l);
            Log.d(TAG, "storeId now = " + mStoreId);
            update();
        }

    } // onActivityResult
}
