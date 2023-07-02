package com.cdd_game.Rule;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardFactory;
import com.cdd_game.Card.CardGroup;
import com.cdd_game.Card.CardGroupType;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Card.CardRank;
import com.cdd_game.Card.CardSuit;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalRuleValidateTest {

    static CardFactory cardFactory;
    static CardPoolFactory cardPoolFactory;
    static CardPool cards;
    static Rule rule;

    @BeforeClass
    public static void initialize() throws Exception {
        cardFactory = new CardFactory();
        cardPoolFactory = new CardPoolFactory();
        cards = cardPoolFactory.createCardPool("withoutJokers");
        rule = new NormalRule();
    }

    @Test
    public void single() throws Exception {
        CardGroup dA = create();
        dA.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));

        CardGroup c2 = create();
        c2.addCard(get(CardSuit.CLUB, CardRank.Card_2));

        CardGroup h10 = create();
        h10.addCard(get(CardSuit.HEART, CardRank.Card_10));

        CardGroup s6 = create();
        s6.addCard(get(CardSuit.SPADE, CardRank.Card_6));

        assertTrue(rule.validate(dA));
        assertTrue(rule.validate(c2));
        assertTrue(rule.validate(h10));
        assertTrue(rule.validate(s6));

        assertEquals(dA.getMaxCard(), get(CardSuit.DIAMOND, CardRank.Card_A));
        assertEquals(c2.getMaxCard(), get(CardSuit.CLUB, CardRank.Card_2));
        assertEquals(h10.getMaxCard(), get(CardSuit.HEART, CardRank.Card_10));
        assertEquals(s6.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_6));

        assertEquals(dA.getCardGroupType(), CardGroupType.SINGLE);
        assertEquals(c2.getCardGroupType(), CardGroupType.SINGLE);
        assertEquals(h10.getCardGroupType(), CardGroupType.SINGLE);
        assertEquals(s6.getCardGroupType(), CardGroupType.SINGLE);
    }

    @Test
    public void pair() throws Exception {

        CardGroup c2d2 = create();
        c2d2.addCard(get(CardSuit.CLUB, CardRank.Card_2));
        c2d2.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));

        CardGroup h8h7 = create();
        h8h7.addCard(get(CardSuit.HEART, CardRank.Card_8));
        h8h7.addCard(get(CardSuit.HEART, CardRank.Card_7));

        CardGroup sKc5 = create();
        sKc5.addCard(get(CardSuit.SPADE, CardRank.Card_K));
        sKc5.addCard(get(CardSuit.CLUB, CardRank.Card_5));

        assertTrue(rule.validate(c2d2));
        assertFalse(rule.validate(h8h7));
        assertFalse(rule.validate(sKc5));

        assertEquals(c2d2.getMaxCard(), get(CardSuit.CLUB, CardRank.Card_2));
        assertEquals(c2d2.getCardGroupType(), CardGroupType.PAIR);

    }


    @Test
    public void triplet() throws Exception {

        CardGroup c2d2s2 = create();
        c2d2s2.addCard(get(CardSuit.CLUB, CardRank.Card_2));
        c2d2s2.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        c2d2s2.addCard(get(CardSuit.SPADE, CardRank.Card_2));

        CardGroup h8h7hQ = create();
        h8h7hQ.addCard(get(CardSuit.HEART, CardRank.Card_8));
        h8h7hQ.addCard(get(CardSuit.HEART, CardRank.Card_7));
        h8h7hQ.addCard(get(CardSuit.HEART, CardRank.Card_Q));

        CardGroup sKc5d8 = create();
        sKc5d8.addCard(get(CardSuit.SPADE, CardRank.Card_K));
        sKc5d8.addCard(get(CardSuit.CLUB, CardRank.Card_5));
        sKc5d8.addCard(get(CardSuit.DIAMOND, CardRank.Card_8));

        assertTrue(rule.validate(c2d2s2));
        assertFalse(rule.validate(h8h7hQ));
        assertFalse(rule.validate(sKc5d8));

        assertEquals(c2d2s2.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_2));
        assertEquals(c2d2s2.getCardGroupType(), CardGroupType.TRIPLET);
    }

    @Test
    public void d_sequence() throws Exception {

        CardGroup s2s3h4h5c6 = create();
        s2s3h4h5c6.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        s2s3h4h5c6.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        s2s3h4h5c6.addCard(get(CardSuit.HEART, CardRank.Card_4));
        s2s3h4h5c6.addCard(get(CardSuit.HEART, CardRank.Card_5));
        s2s3h4h5c6.addCard(get(CardSuit.CLUB, CardRank.Card_6));

        CardGroup c10cJcQcKdA = create();
        c10cJcQcKdA.addCard(get(CardSuit.CLUB, CardRank.Card_10));
        c10cJcQcKdA.addCard(get(CardSuit.CLUB, CardRank.Card_J));
        c10cJcQcKdA.addCard(get(CardSuit.CLUB, CardRank.Card_Q));
        c10cJcQcKdA.addCard(get(CardSuit.CLUB, CardRank.Card_K));
        c10cJcQcKdA.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));

        CardGroup dAd2s3h4d5 = create();
        dAd2s3h4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        dAd2s3h4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        dAd2s3h4d5.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        dAd2s3h4d5.addCard(get(CardSuit.HEART, CardRank.Card_4));
        dAd2s3h4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_5));

        CardGroup dAd2h4d5s6 = create();
        dAd2h4d5s6.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        dAd2h4d5s6.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        dAd2h4d5s6.addCard(get(CardSuit.HEART, CardRank.Card_4));
        dAd2h4d5s6.addCard(get(CardSuit.DIAMOND, CardRank.Card_5));
        dAd2h4d5s6.addCard(get(CardSuit.SPADE, CardRank.Card_6));

        CardGroup dQdKsAs2s3 = create();
        dQdKsAs2s3.addCard(get(CardSuit.DIAMOND, CardRank.Card_Q));
        dQdKsAs2s3.addCard(get(CardSuit.DIAMOND, CardRank.Card_K));
        dQdKsAs2s3.addCard(get(CardSuit.SPADE, CardRank.Card_A));
        dQdKsAs2s3.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        dQdKsAs2s3.addCard(get(CardSuit.SPADE, CardRank.Card_3));

        assertTrue(rule.validate(s2s3h4h5c6));
        assertTrue(rule.validate(c10cJcQcKdA));
        assertTrue(rule.validate(dAd2s3h4d5));
        assertFalse(rule.validate(dAd2h4d5s6));
        assertFalse(rule.validate(dQdKsAs2s3));

        assertEquals(s2s3h4h5c6.getMaxCard(), get(CardSuit.CLUB, CardRank.Card_6));
        assertEquals(c10cJcQcKdA.getMaxCard(), get(CardSuit.DIAMOND, CardRank.Card_A));
        assertEquals(dAd2s3h4d5.getMaxCard(), get(CardSuit.DIAMOND, CardRank.Card_5));

        assertEquals(s2s3h4h5c6.getCardGroupType(), CardGroupType.D_SEQUENCE);
        assertEquals(c10cJcQcKdA.getCardGroupType(), CardGroupType.D_SEQUENCE);
        assertEquals(dAd2s3h4d5.getCardGroupType(), CardGroupType.D_SEQUENCE);

    }

    @Test
    public void sameSuits() throws Exception {

        CardGroup hAh6h7h9hJ = create();
        hAh6h7h9hJ.addCard(get(CardSuit.HEART, CardRank.Card_A));
        hAh6h7h9hJ.addCard(get(CardSuit.HEART, CardRank.Card_6));
        hAh6h7h9hJ.addCard(get(CardSuit.HEART, CardRank.Card_7));
        hAh6h7h9hJ.addCard(get(CardSuit.HEART, CardRank.Card_9));
        hAh6h7h9hJ.addCard(get(CardSuit.HEART, CardRank.Card_J));

        CardGroup dQdKdAd2d3 = create();
        dQdKdAd2d3.addCard(get(CardSuit.DIAMOND, CardRank.Card_Q));
        dQdKdAd2d3.addCard(get(CardSuit.DIAMOND, CardRank.Card_K));
        dQdKdAd2d3.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        dQdKdAd2d3.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        dQdKdAd2d3.addCard(get(CardSuit.DIAMOND, CardRank.Card_3));

        CardGroup sKsAs2s3s4 = create();
        sKsAs2s3s4.addCard(get(CardSuit.SPADE, CardRank.Card_K));
        sKsAs2s3s4.addCard(get(CardSuit.SPADE, CardRank.Card_A));
        sKsAs2s3s4.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        sKsAs2s3s4.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        sKsAs2s3s4.addCard(get(CardSuit.SPADE, CardRank.Card_4));

        CardGroup cJcQcKcAc2 = create();
        cJcQcKcAc2.addCard(get(CardSuit.CLUB, CardRank.Card_J));
        cJcQcKcAc2.addCard(get(CardSuit.CLUB, CardRank.Card_Q));
        cJcQcKcAc2.addCard(get(CardSuit.CLUB, CardRank.Card_K));
        cJcQcKcAc2.addCard(get(CardSuit.CLUB, CardRank.Card_A));
        cJcQcKcAc2.addCard(get(CardSuit.CLUB, CardRank.Card_2));

        CardGroup d3d9sJsQsK = create();
        d3d9sJsQsK.addCard(get(CardSuit.DIAMOND, CardRank.Card_3));
        d3d9sJsQsK.addCard(get(CardSuit.DIAMOND, CardRank.Card_9));
        d3d9sJsQsK.addCard(get(CardSuit.SPADE, CardRank.Card_J));
        d3d9sJsQsK.addCard(get(CardSuit.SPADE, CardRank.Card_Q));
        d3d9sJsQsK.addCard(get(CardSuit.SPADE, CardRank.Card_K));

        assertTrue(rule.validate(hAh6h7h9hJ));
        assertTrue(rule.validate(dQdKdAd2d3));
        assertTrue(rule.validate(sKsAs2s3s4));
        assertTrue(rule.validate(cJcQcKcAc2));
        assertFalse(rule.validate(d3d9sJsQsK));

        assertEquals(hAh6h7h9hJ.getMaxCard(), get(CardSuit.HEART, CardRank.Card_A));
        assertEquals(dQdKdAd2d3.getMaxCard(), get(CardSuit.DIAMOND, CardRank.Card_2));
        assertEquals(sKsAs2s3s4.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_2));
        assertEquals(cJcQcKcAc2.getMaxCard(), get(CardSuit.CLUB, CardRank.Card_2));

        assertEquals(hAh6h7h9hJ.getCardGroupType(), CardGroupType.SAMESUITS);
        assertEquals(dQdKdAd2d3.getCardGroupType(), CardGroupType.SAMESUITS);
        assertEquals(sKsAs2s3s4.getCardGroupType(), CardGroupType.SAMESUITS);
        assertEquals(cJcQcKcAc2.getCardGroupType(), CardGroupType.SAMESUITS);
    }

    @Test
    public void tripletWithPair() throws Exception {

        CardGroup hAsAcAd2h2 = create();
        hAsAcAd2h2.addCard(get(CardSuit.HEART, CardRank.Card_A));
        hAsAcAd2h2.addCard(get(CardSuit.SPADE, CardRank.Card_A));
        hAsAcAd2h2.addCard(get(CardSuit.CLUB, CardRank.Card_A));
        hAsAcAd2h2.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        hAsAcAd2h2.addCard(get(CardSuit.HEART, CardRank.Card_2));

        CardGroup h3s3s2h2c2 = create();
        h3s3s2h2c2.addCard(get(CardSuit.HEART, CardRank.Card_3));
        h3s3s2h2c2.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        h3s3s2h2c2.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        h3s3s2h2c2.addCard(get(CardSuit.HEART, CardRank.Card_2));
        h3s3s2h2c2.addCard(get(CardSuit.CLUB, CardRank.Card_2));

        CardGroup h8h5s8d5c8 = create();
        h8h5s8d5c8.addCard(get(CardSuit.HEART, CardRank.Card_8));
        h8h5s8d5c8.addCard(get(CardSuit.HEART, CardRank.Card_5));
        h8h5s8d5c8.addCard(get(CardSuit.SPADE, CardRank.Card_8));
        h8h5s8d5c8.addCard(get(CardSuit.DIAMOND, CardRank.Card_5));
        h8h5s8d5c8.addCard(get(CardSuit.CLUB, CardRank.Card_8));

        assertTrue(rule.validate(hAsAcAd2h2));
        assertTrue(rule.validate(h3s3s2h2c2));
        assertTrue(rule.validate(h8h5s8d5c8));

        assertEquals(hAsAcAd2h2.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_A));
        assertEquals(h3s3s2h2c2.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_2));
        assertEquals(h8h5s8d5c8.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_8));

        assertEquals(hAsAcAd2h2.getCardGroupType(), CardGroupType.TRIPLETWITHPAIR);
        assertEquals(h3s3s2h2c2.getCardGroupType(), CardGroupType.TRIPLETWITHPAIR);
        assertEquals(h8h5s8d5c8.getCardGroupType(), CardGroupType.TRIPLETWITHPAIR);
    }

    @Test
    public void quadrupleWithSingle() throws Exception {

        CardGroup hAsAcAdAh2 = create();
        hAsAcAdAh2.addCard(get(CardSuit.HEART, CardRank.Card_A));
        hAsAcAdAh2.addCard(get(CardSuit.SPADE, CardRank.Card_A));
        hAsAcAdAh2.addCard(get(CardSuit.CLUB, CardRank.Card_A));
        hAsAcAdAh2.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        hAsAcAdAh2.addCard(get(CardSuit.HEART, CardRank.Card_2));

        CardGroup h3d2s2h2c2 = create();
        h3d2s2h2c2.addCard(get(CardSuit.HEART, CardRank.Card_3));
        h3d2s2h2c2.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        h3d2s2h2c2.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        h3d2s2h2c2.addCard(get(CardSuit.HEART, CardRank.Card_2));
        h3d2s2h2c2.addCard(get(CardSuit.CLUB, CardRank.Card_2));

        CardGroup h8h5s8d8c8 = create();
        h8h5s8d8c8.addCard(get(CardSuit.HEART, CardRank.Card_8));
        h8h5s8d8c8.addCard(get(CardSuit.HEART, CardRank.Card_5));
        h8h5s8d8c8.addCard(get(CardSuit.SPADE, CardRank.Card_8));
        h8h5s8d8c8.addCard(get(CardSuit.DIAMOND, CardRank.Card_8));
        h8h5s8d8c8.addCard(get(CardSuit.CLUB, CardRank.Card_8));

        assertTrue(rule.validate(hAsAcAdAh2));
        assertTrue(rule.validate(h3d2s2h2c2));
        assertTrue(rule.validate(h8h5s8d8c8));

        assertEquals(hAsAcAdAh2.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_A));
        assertEquals(h3d2s2h2c2.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_2));
        assertEquals(h8h5s8d8c8.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_8));

        assertEquals(hAsAcAdAh2.getCardGroupType(), CardGroupType.QUADRUPLEWITHSINGLE);
        assertEquals(h3d2s2h2c2.getCardGroupType(), CardGroupType.QUADRUPLEWITHSINGLE);
        assertEquals(h8h5s8d8c8.getCardGroupType(), CardGroupType.QUADRUPLEWITHSINGLE);
    }

    @Test
    public void s_sequence() throws Exception {
        CardGroup s2s3s4s5s6 = create();
        s2s3s4s5s6.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        s2s3s4s5s6.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        s2s3s4s5s6.addCard(get(CardSuit.SPADE, CardRank.Card_4));
        s2s3s4s5s6.addCard(get(CardSuit.SPADE, CardRank.Card_5));
        s2s3s4s5s6.addCard(get(CardSuit.SPADE, CardRank.Card_6));

        CardGroup c10cJcQcKcA = create();
        c10cJcQcKcA.addCard(get(CardSuit.CLUB, CardRank.Card_10));
        c10cJcQcKcA.addCard(get(CardSuit.CLUB, CardRank.Card_J));
        c10cJcQcKcA.addCard(get(CardSuit.CLUB, CardRank.Card_Q));
        c10cJcQcKcA.addCard(get(CardSuit.CLUB, CardRank.Card_K));
        c10cJcQcKcA.addCard(get(CardSuit.CLUB, CardRank.Card_A));

        CardGroup dAd2d3d4d5 = create();
        dAd2d3d4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        dAd2d3d4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        dAd2d3d4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_3));
        dAd2d3d4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_4));
        dAd2d3d4d5.addCard(get(CardSuit.DIAMOND, CardRank.Card_5));

        assertTrue(rule.validate(s2s3s4s5s6));
        assertTrue(rule.validate(c10cJcQcKcA));
        assertTrue(rule.validate(dAd2d3d4d5));

        assertEquals(s2s3s4s5s6.getMaxCard(), get(CardSuit.SPADE, CardRank.Card_6));
        assertEquals(c10cJcQcKcA.getMaxCard(), get(CardSuit.CLUB, CardRank.Card_A));
        assertEquals(dAd2d3d4d5.getMaxCard(), get(CardSuit.DIAMOND, CardRank.Card_5));

        assertEquals(s2s3s4s5s6.getCardGroupType(), CardGroupType.S_SEQUENCE);
        assertEquals(c10cJcQcKcA.getCardGroupType(), CardGroupType.S_SEQUENCE);
        assertEquals(dAd2d3d4d5.getCardGroupType(), CardGroupType.S_SEQUENCE);
    }

    @Test
    public void others() throws Exception {
        // 4张牌/多于5张牌的情况

        CardGroup s2s3s4s5 = create();
        s2s3s4s5.addCard(get(CardSuit.SPADE, CardRank.Card_2));
        s2s3s4s5.addCard(get(CardSuit.SPADE, CardRank.Card_3));
        s2s3s4s5.addCard(get(CardSuit.SPADE, CardRank.Card_4));
        s2s3s4s5.addCard(get(CardSuit.SPADE, CardRank.Card_5));

        CardGroup c10s10h10d10 = create();
        c10s10h10d10.addCard(get(CardSuit.CLUB, CardRank.Card_10));
        c10s10h10d10.addCard(get(CardSuit.SPADE, CardRank.Card_10));
        c10s10h10d10.addCard(get(CardSuit.HEART, CardRank.Card_10));
        c10s10h10d10.addCard(get(CardSuit.DIAMOND, CardRank.Card_10));

        CardGroup dAd2d3d4d5sJhK = create();
        dAd2d3d4d5sJhK.addCard(get(CardSuit.DIAMOND, CardRank.Card_A));
        dAd2d3d4d5sJhK.addCard(get(CardSuit.DIAMOND, CardRank.Card_2));
        dAd2d3d4d5sJhK.addCard(get(CardSuit.DIAMOND, CardRank.Card_3));
        dAd2d3d4d5sJhK.addCard(get(CardSuit.DIAMOND, CardRank.Card_4));
        dAd2d3d4d5sJhK.addCard(get(CardSuit.SPADE, CardRank.Card_J));
        dAd2d3d4d5sJhK.addCard(get(CardSuit.HEART, CardRank.Card_K));

        assertFalse(rule.validate(s2s3s4s5));
        assertFalse(rule.validate(c10s10h10d10));
        assertFalse(rule.validate(dAd2d3d4d5sJhK));
    }


    /**
     * 创建状态为“未识别”的空卡组
     */
    private CardGroup create() throws Exception {
        return cardPoolFactory.createCardGroup(CardGroupType.UNKNOWN);
    }

    /**
     * 返回指定花色和点数的牌
     */
    private Card get(CardSuit suit, CardRank rank) {
        return cards.getCardBySuitAndRank(suit, rank);
    }



}