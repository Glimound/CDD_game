package com.cdd_game.Game;

import com.cdd_game.Player.Player;
import com.cdd_game.Rule.Rule;

import java.util.ArrayList;

/**
 * 游戏房间单例类。
 * 每一个房间都应有不同且唯一的GameRoom实例。
 */
public class GameRoom {
    // 包含server和client的deviceID，Rule，player的集合（按照加入时间排序），上一局赢的player
    // 包含添加玩家、设置规则方法、getNextPalyer（获得下家）、setRule、允许搜索、拒绝搜索、解散房间、退出房间

    private static GameRoom gameRoomInstance = null;
    private Game game;
    private Rule rule;
    /**
     * 加入房间的玩家。按照玩家加入房间的顺序排序。
     */
    private ArrayList<Player> players;
    int playerNumLimit;
    private Player winner;


    private GameRoom() {

    }

    public static GameRoom getGameRoomInstance() {
        return gameRoomInstance;
    }

    /**
     * 返回目标玩家的下家（按照加入房间的顺序）。只用于Game实例中设置玩家出牌顺序，不用于控制游戏循环。
     * @param player 目标玩家
     */
    public Player getNextPlayer(Player player) {
        int index = players.indexOf(player);
        // TODO: if (index == ruleStrategy.getPlayerNumRule().getPlayerNum() - 1)
        if (index == 3)
            return players.get(0);
        else
            return players.get(++index);
    }

    /**
     * 玩家加入房间
     */
    public void addPlayer(String deviceID, String nickName) {   // 以及称号等
        Player player = new Player(deviceID, nickName);
        players.add(player);
    }

    public void removePlayer(String deviceID) {
        Player player = null;
        for (Player playerTmp : players) {
            if (playerTmp.getDeviceID().equals(deviceID)) {
                player = playerTmp;
                break;
            }
        }
        if (winner == player)
            winner = null;
        players.remove(player);
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
