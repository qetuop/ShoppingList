package com.qetuop.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;

import com.qetuop.shoppinglist.dbadapter.BaseDbAdapter;
import com.qetuop.shoppinglist.dbadapter.ItemDbAdapter;
import com.qetuop.shoppinglist.pojo.Item;

import java.sql.SQLException;

/**
 * Created by brian on 5/10/16.
 */
public class ItemCursorAdapter extends CursorAdapter {
    protected static final String TAG = "ItemCursorAdapter";

    private Context context;

    public static enum OPTION {
        SELECTED(0), COMPLETED(1);

        private final int value;

        private OPTION(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    int option = 0;

    public ItemCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);

        option = flags;
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

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ITEM_NAME));
        int checked = 0;

        if ( option == OPTION.COMPLETED.getValue() ) {
            checked = cursor.getInt(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ITEM_COMPLETED));
            Log.d(TAG, "COMPLETED:"+checked);
        }

        CheckBox itemCheckedCb = (CheckBox) view.findViewById(R.id.row_item_checked_cb);
        itemCheckedCb.setChecked((checked == 1)? true : false);
        itemCheckedCb.setText(name);
        //itemCheckedCb.setOnClickListener(itemClickListener);
        itemCheckedCb.setOnCheckedChangeListener(ccl);

        // TODO: is there a better way
        itemCheckedCb.setTag(cursor.getLong(cursor.getColumnIndexOrThrow(BaseDbAdapter.COLUMN_ID)));

        this.context = context;

    }

    private OnCheckedChangeListener ccl = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            ItemDbAdapter mItemDbAdapter = new ItemDbAdapter(context);
            try {
                mItemDbAdapter.open();
            } catch (SQLException e) {
                Log.e(TAG, "item table open error");
            }

            long id = (long) buttonView.getTag();

            Item item = mItemDbAdapter.getId(id);
            item.setCompleted((isChecked == true)? 1 : 0);
            mItemDbAdapter.update(id, item);

        }
    };
}

