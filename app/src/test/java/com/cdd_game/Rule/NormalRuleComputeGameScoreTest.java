package com.cdd_game.Rule;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardFactory;
import com.cdd_game.Card.CardGroup;
import com.cdd_game.Card.CardGroupType;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Card.CardRank;
import com.cdd_game.Card.CardSuit;
import com.cdd_game.Player.Player;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;

public class NormalRuleComputeGameScoreTest {

    static CardFactory cardFactory;
    static CardPoolFactory cardPoolFactory;
    static CardPool cards;
    static Rule rule;

    static HashMap<Player, CardPool> remainingCards1;
    static HashMap<Player, CardPool> remainingCards2;
    static HashMap<Player, CardPool> remainingCards3;
    static HashMap<Player, CardPool> remainingCards4;

    static Player player1;
    static Player player2;
    static Player player3;
    static Player player4;

    @BeforeClass
    public static void initialize() throws Exception {
        cardFactory = new CardFactory();
        cardPoolFactory = new CardPoolFactory();
        cards = cardPoolFactory.createCardPool("withoutJokers");
        rule = new NormalRule();

        remainingCards1 = new HashMap<>();
        remainingCards2 = new HashMap<>();
        remainingCards3 = new HashMap<>();
        remainingCards4 = new HashMap<>();

        player1 = new Player("1", "a");
        player2 = new Player("2", "b");
        player3 = new Player("3", "c");
        player4 = new Player("4", "d");
    }

    @Test
    public void remainingCards1() throws Exception {
        CardPool cards1 = cardPoolFactory.createCardPool("empty");
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_A));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_3));

        CardPool cards2 = cardPoolFactory.createCardPool("empty");
        cards2.addCard(get(CardSuit.HEART, CardRank.Card_J));

        CardPool cards3 = cardPoolFactory.createCardPool("empty");

        CardPool cards4 = cardPoolFactory.createCardPool("empty");
        cards4.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));

        remainingCards1.put(player1, cards1);
        remainingCards1.put(player2, cards2);
        remainingCards1.put(player3, cards3);
        remainingCards1.put(player4, cards4);

        HashMap<Player, Integer> result = rule.computeGameScore(remainingCards1);

        assertEquals(cards1.size(), 2);
        assertEquals(cards2.size(), 1);
        assertEquals(cards3.size(), 0);
        assertEquals(cards4.size(), 1);

        assertEquals(result.size(), remainingCards1.size());
        assertEquals((int) result.get(player1), -4);
        assertEquals((int) result.get(player2), 0);
        assertEquals((int) result.get(player3), 4);
        assertEquals((int) result.get(player4), 0);
    }

    @Test
    public void remainingCards2() throws Exception {
        CardPool cards1 = cardPoolFactory.createCardPool("empty");
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_4));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_5));
        cards1.addCard(get(CardSuit.DIAMOND, CardRank.Card_6));
        cards1.addCard(get(CardSuit.DIAMOND, CardRank.Card_7));
        cards1.addCard(get(CardSuit.DIAMOND, CardRank.Card_8));

        CardPool cards2 = cardPoolFactory.createCardPool("empty");
        cards2.addCard(get(CardSuit.HEART, CardRank.Card_10));
        cards2.addCard(get(CardSuit.HEART, CardRank.Card_J));
        cards2.addCard(get(CardSuit.HEART, CardRank.Card_Q));
        cards2.addCard(get(CardSuit.HEART, CardRank.Card_K));
        cards2.addCard(get(CardSuit.CLUB, CardRank.Card_A));
        cards2.addCard(get(CardSuit.CLUB, CardRank.Card_2));
        cards2.addCard(get(CardSuit.CLUB, CardRank.Card_3));
        cards2.addCard(get(CardSuit.CLUB, CardRank.Card_4));

        CardPool cards3 = cardPoolFactory.createCardPool("empty");
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_J));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_Q));

        CardPool cards4 = cardPoolFactory.createCardPool("empty");

        remainingCards2.put(player1, cards1);
        remainingCards2.put(player2, cards2);
        remainingCards2.put(player3, cards3);
        remainingCards2.put(player4, cards4);

        HashMap<Player, Integer> result = rule.computeGameScore(remainingCards2);

        assertEquals(cards1.size(), 7);
        assertEquals(cards2.size(), 8);
        assertEquals(cards3.size(), 2);
        assertEquals(cards4.size(), 0);

        assertEquals(result.size(), remainingCards2.size());
        assertEquals((int) result.get(player1), -3);
        assertEquals((int) result.get(player2), -39);
        assertEquals((int) result.get(player3), 17);
        assertEquals((int) result.get(player4), 25);
    }

    @Test
    public void remainingCards3() throws Exception {
        CardPool cards1 = cardPoolFactory.createCardPool("empty");

        CardPool cards2 = cardPoolFactory.createCardPool("empty");
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_3));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_4));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_5));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_6));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_7));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_8));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_9));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_10));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_J));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_Q));
        cards2.addCard(get(CardSuit.DIAMOND, CardRank.Card_K));

        CardPool cards3 = cardPoolFactory.createCardPool("empty");
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_4));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_5));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_6));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_7));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_8));
        cards3.addCard(get(CardSuit.SPADE, CardRank.Card_9));

        CardPool cards4 = cardPoolFactory.createCardPool("empty");
        cards4.addCard(get(CardSuit.HEART, CardRank.Card_A));

        remainingCards3.put(player1, cards1);
        remainingCards3.put(player2, cards2);
        remainingCards3.put(player3, cards3);
        remainingCards3.put(player4, cards4);

        HashMap<Player, Integer> result = rule.computeGameScore(remainingCards3);

        assertEquals(cards1.size(), 0);
        assertEquals(cards2.size(), 13);
        assertEquals(cards3.size(), 8);
        assertEquals(cards4.size(), 1);

        assertEquals(result.size(), remainingCards3.size());
        assertEquals((int) result.get(player1), 85);
        assertEquals((int) result.get(player2), -123);
        assertEquals((int) result.get(player3), -43);
        assertEquals((int) result.get(player4), 81);
    }

    @Test
    public void remainingCards4() throws Exception {
        CardPool cards1 = cardPoolFactory.createCardPool("empty");
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_4));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_5));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_6));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_7));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_8));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_9));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_10));
        cards1.addCard(get(CardSuit.SPADE, CardRank.Card_J));

        CardPool cards2 = cardPoolFactory.createCardPool("empty");

        CardPool cards3 = cardPoolFactory.createCardPool("empty");
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_2));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_3));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_4));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_5));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_6));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_7));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_8));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_9));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_10));
        cards3.addCard(get(CardSuit.HEART, CardRank.Card_J));

        CardPool cards4 = cardPoolFactory.createCardPool("empty");
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_2));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_3));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_4));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_5));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_6));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_7));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_8));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_9));
        cards4.addCard(get(CardSuit.CLUB, CardRank.Card_10));

        remainingCards4.put(player1, cards1);
        remainingCards4.put(player2, cards2);
        remainingCards4.put(player3, cards3);
        remainingCards4.put(player4, cards4);

        HashMap<Player, Integer> result = rule.computeGameScore(remainingCards4);

        assertEquals(cards1.size(), 10);
        assertEquals(cards2.size(), 0);
        assertEquals(cards3.size(), 10);
        assertEquals(cards4.size(), 9);

        assertEquals(result.size(), remainingCards4.size());
        assertEquals((int) result.get(player1), -132);
        assertEquals((int) result.get(player2), 108);
        assertEquals((int) result.get(player3), -12);
        assertEquals((int) result.get(player4), 36);
    }

    /**
     * 返回指定花色和点数的牌
     */
    private Card get(CardSuit suit, CardRank rank) {
        return cards.getCardBySuitAndRank(suit, rank);
    }
}
