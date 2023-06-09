package com.cdd_game.Card;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardPoolFactoryTest {

    @Test
    public void createCardPool() throws Exception {
        CardPoolFactory factory = new CardPoolFactory();
        CardPool cards1 = factory.createCardPool("iNtaCt");
        assertEquals(cards1.size(), 54);
        assertEquals(cards1.getCards().size(), 54);
        for (int i = CardRank.getMinWeight(); i <= CardRank.getMaxWeight() - 1; i++) {
            assertNotNull(cards1.getCardBySuitAndRank(CardSuit.DIAMOND, CardRank.getEnumByWeight(i)));
            assertNotNull(cards1.getCardBySuitAndRank(CardSuit.CLUB, CardRank.getEnumByWeight(i)));
            assertNotNull(cards1.getCardBySuitAndRank(CardSuit.HEART, CardRank.getEnumByWeight(i)));
            assertNotNull(cards1.getCardBySuitAndRank(CardSuit.SPADE, CardRank.getEnumByWeight(i)));
        }
        assertNotNull(cards1.getCardBySuitAndRank(CardSuit.RED, CardRank.Card_Joker));
        assertNotNull(cards1.getCardBySuitAndRank(CardSuit.BLACK, CardRank.Card_Joker));


        CardPool cards2 = factory.createCardPool("withoutJOKERs");
        assertEquals(cards2.size(), 52);
        assertEquals(cards2.getCards().size(), 52);
        for (int i = CardRank.getMinWeight(); i <= CardRank.getMaxWeight() - 1; i++) {
            assertNotNull(cards2.getCardBySuitAndRank(CardSuit.DIAMOND, CardRank.getEnumByWeight(i)));
            assertNotNull(cards2.getCardBySuitAndRank(CardSuit.CLUB, CardRank.getEnumByWeight(i)));
            assertNotNull(cards2.getCardBySuitAndRank(CardSuit.HEART, CardRank.getEnumByWeight(i)));
            assertNotNull(cards2.getCardBySuitAndRank(CardSuit.SPADE, CardRank.getEnumByWeight(i)));
        }
        assertNull(cards2.getCardBySuitAndRank(CardSuit.RED, CardRank.Card_Joker));
        assertNull(cards2.getCardBySuitAndRank(CardSuit.BLACK, CardRank.Card_Joker));


        CardPool cards3 = factory.createCardPool("empty");
        assertTrue(cards3.isEmpty());
        assertTrue(cards3.getCards().isEmpty());

        assertThrows(Exception.class,
                () -> factory.createCardPool("dfwfe12"));
    }

    @Test
    public void createCardGroup() throws Exception {
        CardPoolFactory factory = new CardPoolFactory();
        CardGroup cards1 = factory.createCardGroup(CardGroupType.UNKNOWN);
        assertEquals(cards1.size(), 0);
        assertEquals(cards1.getCardGroupType(), CardGroupType.UNKNOWN);

        CardGroup cards2 = factory.createCardGroup(CardGroupType.QUADRUPLEWITHSINGLE);
        assertEquals(cards2.size(), 0);
        assertEquals(cards2.getCardGroupType(), CardGroupType.QUADRUPLEWITHSINGLE);
    }
}