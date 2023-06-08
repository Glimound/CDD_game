package com.cdd_game.Card;

public enum CardGroupType {
    UNKNOW(0),              //未识别
    SINGLE(1),              //单牌
    PAIR(2),                //对子
    TRIPLET(3),             //三牌
    D_SEQUENCE(5),          //杂顺
    SAMESUITS(6),           //同花五
    TRIPLETWITHPAIR(7),     //三个带一对
    QUADRUPLELWITHSINGLE(8),          //四带一
    S_SEQUENCE(9);          //同花顺
    int weight;

    CardGroupType(int weight){
        this.weight=weight;
    }

    public int getWeight() {
        return weight;
    }
}
