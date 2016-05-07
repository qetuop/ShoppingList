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

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";

    // Database accessors
    private BaseDbAdapter mBaseDbAdapter;
    private ItemDbAdapter mItemDbAdapter;
    private StoreDbAdapter mStoreDbAdapter;

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
    }

    private void hardcodedSetup() {

        ////// --------------------------- //////

        ////// Hardcode some exercieses //////
        //mItemDbAdapter.removeAll();

        // Create Items
        String[] tmp = new String[] { "Milk", "Butter", "Cheese", "cereal",
                "ice cream", "apples", "chicken", "french fries"};
        ArrayList<String> list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Item item = new Item(s);
            long id = mItemDbAdapter.insert(item);
        }

        // Create Items
        tmp = new String[] { "Giant", "Safeway"};
        list = new ArrayList<String>();
        list.addAll( Arrays.asList(tmp) );
        for ( String s : list ) {
            Store store = new Store(s);
            long id = mStoreDbAdapter.insert(store);
        }

        //Item item = new Item("Milk");
        //long id = mItemDbAdapter.insert(item);

//
//        Exercise ex1 = mExerciseDbAdapter.getExercise("Bench Press");
//        long ex_id1 = 0;
//        if ( ex1.getExerciseName() == null ) {
//            ex1 = new Exercise("Bench Press", "Chest", mUserId);
//
//            // Inserting in db
//            ex_id1 = mExerciseDbAdapter.createExercise(ex1);
//            ex1.setId(ex_id1);
//            //long ex_id2 = mExerciseDbAdapter.createExercise(ex2);
//            //ex2.setId(ex_id2);
//        }
//        else
//            ex_id1 = ex1.getId();
//
//        Exercise ex;
//        ex= new Exercise("Tricep Push Down", "Tricep", mUserId);
//        mExerciseDbAdapter.createExercise(ex);
//
//        ex = new Exercise("Curls", "Biceps", mUserId);
//        mExerciseDbAdapter.createExercise(ex);
//
//        ex = new Exercise("Squat", "Legs", mUserId);
//        mExerciseDbAdapter.createExercise(ex);
//
//        ex = new Exercise("Flys", "Chest", mUserId);
//        mExerciseDbAdapter.createExercise(ex);
//
//        //Exercise ex2; = new Exercise("Tricep Push Down", "Tricep", mUserId);
//
//        ////// --------------------------- //////
//
//        ////// Hardcode some workouts //////
//        mWorkoutDbAdapter.removeAllWorkouts();
//
//        // Create Workout
//        Workout w1 = new Workout(System.currentTimeMillis(), ex_id1);
//        Workout w2 = new Workout(System.currentTimeMillis()-1000000000, ex_id1);
//        Workout w3 = new Workout(System.currentTimeMillis(), ex_id1);
//        System.out.println(w1.getExercise_id());
//        //w1.setExercise_id(ex_id1);
//        w1.setReps("10,8,6");
//        w1.setSets(3);
//        w1.setWeight("81,90,100");
//        System.out.println(w1.getExercise_id());
//        //w2.setExercise_id(ex_id1);
//        w2.setReps("20,16,12");
//        w2.setSets(3);
//        w2.setWeight("40,45,50");
//
//        /*w3.setExercise_id(ex_id1);
//        w3.setReps("10,8,6");
//        w3.setSets(3);
//        w3.setWeight("80,90,100");*/
//
//        long w_id1 = mWorkoutDbAdapter.createWorkout(w1);
//        System.out.println(w1.getExercise_id());
//        long w_id2 = mWorkoutDbAdapter.createWorkout(w2);
//        long w_id3 = mWorkoutDbAdapter.createWorkout(w3);
//
//        ////// --------------------------- //////

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
