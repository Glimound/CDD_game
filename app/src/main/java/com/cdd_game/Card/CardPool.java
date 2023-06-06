package com.cdd_game.Card;

import java.util.ArrayList;

public class CardPool {
    private ArrayList<Card> cards;

    /**
     * 当前牌池中卡牌的数量。
     */
    private int cardCounter;

    CardPool() {
        cards = new ArrayList<>();
        cardCounter = 0;
    }

    public int getCardCounter() {
        return cardCounter;
    }

    public void addCard(Card card) {
        cards.add(card);
        cardCounter++;
    }

    public void addCards(CardPool cardPool) {
        cards.addAll(cardPool.cards);
        cardCounter++;
    }

    /**
     * 寻找卡池中的card对象。若卡池中存在该卡，则返回该card对象；
     * 若卡池为空或不存在该卡，则卡池不变并返回null。
     * @param id 目标Card对象的id
     */
    public Card getCardByID(int id) {
        if (cards.isEmpty())
            return null;
        for (Card card : cards) {
            if (card.getId() == id)
                return card;
        }
        return null;
    }

    public Card getCardBySuitAndRank() {

        return null;
    }

    /**
     * 寻找并移除卡池中的card对象。若卡池中存在该卡，则移除该卡，并返回被移除的card对象；
     * 若卡池为空或不存在该卡，则卡池不变并返回null。
     * @param card Card实例
     */
    public Card removeCard(Card card) {
        if (cards.isEmpty())
            return null;
        if (!cards.contains(card))
            return null;
        Card targetCard = cards.get(cards.indexOf(card));
        cards.remove(targetCard);
        return targetCard;
    }

    /**
     * 将cardPool中包含的牌。若找到并成功移除这些卡则返回true；
     * 若卡池为空或不存在这些卡，则卡池不变并返回false。
     * ！！！！待测试 部分有（交集情况）
     * @param cardPool CardPool实例
     */
    public boolean removeCards(CardPool cardPool) {
        return cards.removeAll(cardPool.cards);
    }

    public Card removeCardByID() {

        return null;
    }

    public void sort() {

    }

    public void shuffle() {

    }

    public void findMinCardBySuit() {

    }

    public void findMinCardByRank() {

    }

}
