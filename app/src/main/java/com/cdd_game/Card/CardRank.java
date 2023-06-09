package com.cdd_game.Card;

// TODO: 实现iterable接口
// TODO: 重构getEnumByWeight()函数，通过哈希表实现

/**
 * 点数：从小到大排序。
 */
public enum CardRank {
    Card_3(1,"3"),
    Card_4(2,"4"),
    Card_5(3,"5"),
    Card_6(4,"6"),
    Card_7(5,"7"),
    Card_8(6,"8"),
    Card_9(7,"9"),
    Card_10(8,"10"),
    Card_J(9,"J"),
    Card_Q(10,"Q"),
    Card_K(11,"K"),
    Card_A(12,"A"),
    Card_2(13,"2"),
    Card_Joker(14,"Joker");


    int weight;
    String name;
    CardRank(int Weight,String Name){
        weight=Weight;
        name=Name;
    }

    public int RankCompareTo(CardRank Card){
        return weight-Card.weight;
    }

    public int getWeight(){
        return weight;
    }

    public String getName(){
        return name;
    }

    public static CardRank getEnumByWeight (int weight){
        for (CardRank rank : CardRank.values()) {
            if (rank.weight == weight)
                return rank;
        }
        return null;
    }

    public static int getMinWeight() {
        return Card_3.weight;
    }

    public static int getMaxWeight() {
        return Card_Joker.weight;
    }
}
