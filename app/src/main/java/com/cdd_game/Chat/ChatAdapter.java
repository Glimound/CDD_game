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

import java.util.LinkedList;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<ChatData> myData;
    private Player player;
    public ChatAdapter() {}

    public ChatAdapter(LinkedList<ChatData> myData, Context mContext,Player player) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        if(player.getNickName().equals(myData.get(position).getNickName() )){
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.message_left,parent,false);
            imageView = convertView.findViewById(R.id.image_left);
            textView = convertView.findViewById(R.id.txt_left);
        }
        else {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.message_right,parent,false);
            imageView = convertView.findViewById(R.id.image_right);
            textView = convertView.findViewById(R.id.txt_right);
        }
        imageView.setImageResource(R.drawable.back);
        textView.setText(myData.get(position).getText());

        return convertView;
    }

}

