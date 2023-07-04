package com.cdd_game.Message;

import com.cdd_game.Card.CardGroup;

public class MsgPlayCard extends MessageSchema{
    CardGroup cardGroup;
    /**
     * MsgPlayCard的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param cardGroup 打出的牌
     */
    public MsgPlayCard(long time, String deviceID, String nickName, CardGroup cardGroup) {
        super(time, deviceID, nickName, BehaviourType.PLAY_CARD);
        this.cardGroup = cardGroup;
    }
}
