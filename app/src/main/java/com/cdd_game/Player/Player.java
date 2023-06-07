package com.cdd_game.Player;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardGroup;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Game.Game;

public class Player {
    private String nickName;
    private CardPool ownCards;
    private String deviceID;

    //Constructor
    Player(String name,CardPool cardpool,String id){
        this.nickName=name;
        this.ownCards=cardpool;
        this.deviceID=id;
    }

    /**
     * @param cards
     * cards 代表玩家准备出的牌
     * @return
     */
    public boolean playCard(CardGroup cards){
        //TODO：首先检查出的牌是否符合规则(待修改）
        if(Game.getInstance().getRule().validate(cards)){
            //TODO：再检查出牌是否大于上家()
            /*if(Game.getGameInstance().getRule().checkAmount(cards,Game.getGameInstance().lastcards)){
                //将要出的牌从手牌中去除，并加入到弃牌堆中
                ownCards.removeCards(cards);
                Game.getGameInstance().getDiscardCards().addCards(cards);
                */
                return true;
            }
        return false;
}
    public boolean pass(){
        //TODO:
        return true;
    }

    /**
     * TODO:传入参数类型
     * @return
     */
    public boolean sortCard(){
        this.ownCards.sort();
        return true;
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

    public CardPool getOwnsCards() {
        return this.ownCards;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    public String getDeviceID(){
        return this.deviceID;
    }
}
