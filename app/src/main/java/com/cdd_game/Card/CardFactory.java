package com.cdd_game.Card;

public class CardFactory {
    private static int CARD_ID = 0;

    public Card createCard(int suit, int rank) {
        return new Card(CARD_ID++, suit, rank);
    }

    public static void refreshCardID() {
        CARD_ID = 0;
    }

    public static int getCardId() {
        return CARD_ID;
    }
}
