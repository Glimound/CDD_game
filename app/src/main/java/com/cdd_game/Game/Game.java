package com.cdd_game.Game;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.Rule;

import java.util.Set;

public class Game {
    private String gameID;
    private Game gameInstance;
    private CardPool initialCards;
    private CardPool discardCards;
    private Rule rule;
    private Set<Player> players;

    private static class LazyHolder {
        private static final Game INSTANCE = new Game();
    }
    private Game (){}
    public static final Game getInstance() {
            return LazyHolder.INSTANCE;
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
}
