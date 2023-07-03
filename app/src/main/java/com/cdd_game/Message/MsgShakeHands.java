package com.cdd_game.Message;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.NormalRule;
import com.cdd_game.Rule.Rule;

import java.util.ArrayList;
import java.util.Set;

public class MsgShakeHands extends MessageSchema {
    public NormalRule rule;
    public ArrayList<Player> players;
    public int playerNumLimit;
    public Player winner;

    /**
     * MsgShakeHands的构造函数。不使用的参数项应设为null。
     *
     * @param time              时间戳
     * @param deviceID          行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName          行为发出者的用户昵称
     * @param players           players实例，用于同步信息
     * @param rule              rule实例，用于同步信息
     * @param playerNumLimit    该房间玩家数量上限，用于同步信息
     * @param winner            该房间上局胜利玩家，用于同步信息
     */
    public MsgShakeHands(long time, String deviceID, String nickName, ArrayList<Player> players,
                         NormalRule rule, int playerNumLimit, Player winner) {
        super(time, deviceID, nickName, BehaviourType.SHAKE_HAND);
        this.players = players;
        this.rule = rule;
        this.playerNumLimit = playerNumLimit;
        this.winner = winner;
    }
}
