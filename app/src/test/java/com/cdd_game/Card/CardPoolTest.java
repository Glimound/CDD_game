package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class CardPoolTest {

    static CardPool cardPoolWith3AndJoker = new CardPool();
    static CardPool cardPoolWith2AndKAndJoker = new CardPool();
    static CardPool emptyCardPool = new CardPool();

    static Card card3CLUB, card3HEART, card3DIAMOND, card3SPADE, cardREDJOKER, cardBLACKJOKER;
    static Card card2CLUB, card2HEART, card2DIAMOND, card2SPADE,
            cardKCLUB, cardKHEART, cardKDIAMOND, cardKSPADE;
    static CardFactory factory = new CardFactory();

    @BeforeClass
    public static void initialize() throws Exception {
        card3CLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_3);
        card3HEART = factory.createCard(CardSuit.HEART, CardRank.Card_3);
        card3DIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_3);
        card3SPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_3);
        cardREDJOKER = factory.createCard(CardSuit.RED, CardRank.Card_Joker);
        cardBLACKJOKER = factory.createCard(CardSuit.BLACK, CardRank.Card_Joker);

        cardPoolWith3AndJoker.addCard(card3CLUB);
        cardPoolWith3AndJoker.addCard(card3HEART);
        cardPoolWith3AndJoker.addCard(card3DIAMOND);
        cardPoolWith3AndJoker.addCard(card3SPADE);
        cardPoolWith3AndJoker.addCard(cardREDJOKER);
        cardPoolWith3AndJoker.addCard(cardBLACKJOKER);

        card2CLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_2);
        card2HEART = factory.createCard(CardSuit.HEART, CardRank.Card_2);
        card2DIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_2);
        card2SPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_2);
        cardKCLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_K);
        cardKHEART = factory.createCard(CardSuit.HEART, CardRank.Card_K);
        cardKDIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_K);
        cardKSPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_K);

        cardPoolWith2AndKAndJoker.addCard(card2CLUB);
        cardPoolWith2AndKAndJoker.addCard(card2HEART);
        cardPoolWith2AndKAndJoker.addCard(card2DIAMOND);
        cardPoolWith2AndKAndJoker.addCard(card2SPADE);
        cardPoolWith2AndKAndJoker.addCard(cardKCLUB);
        cardPoolWith2AndKAndJoker.addCard(cardKHEART);
        cardPoolWith2AndKAndJoker.addCard(cardKDIAMOND);
        cardPoolWith2AndKAndJoker.addCard(cardKSPADE);
        cardPoolWith2AndKAndJoker.addCard(cardREDJOKER);
        cardPoolWith2AndKAndJoker.addCard(cardBLACKJOKER);
    }

    @Test
    public void addCard() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        assertEquals(cardPoolTmp.size(), 0);
        cardPoolTmp.addCard(card2SPADE);
        assertEquals(cardPoolTmp.size(), 1);
        cardPoolTmp.addCard(factory.createCard(CardSuit.HEART, CardRank.Card_A));
        assertEquals(cardPoolTmp.size(), 2);

        cardPoolTmp.addCard(card2SPADE);
        assertEquals(cardPoolTmp.size(), 2);
    }

    @Test
    public void addCards() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        CardPool emptyCardPool = new CardPool();

        cardPoolTmp.addCards(emptyCardPool);
        assertTrue(isEqual(cardPoolTmp, emptyCardPool));
        cardPoolTmp.addCards(cardPoolWith3AndJoker);
        assertTrue(isEqual(cardPoolTmp, cardPoolWith3AndJoker));

        cardPoolTmp.addCards(cardPoolWith3AndJoker);
        assertEquals(cardPoolTmp.size(), cardPoolWith3AndJoker.size());
    }

    @Test
    public void getCardByID() throws Exception {
        assertEquals(cardPoolWith3AndJoker.getCardByID(cardBLACKJOKER.getId()), cardBLACKJOKER);
        assertEquals(cardPoolWith3AndJoker.getCardByID(card3CLUB.getId()), card3CLUB);
        assertNull(emptyCardPool.getCardByID(cardBLACKJOKER.getId()));
        assertNull(cardPoolWith3AndJoker.getCardByID(card2CLUB.getId()));
    }

    @Test
    public void getCardBySuitAndRank() throws Exception {
        assertEquals(cardPoolWith3AndJoker.getCardBySuitAndRank(CardSuit.RED, CardRank.Card_Joker), cardREDJOKER);
        assertEquals(cardPoolWith3AndJoker.getCardBySuitAndRank(CardSuit.SPADE, CardRank.Card_3), card3SPADE);
        assertNull(emptyCardPool.getCardBySuitAndRank(CardSuit.CLUB, CardRank.Card_2));
        assertNull(cardPoolWith3AndJoker.getCardBySuitAndRank(CardSuit.CLUB, CardRank.Card_2));
    }

    @Test
    public void getFirstCard() {
        assertEquals(cardPoolWith3AndJoker.getFirstCard(), card3CLUB);
        assertNull(emptyCardPool.getFirstCard());
    }

    @Test
    public void getLastCard() {
        assertEquals(cardPoolWith3AndJoker.getLastCard(), cardBLACKJOKER);
        assertNull(emptyCardPool.getLastCard());
    }

    @Test
    public void removeCard() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        Card card1 = factory.createCard(CardSuit.HEART, CardRank.Card_5);
        Card card2 = factory.createCard(CardSuit.CLUB, CardRank.Card_6);

        cardPoolTmp.addCard(card1);
        cardPoolTmp.addCard(card2);

        assertEquals(cardPoolTmp.removeCard(card1), card1);
        assertEquals(cardPoolTmp.size(), 1);
        assertNull(cardPoolTmp.removeCard(cardBLACKJOKER));
        assertEquals(cardPoolTmp.removeCard(card2), card2);
        assertEquals(cardPoolTmp.size(), 0);
    }

    @Test
    public void removeCards() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        cardPoolTmp.addCard(card2CLUB);
        cardPoolTmp.addCard(cardREDJOKER);

        assertTrue(cardPoolTmp.removeCards(cardPoolWith3AndJoker));
        assertEquals(cardPoolTmp.getFirstCard(), card2CLUB);

        assertTrue(cardPoolTmp.removeCards(cardPoolWith2AndKAndJoker));
        assertEquals(cardPoolTmp.size(), 0);

    }

    @Test
    public void removeCardByID() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        Card card1 = factory.createCard(CardSuit.HEART, CardRank.Card_9);
        Card card2 = factory.createCard(CardSuit.CLUB, CardRank.Card_10);

        cardPoolTmp.addCard(card1);
        cardPoolTmp.addCard(card2);

        assertEquals(cardPoolTmp.removeCardByID(card1.getId()), card1);
        assertEquals(cardPoolTmp.size(), 1);
        assertNull(cardPoolTmp.removeCardByID(cardBLACKJOKER.getId()));
        assertEquals(cardPoolTmp.removeCardByID(card2.getId()), card2);
        assertEquals(cardPoolTmp.size(), 0);
    }

    @Test
    public void removeFirstCard() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        Card card1 = factory.createCard(CardSuit.HEART, CardRank.Card_J);
        Card card2 = factory.createCard(CardSuit.CLUB, CardRank.Card_Q);

        cardPoolTmp.addCard(card1);
        cardPoolTmp.addCard(card2);

        assertEquals(cardPoolTmp.removeFirstCard(), card1);
        assertEquals(cardPoolTmp.size(), 1);
        assertEquals(cardPoolTmp.removeFirstCard(), card2);
        assertEquals(cardPoolTmp.size(), 0);
        assertNull(cardPoolTmp.removeCard(cardBLACKJOKER));
    }

    @Test
    public void removeLastCard() throws Exception {
        CardPool cardPoolTmp = new CardPool();
        Card card1 = factory.createCard(CardSuit.SPADE, CardRank.Card_J);
        Card card2 = factory.createCard(CardSuit.DIAMOND, CardRank.Card_Q);

        cardPoolTmp.addCard(card1);
        cardPoolTmp.addCard(card2);

        assertEquals(cardPoolTmp.removeLastCard(), card2);
        assertEquals(cardPoolTmp.size(), 1);
        assertEquals(cardPoolTmp.removeLastCard(), card1);
        assertEquals(cardPoolTmp.size(), 0);
        assertNull(cardPoolTmp.removeCard(cardBLACKJOKER));
    }

    @Test
    public void sort() {
        cardPoolWith2AndKAndJoker.sort();
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(0), cardKDIAMOND);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(1), cardKCLUB);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(2), cardKHEART);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(3), cardKSPADE);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(4), card2DIAMOND);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(5), card2CLUB);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(6), card2HEART);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(7), card2SPADE);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(8), cardBLACKJOKER);
        assertEquals(cardPoolWith2AndKAndJoker.getCards().get(9), cardREDJOKER);
    }

    @Test
    public void shuffle() {
        int counter = 0;
        Card previousCard = cardPoolWith2AndKAndJoker.getFirstCard();
        for (int i = 0; i < 10; i++) {
            cardPoolWith2AndKAndJoker.shuffle();
            if (cardPoolWith2AndKAndJoker.getFirstCard() == previousCard)
                counter++;
        }
        assertTrue(counter < 10);
    }

    @Test
    public void isSameRanks() throws Exception {
        assertFalse(cardPoolWith2AndKAndJoker.isSameRanks());
        assertFalse(cardPoolWith3AndJoker.isSameRanks());

        CardPool cardPoolTmp1 = new CardPool();
        cardPoolTmp1.addCard(factory.createCard(CardSuit.HEART, CardRank.Card_K));
        cardPoolTmp1.addCard(factory.createCard(CardSuit.DIAMOND, CardRank.Card_K));
        cardPoolTmp1.addCard(factory.createCard(CardSuit.SPADE, CardRank.Card_K));
        assertTrue(cardPoolTmp1.isSameRanks());

        CardPool cardPoolTmp2 = new CardPool();
        cardPoolTmp2.addCard(cardREDJOKER);
        cardPoolTmp2.addCard(cardBLACKJOKER);
        assertTrue(cardPoolTmp2.isSameRanks());

    }

    @Test
    public void isSameSuits() throws Exception {
        assertFalse(cardPoolWith2AndKAndJoker.isSameSuits());
        assertFalse(cardPoolWith3AndJoker.isSameSuits());

        CardPool cardPoolTmp1 = new CardPool();
        cardPoolTmp1.addCard(factory.createCard(CardSuit.DIAMOND, CardRank.Card_5));
        cardPoolTmp1.addCard(factory.createCard(CardSuit.DIAMOND, CardRank.Card_6));
        cardPoolTmp1.addCard(factory.createCard(CardSuit.DIAMOND, CardRank.Card_7));
        assertTrue(cardPoolTmp1.isSameSuits());

        CardPool cardPoolTmp2 = new CardPool();
        cardPoolTmp2.addCard(cardREDJOKER);
        cardPoolTmp2.addCard(cardBLACKJOKER);
        assertFalse(cardPoolTmp2.isSameSuits());
    }

    @Test
    public void isEmpty() {
        assertTrue(emptyCardPool.isEmpty());
    }

    protected boolean isEqual(CardPool pool1, CardPool pool2) {
        ArrayList<Card> list1 = pool1.getCards();
        ArrayList<Card> list2 = pool2.getCards();
        if (pool1.size() == pool2.size() && list1.size() == list2.size() && pool1.size() == list1.size()) {
            for (int i = 0; i < pool1.size(); i++) {
                if (!list1.contains(list2.get(i)))
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }
}