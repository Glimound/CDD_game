package com.cdd_game.Card;

/**
 * 创建CardPool及CardGroup类型实例的工厂类。
 */
public class CardPoolFactory {

    /**
     * 创建CardPool类型实例的工厂方法。
     * @param configuration 配置项，接收一个描述产品配置的String。“intact”为创建含有54张扑克牌的完整牌池，
     *                      “withoutJokers”为创建不含大小王的牌池（52张），"empty"为创建空牌池。
     *                      对大小写不敏感。
     * @return CardPool类型或CardGroup类型的实例
     */
    public CardPool createCardPool(String configuration) throws Exception {
        CardPool cardPool = new CardPool();
        CardFactory cardFactory = new CardFactory();
        if (configuration.equalsIgnoreCase("intact")) {
            CardFactory.refreshCardID();
            for (int i = CardRank.getMinWeight(); i <= CardRank.getMaxWeight() - 1; i++) {
                cardPool.addCard(cardFactory.createCard(CardSuit.DIAMOND, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.CLUB, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.HEART, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.SPADE, CardRank.getEnumByWeight(i)));
            }
            cardPool.addCard(cardFactory.createCard(CardSuit.BLACK, CardRank.Card_Joker));
            cardPool.addCard(cardFactory.createCard(CardSuit.RED, CardRank.Card_Joker));
            return cardPool;
        } else if (configuration.equalsIgnoreCase("withoutJokers")) {
            CardFactory.refreshCardID();
            for (int i = CardRank.getMinWeight(); i <= CardRank.getMaxWeight() - 1; i++) {
                cardPool.addCard(cardFactory.createCard(CardSuit.DIAMOND, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.CLUB, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.HEART, CardRank.getEnumByWeight(i)));
                cardPool.addCard(cardFactory.createCard(CardSuit.SPADE, CardRank.getEnumByWeight(i)));
            }
            return cardPool;
        } else if (configuration.equalsIgnoreCase("empty")) {
            return new CardPool();
        } else {
            throw new Exception("Invalid configuration.");
        }
    }

    /**
     * 创建CardGroup类型实例的工厂方法。创建空cardGroup对象。
     * @param cardGroupType 卡组的类型，未指明时应当使用CardGroupType.UNKNOW（未识别）
     * @return
     */
    public CardGroup createCardGroup(CardGroupType cardGroupType) throws Exception {
        return new CardGroup(cardGroupType);
    }
}
