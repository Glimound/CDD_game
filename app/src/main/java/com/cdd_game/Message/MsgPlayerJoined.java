package com.cdd_game.Message;

public class MsgPlayerJoined extends MessageSchema {
    public String joinedPlayerID;
    public String joinedPlayerNickName;

    /**
     * Message的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param joinedPlayerID 加入游戏的玩家的设备ID
     * @param joinedPlayerNickName 加入游戏的玩家的用户昵称
     */
    public MsgPlayerJoined(long time, String deviceID, String nickName, String joinedPlayerID,
                           String joinedPlayerNickName) {
        super(time, deviceID, nickName, BehaviourType.PLAYER_JOINED);
        this.joinedPlayerID = joinedPlayerID;
        this.joinedPlayerNickName = joinedPlayerNickName;
    }
}
