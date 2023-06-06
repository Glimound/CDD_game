package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {
    Card card1 = new Card(1, Card.DIAMOND, Card.FIVE);
    Card card2 = new Card(2, Card.CLUB, Card.A);
    Card card3 = new Card(3, Card.HEART, Card.REDJOKER);
    Card card4 = new Card(4, Card.SPADE, Card.K);

    @Test
    public void testSuit() {
        assertTrue(card1.getSuit() < card2.getSuit());
        assertTrue(card2.getSuit() < card3.getSuit());
        assertTrue(card3.getSuit() < card4.getSuit());
    }

    @Test
    public void testRank() {
        assertTrue(card1.getRank() < card2.getRank());
        assertTrue(card2.getRank() < card3.getRank());
        assertTrue(card3.getRank() > card4.getRank());
    }

    @Test
    public void getId() {
        assertEquals(card1.getId(), 1);
        assertEquals(card4.getId(), 4);
    }

    @Test
    public void getRank() {
        assertEquals(card2.getRank(), Card.A);
        assertEquals(card4.getRank(), Card.K);
    }

    @Test
    public void setRank() {
        card1.setRank(Card.J);
        card2.setRank(Card.BLACKJOKER);
        card3.setRank(Card.A);
        card4.setRank(Card.TWO);

        assertEquals(card1.getRank(), Card.J);
        assertEquals(card2.getRank(), Card.BLACKJOKER);
        assertEquals(card3.getRank(), Card.A);
        assertEquals(card4.getRank(), Card.TWO);
    }

    @Test
    public void getSuit() {
        assertEquals(card1.getSuit(), Card.DIAMOND);
        assertEquals(card4.getSuit(), Card.SPADE);
    }

    @Test
    public void setSuit() {
        card1.setSuit(Card.SPADE);
        card2.setSuit(Card.DIAMOND);
        card3.setSuit(Card.CLUB);
        card4.setSuit(Card.HEART);

        assertEquals(card1.getSuit(), Card.SPADE);
        assertEquals(card2.getSuit(), Card.DIAMOND);
        assertEquals(card3.getSuit(), Card.CLUB);
        assertEquals(card4.getSuit(), Card.HEART);
    }


}