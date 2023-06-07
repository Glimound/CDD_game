package com.cdd_game.Game;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.Rule;

import java.util.Set;

public class Game {
    private String gameID;
    private CardPool initialCards;
    private CardPool discardCards;
    private Rule rule;
    private Set<Player> players;

    //懒汉式单例类.在第一次调用的时候实例化自己
    //三个重载
    private Game(String gameID,CardPool initialCards,CardPool discardCards,Rule rule ,Player player) {
        this.gameID=gameID;
        this.initialCards=initialCards;
        this.discardCards=discardCards;
        this.rule=rule;
        this.players.add(player);

    }
    private Game(String gameID,CardPool initialCards,CardPool discardCards,Rule rule ,Set<Player> players) {
        this.gameID=gameID;
        this.initialCards=initialCards;
        this.discardCards=discardCards;
        this.rule=rule;
        this.players=players;

    }
    private Game(){}
    private static Game game=null;
    //静态工厂方法
    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    //TODO:
    public boolean initialize(){
        return true;
    }

    //TODO:
    public boolean dealCards(){
        return true;
    }

    public boolean addPlayer(Player player){
        players.add(player);
        return true;
    }

    public boolean removePlayer(Player player){
        players.remove(player);
        return true;
    }

    //数据成员的set和get函数
    public void setRule(Rule rule){
        this.rule=rule;
    }
    public Rule getRule(){
        return rule;
    }

    public void setInitialCards(CardPool initialCards){
        this.initialCards=initialCards;
    }
    public CardPool getInitialCards() {
        return initialCards;
    }

    public void setDiscardCards(CardPool discardCards){
        this.discardCards=discardCards;
    }
    public CardPool getDiscardCards() {
        return discardCards;
    }

    public void setGameID(String gameID){
        this.gameID=gameID;
    }
    public String getGameID() {
        return gameID;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
    public Set<Player> getPlayers() {
        return players;
    }
}
