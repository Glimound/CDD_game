package com.cdd_game.Message;

public class MsgGameEnd extends MessageSchema {
    String winnerNickName;
    /**
     * MsgGameEnd的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     * @param winnerNickName 赢家昵称
     */
    public MsgGameEnd(long time, String deviceID, String nickName, String winnerNickName) {
        super(time, deviceID, nickName, BehaviourType.GAME_END);
        this.winnerNickName = winnerNickName;
    }
}
