package com.cdd_game.Card;

public enum CardSuit {
    DIAMOND(1,"方块"),
    CLUB(2,"梅花"),
    HEART(3,"红桃"),
    SPADE(4,"黑桃");

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
