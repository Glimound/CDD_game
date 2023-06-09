package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class CardGroupTest {

    static CardGroup cardGroupWithKAndAAnd2 = new CardGroup();
    static Card card2CLUB, card2HEART, card2DIAMOND, card2SPADE,
            cardKCLUB, cardKHEART, cardKDIAMOND, cardKSPADE,
            cardACLUB, cardAHEART, cardADIAMOND, cardASPADE;

    static CardFactory factory = new CardFactory();

    @BeforeClass
    public static void initialize() throws Exception {
        cardKCLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_K);
        cardKHEART = factory.createCard(CardSuit.HEART, CardRank.Card_K);
        cardKDIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_K);
        cardKSPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_K);
        cardACLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_A);
        cardAHEART = factory.createCard(CardSuit.HEART, CardRank.Card_A);
        cardADIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_A);
        cardASPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_A);
        card2CLUB = factory.createCard(CardSuit.CLUB, CardRank.Card_2);
        card2HEART = factory.createCard(CardSuit.HEART, CardRank.Card_2);
        card2DIAMOND = factory.createCard(CardSuit.DIAMOND, CardRank.Card_2);
        card2SPADE = factory.createCard(CardSuit.SPADE, CardRank.Card_2);

        cardGroupWithKAndAAnd2.addCard(cardKCLUB);
        cardGroupWithKAndAAnd2.addCard(cardKHEART);
        cardGroupWithKAndAAnd2.addCard(cardKDIAMOND);
        cardGroupWithKAndAAnd2.addCard(cardKSPADE);
        cardGroupWithKAndAAnd2.addCard(cardACLUB);
        cardGroupWithKAndAAnd2.addCard(cardAHEART);
        cardGroupWithKAndAAnd2.addCard(cardADIAMOND);
        cardGroupWithKAndAAnd2.addCard(cardASPADE);
        cardGroupWithKAndAAnd2.addCard(card2CLUB);
        cardGroupWithKAndAAnd2.addCard(card2HEART);
        cardGroupWithKAndAAnd2.addCard(card2DIAMOND);
        cardGroupWithKAndAAnd2.addCard(card2SPADE);
    }

    @Test
    public void getCardsByRank() {
    }

    @Test
    public void compare() {
    }

    @Test
    public void sequenceSort() {
        cardGroupWithKAndAAnd2.sequenceSort();
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(0), cardADIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(1), cardACLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(2), cardAHEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(3), cardASPADE);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(4), card2DIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(5), card2CLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(6), card2HEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(7), card2SPADE);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(8), cardKDIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(9), cardKCLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(10), cardKHEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(11), cardKSPADE);
    }

    @Test
    public void sequenceAMaxSort() {
        cardGroupWithKAndAAnd2.sequenceAMaxSort();
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(0), card2DIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(1), card2CLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(2), card2HEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(3), card2SPADE);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(4), cardKDIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(5), cardKCLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(6), cardKHEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(7), cardKSPADE);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(8), cardADIAMOND);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(9), cardACLUB);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(10), cardAHEART);
        assertEquals(cardGroupWithKAndAAnd2.getCards().get(11), cardASPADE);

    }
}