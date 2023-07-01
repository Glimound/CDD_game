package com.cdd_game.Connection;

import com.cdd_game.Card.CardGroup;

/**
 * 行为类型：
 * PLAY_CARD: 出牌，
 * PASS: 跳过出牌，
 * WIN: 玩家获胜，
 */
enum BehaviourType {
    PLAY_CARD,
    PASS,
    WIN;
}

public class MessageSchema {
    public String time;
    public String deviceID;
    public String nickName;
    public BehaviourType behaviour;
    public String value;
    public CardGroup cardGroup;

    /**
     * Message的构造函数。不使用的参数项应设为null。
     * @param time 时间戳
     * @param deviceID 行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName 行为发出者的用户昵称
     * @param behaviour 行为类型
     * @param value 值
     * @param cardGroup 携带的牌组
     */
    public MessageSchema(String time, String deviceID, String nickName,
                         BehaviourType behaviour, String value, CardGroup cardGroup) {
        this.time = time;
        this.deviceID = deviceID;
        this.nickName = nickName;
        this.behaviour = behaviour;
        this.value = value;
        this.cardGroup = cardGroup;
    }
}
