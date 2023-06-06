package com.cdd_game.Card;

public class CardFactory {
    private static int CARD_ID = 0;

    public Card createCard(CardSuit suit, CardRank rank) throws Exception {
        if (rank.name.equals("Joker") && !(suit.name.equals("RED") || suit.name.equals("BLACK")))
            throw new Exception("Invalid suit. Joker's suit can only be RED or BLACK.");

        if (!rank.name.equals("Joker") && (suit.name.equals("RED") || suit.name.equals("BLACK")))
            throw new Exception("Invalid suit. RED or BLACK can only be Joker's suit.");

        return new Card(CARD_ID++, suit, rank);
    }

    public static void refreshCardID() {
        CARD_ID = 0;
    }

    public static int getCardId() {
        return CARD_ID;
    }
}
