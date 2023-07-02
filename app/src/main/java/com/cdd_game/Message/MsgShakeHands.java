package com.cdd_game.Message;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.Rule;

import java.util.Set;

public class MsgShakeHands extends MessageSchema {
    public Rule rule;
    public Set<Player> players;

    /**
     * MsgShakeHands的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param players   players实例，用于同步信息
     * @param rule      rule实例，用于同步信息
     */
    public MsgShakeHands(long time, String deviceID, String nickName, Set<Player> players, Rule rule) {
        super(time, deviceID, nickName, BehaviourType.SHAKE_HAND);
        this.players = players;
        this.rule = rule;
    }
}
