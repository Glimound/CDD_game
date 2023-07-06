package com.cdd_game.Chat;

import android.provider.ContactsContract;

public class ChatData {
    private String text;
    private String nickName;

    public ChatData() {
    }

    public ChatData( String text,String nickName) {
        this.text = text;
        this.nickName = nickName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String  getNickName() {
        return nickName;
    }

    public void setNickName(int type) {
        this.nickName = nickName;
    }
}


