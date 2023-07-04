package com.cdd_game.Player;

import android.util.Log;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardGroup;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Game.Game;

/**
 * 玩家类，实例存储在server上，玩家涉及后台的任何属性及操作都需要双向绑定至该类（可通过controller层作为中继）
 */
public class Player {
    private String nickName;
    private CardPool ownCards;
    private String deviceID;
    private boolean isReady;

    // TODO：胜局 称号？

    //Constructor
    public Player(String deviceID, String nickName){
        this.nickName = nickName;
        this.deviceID = deviceID;
        this.isReady = false;
        try {
            this.ownCards = new CardPoolFactory().createCardPool("empty");
        } catch (Exception e) {
            Log.e("Game", "Create empty card pool failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param cards
     * cards 代表玩家准备出的牌
     * TODO:切换玩家状态，并发送相应消息；玩家的出牌函数应该包含哪些？
     * @return
     */
    public void playCard(CardGroup cards) {
        if (Game.getGameInstance().getRule().validate(cards)) {
            if (Game.getGameInstance().getRule().compareToCards(cards, Game.getGameInstance().getLastCards())) {
                //将要出的牌从手牌中去除，加入到弃牌堆中,并设置为上家出的牌
                ownCards.removeCards(cards);
                Game.getGameInstance().getDiscardCards().addCards(cards);
                // 切换状态
            } else {
                // 发送消息
            }
        }
    }
    public void pass(){
        Game.getGameInstance().gameTurnPlusOne();
        Game.getGameInstance().nextTurn();
        // 切换状态
    }

    /**
     * TODO:传入参数类型
     * @return
     */
    public void sortCard(){
        this.ownCards.sort();
        // 发送消息 display
    }

    //数据成员的set与get
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName(){
        return this.nickName;
    }

    public void setOwnCards(CardPool ownCards) {
        this.ownCards = ownCards;
    }

    public CardPool getOwnCards() {
        return this.ownCards;
    }

    public String getDeviceID(){
        return this.deviceID;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

}
