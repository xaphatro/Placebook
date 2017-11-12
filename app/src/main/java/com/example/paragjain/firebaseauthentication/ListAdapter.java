package com.example.paragjain.firebaseauthentication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pradeep on 12-Nov-17.
 */

public class ListAdapter extends ArrayAdapter<List> {
    public ListAdapter(Context context, ArrayList<List> listHolder) {
        super(context, 0, listHolder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List li = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }
        TextView listNameView = (TextView) convertView.findViewById(R.id.list_title);
        TextView listIDView = (TextView) convertView.findViewById(R.id.list_id);
        listNameView.setText(li.listName);
        listIDView.setText(li.listID);
        return convertView;
    }
}
