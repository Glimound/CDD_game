package com.cdd_game.Message;

public class MsgReady extends MessageSchema {
    /**
     * MsgReady的构造函数。不使用的参数项应设为null。
     *
     * @param time      时间戳
     * @param deviceID  行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName  行为发出者的用户昵称
     */
    public MsgReady(long time, String deviceID, String nickName) {
        super(time, deviceID, nickName, BehaviourType.READY);
    }
}
