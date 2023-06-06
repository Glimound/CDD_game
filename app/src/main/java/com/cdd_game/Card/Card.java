package com.cdd_game.Card;

public class Card implements Comparable<Card> {

    private final int id;
    private CardSuit suit;
    private CardRank rank;

    Card(int id, CardSuit suit, CardRank rank) {
        this.id = id;
        this.suit = suit;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public CardRank getRank() {
        return rank;
    }

    private void setRank(CardRank rank) {
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    private void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    @Override
    public int compareTo(Card card) {
        if (getRank().getWeight() > card.getRank().getWeight()) {
            return 1;
        } else if (getRank().getWeight() < card.getRank().getWeight()) {
            return -1;
        } else {
            if (getSuit().getWeight() > card.getSuit().getWeight()) {
                return 1;
            } else if (getSuit().getWeight() < card.getSuit().getWeight()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
