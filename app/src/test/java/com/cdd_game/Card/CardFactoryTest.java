package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardFactoryTest {

    CardFactory factory = new CardFactory();
    @Test
    public void createCard() throws Exception {
        Card card1 = factory.createCard(CardSuit.CLUB, CardRank.Card_K);
        assertNotNull(card1);
        assertEquals(card1.getSuit(), CardSuit.CLUB);
        assertEquals(card1.getRank(), CardRank.Card_K);
        assertEquals(card1.getId(), CardFactory.getCardId() - 1);

        Card card2 = factory.createCard(CardSuit.SPADE, CardRank.Card_2);
        assertNotNull(card2);
        assertEquals(card2.getSuit(), CardSuit.SPADE);
        assertEquals(card2.getRank(), CardRank.Card_2);
        assertEquals(card2.getId(), CardFactory.getCardId() - 1);

        Card card3 = factory.createCard(CardSuit.BLACK, CardRank.Card_Joker);
        assertNotNull(card3);
        assertEquals(card3.getSuit(), CardSuit.BLACK);
        assertEquals(card3.getRank(), CardRank.Card_Joker);
        assertEquals(card3.getId(), CardFactory.getCardId() - 1);


        assertThrows(Exception.class,
                () -> {
                    factory.createCard(CardSuit.HEART, CardRank.Card_Joker);
                });

        assertThrows(Exception.class,
                () -> {
                    factory.createCard(CardSuit.RED, CardRank.Card_K);
                });

        assertThrows(Exception.class,
                () -> {
                    factory.createCard(CardSuit.BLACK, CardRank.Card_2);
                });

    }

    @Test
    public void refreshCardID() {
        CardFactory.refreshCardID();
        assertEquals(CardFactory.getCardId(), 0);
    }

}