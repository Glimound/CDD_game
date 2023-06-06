package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class CardTest {
    Card card1 = new Card(1, CardSuit.DIAMOND, CardRank.Card_5);
    Card card2 = new Card(2, CardSuit.CLUB, CardRank.Card_A);
    Card card3 = new Card(3, CardSuit.RED, CardRank.Card_Joker);
    Card card4 = new Card(4, CardSuit.SPADE, CardRank.Card_K);

    @Test
    public void testSuit() {
        assertTrue(card1.getSuit().getWeight() < card2.getSuit().getWeight());
        assertTrue(card2.getSuit().getWeight() < card4.getSuit().getWeight());
    }

    @Test
    public void testRank() {
        assertTrue(card1.getRank().getWeight() < card2.getRank().getWeight());
        assertTrue(card2.getRank().getWeight() < card3.getRank().getWeight());
        assertTrue(card3.getRank().getWeight() > card4.getRank().getWeight());
    }

    @Test
    public void getId() {
        assertEquals(card1.getId(), 1);
        assertEquals(card4.getId(), 4);
    }

    @Test
    public void getRank() {
        assertEquals(card2.getRank(), CardRank.Card_A);
        assertEquals(card4.getRank(), CardRank.Card_K);
    }

    @Test
    public void getSuit() {
        assertEquals(card1.getSuit(), CardSuit.DIAMOND);
        assertEquals(card4.getSuit(), CardSuit.SPADE);
    }

    @Test
    public void compareToTest() {
        ArrayList<Card> cardList = new ArrayList<>();
        Card card5 = new Card(5, CardSuit.SPADE, CardRank.Card_J);
        Card card6 = new Card(6, CardSuit.HEART, CardRank.Card_J);
        Card card7 = new Card(7, CardSuit.SPADE, CardRank.Card_A);
        Card card8 = new Card(8, CardSuit.DIAMOND, CardRank.Card_3);
        Card card9 = new Card(9, CardSuit.BLACK, CardRank.Card_Joker);

        cardList.add(card5);
        cardList.add(card6);
        cardList.add(card7);
        cardList.add(card8);
        cardList.add(card9);

        java.util.Collections.sort(cardList);
        assertEquals(cardList.get(0), card8);
        assertEquals(cardList.get(1), card6);
        assertEquals(cardList.get(2), card5);
        assertEquals(cardList.get(3), card7);
        assertEquals(cardList.get(4), card9);
    }

}