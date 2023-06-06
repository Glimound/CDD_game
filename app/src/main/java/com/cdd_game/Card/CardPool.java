package com.cdd_game.Card;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * 返回卡池中对应花色和点数的卡，若卡池中无牌或未找到则返回null。
     */
    public Card getCardBySuitAndRank(CardSuit suit, CardRank rank) {
        if (cards.isEmpty())
            return null;
        for (Card card : cards) {
            if (card.getSuit() == suit && card.getRank() == rank)
                return card;
        }
        return null;
    }

    /**
     * 返回卡池中第一张牌，若卡池中无牌则返回null。
     */
    public Card getFirstCard() {
        if (cards.isEmpty())
            return null;
        return cards.get(0);
    }

    /**
     * 返回卡池中的最后一张牌，若卡池中无牌则返回null。
     */
    public Card getLastCard() {
        if (cards.isEmpty())
            return null;
        return cards.get(cardCounter - 1);
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

    /**
     * 寻找卡池中的对应id的card对象。若卡池中存在该卡，则移除该卡，并返回被移除的card对象；
     * 若卡池为空或不存在该卡，则卡池不变并返回null。
     * @param id 目标Card对象的id
     */
    public Card removeCardByID(int id) {
        Card targetCard = null;
        if (cards.isEmpty())
            return null;
        for (Card card : cards) {
            if (card.getId() == id) {
                targetCard = card;
                break;
            }
        }
        return targetCard;
    }

    /**
     * 移除卡池中第一张牌并返回该Card实例，若卡池中无牌则返回null。
     */
    public Card removeFirstCard() {
        if (cards.isEmpty())
            return null;
        Card card = cards.get(0);
        cards.remove(card);
        return card;
    }

    /**
     * 移除卡池中的最后一张牌并返回该Card实例，若卡池中无牌则返回null。
     */
    public Card removeLastCard() {
        if (cards.isEmpty())
            return null;
        Card card = cards.get(cardCounter - 1);
        cards.remove(card);
        return card;
    }

    /**
     * TODO: 实现不同方式的排序。
     */
    public void sort() {
        Collections.sort(cards);
    }

    /**
     * TODO: 洗牌功能。
     */
    public void shuffle() {

    }
}
