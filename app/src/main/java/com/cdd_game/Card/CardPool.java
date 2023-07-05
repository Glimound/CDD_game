package com.cdd_game.Card;

import java.util.ArrayList;
import java.util.Collections;

public class CardPool {
    protected ArrayList<Card> cards;

    /**
     * 当前牌池中卡牌的数量。
     */
    protected int cardCounter;

    CardPool() {
        cards = new ArrayList<>();
        cardCounter = 0;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int size() {
        return cardCounter;
    }

    /**
     * 向卡池中添加一张卡。若卡池中已存在该卡，则卡池不变。
     */
    public void addCard(Card card) {
        if (!cards.contains(card)) {
            cards.add(card);
            cardCounter++;
        }
    }

    /**
     * 将cardPool中的卡加入该卡池。若卡池中已存在的卡不会重复加入。
     */
    public void addCards(CardPool cardPool) {
        if (cardPool.isEmpty())
            return;
        for (Card card : cardPool.cards) {
            if (!cards.contains(card)) {
                cards.add(card);
                cardCounter++;
            }
        }
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
     * 返回卡池中对应花色和点数的卡，若卡池中无牌或未找到则返回null。
     */
    public Card getCardBySuitAndRank(String suit, String rank) {
        if (cards.isEmpty())
            return null;
        for (Card card : cards) {
            if (card.getSuit().getName().equals(suit) && card.getRank().getName().equals(rank))
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
        cardCounter--;
        return targetCard;
    }

    /**
     * 移除卡池中包含在特定cardPool中的牌。若成功移除这些卡则返回true；
     * 若卡池为空则返回false。
     * @param cardPool CardPool实例
     */
    public boolean removeCards(CardPool cardPool) {
        if (cards.isEmpty())
            return false;
        if (cardPool.isEmpty())
            return true;
        for (Card card : cardPool.cards) {
            if (cards.contains(card)) {
                cards.remove(card);
                cardCounter--;
            }
        }
        return true;
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
        if (targetCard != null) {
            cards.remove(targetCard);
            cardCounter--;
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
        cardCounter--;
        return card;
    }

    /**
     * 删除并返回卡池中对应花色和点数的卡，若卡池中无牌或未找到则返回null。
     */
    public Card removeCardBySuitAndRank(CardSuit suit, CardRank rank) {
        Card card = getCardBySuitAndRank(suit, rank);
        if (card != null) {
            return removeCard(card);
        } else {
            return null;
        }
    }

    /**
     * 移除卡池中的最后一张牌并返回该Card实例，若卡池中无牌则返回null。
     */
    public Card removeLastCard() {
        if (cards.isEmpty())
            return null;
        Card card = cards.get(cardCounter - 1);
        cards.remove(card);
        cardCounter--;
        return card;
    }

    /**
     * TODO: 实现不同方式的排序。
     */
    public void sort() {
        Collections.sort(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * 判断牌型的函数
     */
    public boolean isSameRanks() {
        CardRank cardRank=cards.get(0).getRank();
        for(int i=1;i<cardCounter;i++){
            if(!cards.get(i).getRank().equals(cardRank)){
                return false;
            }
        }
        return true;
    }

    public boolean isSameSuits(){
        CardSuit cardSuit=cards.get(0).getSuit();
        for(int i=1;i<cardCounter;i++){
            if(!cards.get(i).getSuit().equals(cardSuit)){
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
       return (size() == 0);
    }

}
