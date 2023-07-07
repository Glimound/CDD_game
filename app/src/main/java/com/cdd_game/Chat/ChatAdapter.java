package com.cdd_game.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdd_game.Player.Player;
import com.cdd_game.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ChatData> myData;
    private Player player;
    public ChatAdapter() {}

    public ChatAdapter(ArrayList<ChatData> myData, Context mContext,Player player) {
        this.myData = myData;
        this.mContext = mContext;
        this.player=player;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView name;
        TextView textView;
        if(!player.getNickName().equals(myData.get(position).getNickName() )){
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.message_left,parent,false);
            name=convertView.findViewById(R.id.left_name);
            textView = convertView.findViewById(R.id.txt_left);
            name.setText(myData.get(position).getNickName()+": ");
        }
        else {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.message_right,parent,false);
            name = convertView.findViewById(R.id.right_name);
            textView = convertView.findViewById(R.id.txt_right);
            name.setText(" :"+player.getNickName());
        }
        textView.setText(myData.get(position).getText());

        return convertView;
    }


}

