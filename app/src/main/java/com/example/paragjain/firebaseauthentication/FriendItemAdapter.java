package com.example.paragjain.firebaseauthentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pradeep on 12-Nov-17.
 */

public class FriendItemAdapter extends ArrayAdapter<Item> {
    public FriendItemAdapter(Context context, ArrayList<Item> itemHolder) {
        super(context, 0, itemHolder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item it = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_item_todo, parent, false);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
        TextView itemID = (TextView) convertView.findViewById(R.id.item_id);
        TextView location = (TextView) convertView.findViewById(R.id.item_location);
        TextView longitude = (TextView) convertView.findViewById(R.id.item_longitude);
        TextView latitude = (TextView) convertView.findViewById(R.id.item_latitude);
        itemName.setText(it.itemName);
        itemID.setText(it.itemID);
        location.setText(it.locationName);
        longitude.setText(it.longitude);
        latitude.setText(it.latitude);
        return convertView;
    }
}
