package com.cdd_game.Message;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cdd_game.Game.Game;
import com.cdd_game.Game.GameRoom;
import com.cdd_game.MainActivity;
import com.cdd_game.Message.MessageSchema;
import com.cdd_game.Player.Player;
import com.cdd_game.R;
import com.cdd_game.Rule.NormalRule;
import com.cdd_game.State;

import java.util.Calendar;

public class MessageParser {
    private MainActivity activity;


    public MessageParser(MainActivity activity) {
        this.activity = activity;
    }

    public void parseMessage(MessageSchema msg) {
        switch (msg.behaviour) {
            case SHAKE_HAND:
                MsgShakeHands tmpMsg = (MsgShakeHands) msg;
                if (activity.state == State.SERVER_WAITING) {
                    Log.d("Message", "Server receive shake hand message.");
                    // 修改MAC-线程映射为nickName-线程映射
                    activity.connector.getConnectedThreadsOfServer().put(tmpMsg.nickName, activity.connector.getConnectedThreadsOfServer().get(activity.tmpMAC));
                    activity.connector.getConnectedThreadsOfServer().remove(activity.tmpMAC);
                    Player playerAdd=new Player(tmpMsg.deviceID, tmpMsg.nickName);
                    GameRoom.getGameRoomInstance().addPlayer(playerAdd);

                    MessageSchema respondMsg = new MsgShakeHands(Calendar.getInstance().getTimeInMillis(),
                            activity.player.getDeviceID(), activity.player.getNickName(),
                            GameRoom.getGameRoomInstance().getPlayers(), (NormalRule) GameRoom.getGameRoomInstance().getRule(),
                            GameRoom.getGameRoomInstance().getPlayerNumLimit(), GameRoom.getGameRoomInstance().getWinner());
                    activity.connector.getConnectedThreadsOfServer().get(tmpMsg.nickName).write(respondMsg);

                    MessageSchema playerJoinedMsg = new MsgPlayerJoined(Calendar.getInstance().getTimeInMillis(),
                            activity.player.getDeviceID(), activity.player.getNickName(), tmpMsg.deviceID, tmpMsg.nickName);
                    for (Player player : GameRoom.getGameRoomInstance().getPlayers()) {
                        if (!player.getNickName().equals(activity.player.getNickName()) &&
                                !player.getNickName().equals(tmpMsg.nickName)) {
                            activity.connector.getConnectedThreadsOfServer().get(player.getNickName()).write(playerJoinedMsg);
                        }
                    }

                    // TODO: 玩家加入房间，更新UI，显示该玩家

                    int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(playerAdd);
                    switch(num){
                        case 1:
                            activity.imageB1.setVisibility(View.VISIBLE);
                        case 2:
                            activity.imageC1.setVisibility(View.VISIBLE);
                        case 3:
                            activity.imageD1.setVisibility(View.VISIBLE);
                    }



                } else if (activity.state == State.CLIENT_SCANNING_GAME_ROOM) {
                    Log.d("Message", "Client receive shake hand message.");
                    GameRoom.createGameRoom(tmpMsg.rule, tmpMsg.playerNumLimit, tmpMsg.winner, tmpMsg.players);

                    // 切换ui，进入游戏准备界面
                    activity.state = State.CLIENT_WAITING;
                    activity.setContentView(R.layout.waiting1);
                    activity.waiting();
                    Player tempPlayer=GameRoom.getGameRoomInstance().getPlayerByNickName(activity.player.getNickName());
                    int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(tempPlayer);
                    switch(num){
                        case 1:
                            activity.imageD1.setVisibility(View.VISIBLE);
                        case 2:
                            activity.imageD1.setVisibility(View.VISIBLE);
                            activity.imageC1.setVisibility(View.VISIBLE);
                        case 3:
                            activity.imageB1.setVisibility(View.VISIBLE);
                            activity.imageD1.setVisibility(View.VISIBLE);
                            activity.imageC1.setVisibility(View.VISIBLE);
                    }
                    // TODO: 更新UI，显示房间内已有的玩家
                }
                break;

            case PLAYER_JOINED:
                if (activity.state == State.CLIENT_WAITING || activity.state == State.CLIENT_READY) {
                    Log.d("Message", "Client receive player joined message.");
                    MsgPlayerJoined tmpMsg2 = (MsgPlayerJoined) msg;
                    Player playerAdd=new Player(tmpMsg2.deviceID, tmpMsg2.nickName);
                    GameRoom.getGameRoomInstance().addPlayer(playerAdd);
                    // TODO: 更新UI，显示加入的玩家
                    int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(playerAdd);
                    int num1=GameRoom.getGameRoomInstance().getPlayers().indexOf(activity.player);
                    int offset=num-num1;
                    switch(offset){
                        case 1:
                            activity.imageB1.setVisibility(View.VISIBLE);
                        case 2:
                            activity.imageC1.setVisibility(View.VISIBLE);
                        case 3:
                            activity.imageD1.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case READY:
                MsgReady tmpMsg3 = (MsgReady) msg;
                if (activity.state == State.SERVER_WAITING) {
                    Log.d("Message", "Server receive player ready message.");
                    GameRoom.getGameRoomInstance().getPlayerByNickName(msg.nickName).setReady(true);
                    // TODO: 更新UI，显示该玩家的已准备状态
                    Player tempPlayer=GameRoom.getGameRoomInstance().getPlayerByNickName(msg.nickName);
                    int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(tempPlayer);
                    int num1=GameRoom.getGameRoomInstance().getPlayers().indexOf(activity.player);
                    int offset=num-num1;
                    switch(offset){
                        case 1:
                            activity.imageB.setVisibility(View.VISIBLE);
                        case 2:
                            activity.imageC.setVisibility(View.VISIBLE);
                        case 3:
                            activity.imageD.setVisibility(View.VISIBLE);
                    }

                    for (Player player : GameRoom.getGameRoomInstance().getPlayers()) {
                        if (!player.getNickName().equals(activity.player.getNickName()) &&
                                !player.getNickName().equals(tmpMsg3.nickName)) {
                            activity.connector.getConnectedThreadsOfServer().get(player.getNickName()).write(msg);
                        }
                    }
                } else if (activity.state == State.CLIENT_WAITING) {
                    Log.d("Message", "Client receive player ready message.");
                    GameRoom.getGameRoomInstance().getPlayerByNickName(msg.nickName).setReady(true);
                    // TODO: 更新UI，显示该玩家的已准备状态
                    Player tempPlayer=GameRoom.getGameRoomInstance().getPlayerByNickName(msg.nickName);
                    int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(tempPlayer);
                    int num1=GameRoom.getGameRoomInstance().getPlayers().indexOf(activity.player);
                    int offset=num-num1;
                    switch(offset){
                        case 1:
                            activity.imageB.setVisibility(View.VISIBLE);
                        case 2:
                            activity.imageC.setVisibility(View.VISIBLE);
                        case 3:
                            activity.imageD.setVisibility(View.VISIBLE);
                        case -1:
                            activity.imageD.setVisibility(View.VISIBLE);
                        case -2:
                            activity.imageC.setVisibility(View.VISIBLE);
                        case -3:
                            activity.imageB.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}
