package com.qetuop.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by brian on 5/10/16.
 */
public class ItemCursorAdapter extends CursorAdapter {
    protected static final String TAG = "ItemCursorAdapter";

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
        }

        CheckBox itemCheckedCb = (CheckBox) view.findViewById(R.id.row_item_checked_cb);
        itemCheckedCb.setSelected((checked == 1)? true : false);
        itemCheckedCb.setText(name);
    }
}

