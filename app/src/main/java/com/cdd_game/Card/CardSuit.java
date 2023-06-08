package com.cdd_game.Card;

// TODO: 实现iterable接口

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
    /**
     * 方片
     */
    DIAMOND(1,"DIAMOND"),

    /**
     * 梅花
     */
    CLUB(2,"CLUB"),

    /**
     * 红桃
     */
    HEART(3,"HEART"),

    /**
     * 黑桃
     */
    SPADE(4,"SPADE"),

    /**
     * 黑（小王）
     */
    BLACK(5, "BLACK"),

    /**
     * 红(大王）
     */
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
