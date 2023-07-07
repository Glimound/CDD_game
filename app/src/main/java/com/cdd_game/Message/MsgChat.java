package com.cdd_game.Message;

import com.cdd_game.Chat.ChatData;

public class MsgChat extends MessageSchema {
    public ChatData chat;
    /**
     * MsgChat的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param chat      chatData实例
     */
    public MsgChat(long time, String deviceID, String nickName, ChatData chat) {
        super(time, deviceID, nickName, BehaviourType.CHAT);
        this.chat = chat;
    }
}
