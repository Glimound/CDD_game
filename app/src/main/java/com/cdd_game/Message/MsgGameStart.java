package com.cdd_game.Message;

import com.cdd_game.Player.Player;

import java.util.ArrayList;

public class MsgGameStart extends MessageSchema {
    public String gameID;
    public ArrayList<Player> players;
    /**
     * MsgGameStart的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param gameID    游戏对局id
     * @param players   游戏玩家（游戏对局内顺序）
     */
    public MsgGameStart(long time, String deviceID, String nickName, String gameID, ArrayList<Player> players) {
        super(time, deviceID, nickName, BehaviourType.GAME_START);
        this.gameID = gameID;
        this.players = players;
    }
}
