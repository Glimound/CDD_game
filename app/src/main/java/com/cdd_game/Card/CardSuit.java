package com.cdd_game.Card;

/**
 * DIAMOND：方片；
 * CLUB：梅花；
 * HEART：红桃；
 * SPADE：黑桃。
 * 从小到大排序。
 * BLACK：小王；
 * RED：大王，仅供大小王使用。
 */
public enum CardSuit {
    DIAMOND(1,"DIAMOND"),
    CLUB(2,"CLUB"),
    HEART(3,"HEART"),
    SPADE(4,"SPADE"),
    BLACK(5, "BLACK"),
    RED(6, "RED");

    int weight;
    String name;

    CardSuit(int Weight,String Name){
        weight=Weight;
        name=Name;
    }

    public int SuitCompareTo(CardSuit Card){
        return weight- Card.weight;
    }

    public int getWeight(){
        return weight;
    }

    public String getName(){
        return name;
    }
}
