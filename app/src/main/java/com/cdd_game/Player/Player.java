package com.cdd_game.Player;

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


    // TODO：胜局 称号？

    //Constructor
    public Player(String deviceID, String nickName){
        this.nickName = nickName;
        this.deviceID = deviceID;
    }

    /**
     * @param cards
     * cards 代表玩家准备出的牌
     * @return
     */
    public void playCard(CardGroup cards){
        //TODO：首先检查出的牌是否符合规则(待修改）
        if(Game.getGameInstance().getRule().validate(cards)){
            //TODO：再检查出牌是否大于上家()
            /*if(Game.getGameInstance().getRule().checkAmount(cards,Game.getGameInstance().lastcards)){
                //将要出的牌从手牌中去除，并加入到弃牌堆中
                ownCards.removeCards(cards);
                Game.getGameInstance().getDiscardCards().addCards(cards);
                */
            // 切换状态
        } else {
            // 发送消息
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

}
