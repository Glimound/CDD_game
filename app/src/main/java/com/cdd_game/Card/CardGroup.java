package com.cdd_game.Card;

import java.util.ArrayList;
import java.util.HashMap;


public class CardGroup extends CardPool{

    /**共以下几个状态
     * 未识别，单牌，对子，三牌，四带一，三带一对，杂顺，同花顺，同花五
     */


    private String cardGroupType;


    private Card maxCard;

    CardGroup(){
        super();

    }

    CardGroup(String cardGroupType){
        super();
        this.cardGroupType=cardGroupType;
    }

    public String getCardGroupType(){
        return cardGroupType;
    }

    public void setCardGroupType(String cardGroupType){
        this.cardGroupType=cardGroupType;
    }

    public Card getMaxCard(){
        return maxCard;
    }

    public void setMaxCard(Card maxCard) {
        this.maxCard = maxCard;
    }

    @Override
    public ArrayList<Card> getCards() {
        return cards;
    }

}
