package com.qetuop.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    protected static final String LOG = "MainActivity";

    // Database accessors
    private ItemDbAdapter mItemDbAdapter;

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
        mItemDbAdapter = new ItemDbAdapter(this);
        try {
            mItemDbAdapter.open();
        } catch (SQLException e) {
            Log.e(LOG, "user table open error");
        }
    }

    private void hardcodedSetup() {

        ////// --------------------------- //////

        ////// Hardcode some exercieses //////
        mItemDbAdapter.removeAll();

        // Create
        Item item = new Item("Milk");
        long id = mItemDbAdapter.insert(item);

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
        System.out.println("---All Items---");
        for (Item obj : objs) {

            System.out.println(obj.getId() + " " + obj.getName());
        }
        System.out.println("-----------------");

        final ListView listview = (ListView) findViewById(R.id.content_main_lv_items);

        String[] myStringArray = {"A", "B", "C"};
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


    public void addItem(View view) {
//        Intent intent = new Intent(this, WorkoutActivity.class);
//        //long l = 0l;
//        intent.putExtra(EXTRA_MESSAGE, 0l);
//        startActivity(intent);
    }
}
