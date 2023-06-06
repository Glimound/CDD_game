package com.cdd_game.Card;

public class CardGroup extends CardPool {
    /**
     * 牌组类型。
     */
    private String cardGroupType;

    CardGroup() {
        super();
    }

    CardGroup(String cardGroupType) {
        super();
        this.cardGroupType = cardGroupType;
    }

    public String getCardGroupType() {
        return cardGroupType;
    }

    public void setCardGroupType(String cardGroupType) {
        this.cardGroupType = cardGroupType;
    }
}
