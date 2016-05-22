package com.qetuop.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemLongClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qetuop.shoppinglist.dbadapter.BaseDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.pojo.Item;

import java.sql.SQLException;

/**
 * This is the list view on the main page
 */
public class ItemCursorAdapter extends CursorAdapter {
    protected static final String TAG = "ItemCursorAdapter";

    public final static String EXTRA_MESSAGE = "com.qetuop.MESSAGE";

    private Context context;
    //private long itemId = 0;

    public ItemCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        this.context = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_selecttion_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ID));
        int     checked     = cursor.getInt(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ITEM_COMPLETED));
        String  aisleName   = cursor.getString(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_AISLE_NAME));
        String  itemName    = cursor.getString(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ITEM_NAME));
Log.d(TAG, "bindView: " + itemId);
        CheckBox itemCheckedCb  = (CheckBox) view.findViewById(R.id.row_item_checked_cb);
        TextView aisleNameTv    = (TextView) view.findViewById(R.id.row_aisle_name_tv);
        TextView itemNameTv     = (TextView) view.findViewById(R.id.row_item_name_tv);

        itemCheckedCb.setChecked((checked == 1)? true : false);
        itemCheckedCb.setOnCheckedChangeListener(checkedChangeListener);
        itemNameTv.setOnClickListener(clickListener);
        itemCheckedCb.setTag(itemId);

        aisleNameTv.setText(aisleName);
        itemNameTv.setText(itemName);
        itemNameTv.setTag(itemId);
    }

    private AdapterViewCompat.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long itemId = (long)v.findViewById(R.id.row_item_name_tv).getTag();

            Intent intent = new Intent(context, ItemEditActivity.class);
            intent.putExtra(EXTRA_MESSAGE, itemId);

            int REQUEST_CODE = 0; // set it to ??? a code to identify which activity is returning?
            ((Activity)context).startActivityForResult(intent,REQUEST_CODE);

        }
    };

    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            long itemId = (long)buttonView.getTag();
            ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(context);
            try {
                mItemDbAdapter.open();
            } catch (SQLException e) {
                Log.e(TAG, "item table open error");
            }
Log.d(TAG, "Checked item: " + itemId);
            Item item = mItemDbAdapter.getId(itemId);
            item.setCompleted((isChecked == true)? 1 : 0);
            mItemDbAdapter.update(itemId, item);
        }
    };
}

