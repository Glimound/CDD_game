package com.cdd_game.Card;

public class CardPoolFactory {

    /**
     * 创建CardPool及CardGroup类型实例的工厂方法。
     * @param cardPoolType 创建的卡集合体的类型，接收一个描述产品类型的String。“cardPool”为CardPool类型
     *                     实例，“cardGroup”为CardGroup类型实例。对大小写不敏感。<br>
     * @param configuration 配置项，接收一个描述产品配置的String。<br>对于cardPool，“intact”为创建含有
     *                      54张扑克牌的完整牌池，“withoutJokers”为创建不含大小王的牌池（52张）。
     *                      <br>对于cardGroup，configuration为卡组类型，未指明时应当使用“未识别”。
     *                      对大小写不敏感。<br>
     * @return CardPool类型或CardGroup类型的实例
     */
    public CardPool create(String cardPoolType, String configuration) throws Exception {
        if (cardPoolType.equalsIgnoreCase("cardPool")) {
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
                    cardFactory.createCard(CardSuit.DIAMOND, CardRank.getEnumByWeight(i));
                    cardFactory.createCard(CardSuit.CLUB, CardRank.getEnumByWeight(i));
                    cardFactory.createCard(CardSuit.HEART, CardRank.getEnumByWeight(i));
                    cardFactory.createCard(CardSuit.SPADE, CardRank.getEnumByWeight(i));
                }
                return cardPool;
            }
        } else if (cardPoolType.equalsIgnoreCase("cardGroup")) {
            //TODO: 校验卡组类型（通过枚举类实现）
            return new CardGroup(configuration);
        }
        return null;
    }
}
