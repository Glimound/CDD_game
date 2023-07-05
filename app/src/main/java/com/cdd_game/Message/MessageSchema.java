package com.cdd_game.Message;

public class MessageSchema {
    public long time;
    public String deviceID;
    public String nickName;
    public BehaviourType behaviour;


    /**
     * Message的构造函数。不使用的参数项应设为null。
     *
     * @param time 时间戳
     * @param deviceID 行为发出者的设备ID，应当为设备的蓝牙mac地址
     * @param nickName 行为发出者的用户昵称
     * @param behaviour 行为类型
     */
    public MessageSchema(long time, String deviceID, String nickName, BehaviourType behaviour) {
        this.time = time;
        this.deviceID = deviceID;
        this.nickName = nickName;
        this.behaviour = behaviour;
    }
}
