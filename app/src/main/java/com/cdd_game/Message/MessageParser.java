package com.cdd_game.Message;

import android.util.Log;

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
                    GameRoom.getGameRoomInstance().addPlayer(new Player(tmpMsg.deviceID, tmpMsg.nickName));

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
                } else if (activity.state == State.CLIENT_SCANNING_GAME_ROOM) {
                    Log.d("Message", "Client receive shake hand message.");
                    GameRoom.createGameRoom(tmpMsg.rule, tmpMsg.playerNumLimit, tmpMsg.winner, tmpMsg.players);

                    // 切换ui，进入游戏准备界面
                    activity.state = State.CLIENT_WAITING;
                    activity.setContentView(R.layout.waiting1);
                    activity.waiting();
                    // TODO: 更新UI，显示房间内已有的玩家
                }
                break;

            case PLAYER_JOINED:
                if (activity.state == State.CLIENT_WAITING || activity.state == State.CLIENT_READY) {
                    Log.d("Message", "Client receive player joined message.");
                    MsgPlayerJoined tmpMsg2 = (MsgPlayerJoined) msg;
                    GameRoom.getGameRoomInstance().addPlayer(new Player(tmpMsg2.deviceID, tmpMsg2.nickName));
                    // TODO: 更新UI，显示加入的玩家
                }
                break;

            case READY:
                MsgReady tmpMsg3 = (MsgReady) msg;
                if (activity.state == State.SERVER_WAITING) {
                    Log.d("Message", "Server receive player ready message.");
                    GameRoom.getGameRoomInstance().getPlayerByNickName(msg.nickName).setReady(true);
                    // TODO: 更新UI，显示该玩家的已准备状态

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
                }
                break;

            case GAME_START:
                MsgGameStart tmpMsg4 = (MsgGameStart) msg;
                if (activity.state == State.CLIENT_READY) {
                    GameRoom.getGameRoomInstance().createGame(tmpMsg4.gameID);
                    Game.getGameInstance().setPlayers(tmpMsg4.players);
                    activity.state = State.CLIENT_PLAYING;
                    activity.setContentView(R.layout.game_ui);
                    activity.game();
                }
                break;
        }
    }
}
