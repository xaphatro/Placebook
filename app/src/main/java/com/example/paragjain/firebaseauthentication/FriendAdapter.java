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

public class FriendAdapter extends ArrayAdapter<Friend> {
    public FriendAdapter(Context context, ArrayList<Friend> friendHolder) {
        super(context, 0, friendHolder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Friend fr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView friendName = (TextView) convertView.findViewById(R.id.friend_name);
        TextView friendEmail = (TextView) convertView.findViewById(R.id.friend_email);
        friendName.setText(fr.friendName);
        friendEmail.setText(fr.friendEmail);
        return convertView;
    }
}
