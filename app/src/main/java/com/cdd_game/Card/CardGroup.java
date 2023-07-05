package com.cdd_game.Card;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;


public class CardGroup extends CardPool{

    /**共以下几个状态
     * 未识别，单牌，对子，三牌，四带一，三带一对，杂顺，同花顺，同花五
     */
    private CardGroupType cardGroupType;

    private Card maxCard;

    CardGroup(){
        super();
        this.cardGroupType = CardGroupType.UNKNOWN;
    }

    CardGroup(CardGroupType cardGroupType){
        super();
        this.cardGroupType=cardGroupType;
    }

    public CardGroupType getCardGroupType(){
        return cardGroupType;
    }

    public void setCardGroupType(CardGroupType cardGroupType){
        this.cardGroupType=cardGroupType;
    }

    public Card getMaxCard(){
        return maxCard;
    }

    public void setMaxCard(Card maxCard) {
        this.maxCard = maxCard;
    }

    @Override
    public ArrayList<Card> getCards() {
        return cards;
    }


    public ArrayList<Card> getCardsByRank(CardRank rank){
        ArrayList<Card> retCards=new ArrayList<>();
        if (cards.isEmpty())
            return null;
        for(Card card:cards){
            if(card.getRank()==rank)
                retCards.add(card);
        }
        return retCards;
    }

    public boolean containCardsRank(CardRank rank){
        if (cards.isEmpty())
            return false;
        for(Card card:cards){
            if(card.getRank()==rank)
                return true;
        }
        return false;
    }

    public boolean compare(CardGroup cardGroup){

        if(cardGroup.size()!=this.size()){
            return false;
        }
        if(this.cardGroupType.getWeight()>4&&cardGroup.cardGroupType.getWeight()>4){
            if(this.cardGroupType.getWeight()==cardGroup.cardGroupType.getWeight()){
                if(this.maxCard.compareTo(cardGroup.getMaxCard())==1){
                    return true;
                }else{
                    return false;
                }
            }else{
                if(this.cardGroupType.getWeight()>cardGroup.cardGroupType.getWeight())
                    return true;
                else
                    return false;
            }
        }
        else{
            if(this.maxCard.compareTo(cardGroup.getMaxCard())==1){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * 按顺子中的排序规则进行排序，仅适用于无大小王的情况。eg: A,2,3,...,J,Q,K
     */
    public void sequenceSort() {
        Collections.sort(cards);
        for (Card card; getLastCard().getRank() == CardRank.Card_2; ) {
            cards.add(0, removeLastCard());
            cardCounter++;
        }
        for (Card card; getLastCard().getRank() == CardRank.Card_A; ) {
            cards.add(0, removeLastCard());
            cardCounter++;
        }
    }

    /**
     * 按顺子中的排序规则（A作为顺尾）进行排序，仅适用于无大小王的情况。eg: 2,3,...,J,Q,K,A
     */
    public void sequenceAMaxSort() {
        Collections.sort(cards);
        for (Card card; getLastCard().getRank() == CardRank.Card_2; ) {
            cards.add(0, removeLastCard());
            cardCounter++;
        }
    }

    @NonNull
    public String toString() {
        if (cards.isEmpty()) {
            return "empty";
        } else {
            StringBuilder str = new StringBuilder();
            for (Card card : cards) {
                str.append(card.toString());
            }
            return str.toString();
        }
    }

}
