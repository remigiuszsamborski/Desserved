package net.swierkowski.cookbook4.listAdapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.swierkowski.cookbook4.R;

public class ExcludedProductsAdapter extends CursorAdapter {

    public ExcludedProductsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.excluded_cell, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        CheckBox cbRestriction = (CheckBox) view.findViewById(R.id.checkBox_product);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("productsName"));
        int restriction = cursor.getInt(cursor.getColumnIndexOrThrow("productsRestriction"));
        tvName.setText(name);
        if(restriction==1){
            cbRestriction.setChecked(true);
        } else {cbRestriction.setChecked(false);}
    }

}
