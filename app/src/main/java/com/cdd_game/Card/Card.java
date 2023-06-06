package com.cdd_game.Card;

public class Card {
    /**
     * DIAMOND：方片；CLUB：梅花；HEART：红桃；SPADE：黑桃。从小到大排序。
     */
    public static final int DIAMOND = 0, CLUB = 1, HEART = 2, SPADE = 3;

    /**
     * 点数：从小到大排序。
     */
    public static final int THREE = 0, FOUR = 1, FIVE = 2, SIX = 3, SEVEN = 4, EIGHT = 5, NINE = 6,
            TEN = 7, J = 8, Q = 9, K = 10, A = 11, TWO = 12, BLACKJOKER = 13, REDJOKER = 14;

    private final int id;
    private int suit;
    private int rank;

    Card(int id, int suit, int rank) {
        this.id = id;
        this.suit = suit;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }
}