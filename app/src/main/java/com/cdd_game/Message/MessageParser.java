package com.cdd_game.Message;

import android.util.Log;

import com.cdd_game.Game.Game;
import com.cdd_game.MainActivity;
import com.cdd_game.Message.MessageSchema;
import com.cdd_game.Player.Player;
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
                    Game.getGameInstance().addPlayer(new Player(tmpMsg.deviceID, tmpMsg.nickName));

                    MessageSchema respondMsg = new MsgShakeHands(Calendar.getInstance().getTimeInMillis(),
                            activity.player.getDeviceID(), activity.player.getNickName(),
                            Game.getGameInstance().getPlayers(), Game.getGameInstance().getRule());
                    activity.connector.getConnectedThreadOfClient().write(respondMsg);

                    MessageSchema playerJoinedMsg = new MsgPlayerJoined(Calendar.getInstance().getTimeInMillis(),
                            activity.player.getDeviceID(), activity.player.getNickName(), tmpMsg.deviceID, tmpMsg.nickName);
                    for (Player player : Game.getGameInstance().getPlayers()) {
                        if (!player.getDeviceID().equals(activity.player.getDeviceID()) &&
                                !player.getDeviceID().equals(tmpMsg.deviceID)) {
                            activity.connector.getConnectedThreadsOfServer().get(player.getDeviceID()).write(playerJoinedMsg);
                        }
                    }
                } else if (activity.state == State.CLIENT_WAITING || activity.state == State.CLIENT_READY) {
                    Log.d("Message", "Client receive shake hand message.");
                    Game.getGameInstance().setPlayers(tmpMsg.players);
                    Game.getGameInstance().setRule(tmpMsg.rule);
                }
                break;
            case PLAYER_JOINED:
                if (activity.state == State.CLIENT_WAITING || activity.state == State.CLIENT_READY) {
                    Log.d("Message", "Client receive player joined message.");
                    MsgPlayerJoined tmpMsg2 = (MsgPlayerJoined) msg;
                    Game.getGameInstance().addPlayer(new Player(tmpMsg2.deviceID, tmpMsg2.nickName));
                }
        }
    }
}
