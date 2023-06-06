package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardFactoryTest {

    CardFactory factory = new CardFactory();
    @Test
    public void createCard() {
        Card card1 = factory.createCard(Card.CLUB, Card.K);
        assertNotNull(card1);
        assertEquals(card1.getSuit(), Card.CLUB);
        assertEquals(card1.getRank(), Card.K);
        assertEquals(card1.getId(), CardFactory.getCardId() - 1);

        Card card2 = factory.createCard(Card.HEART, Card.BLACKJOKER);
        assertNotNull(card2);
        assertEquals(card2.getSuit(), Card.HEART);
        assertEquals(card2.getRank(), Card.BLACKJOKER);
        assertEquals(card2.getId(), CardFactory.getCardId() - 1);

        Card card3 = factory.createCard(Card.SPADE, Card.TWO);
        assertNotNull(card3);
        assertEquals(card3.getSuit(), Card.SPADE);
        assertEquals(card3.getRank(), Card.TWO);
        assertEquals(card3.getId(), CardFactory.getCardId() - 1);
    }

    @Test
    public void refreshCardID() {
        CardFactory.refreshCardID();
        assertEquals(CardFactory.getCardId(), 0);
    }

}