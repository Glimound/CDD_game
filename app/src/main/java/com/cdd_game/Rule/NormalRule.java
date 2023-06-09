package com.cdd_game.Rule;
import com.cdd_game.Card.*;

import java.util.ArrayList;
import java.util.HashMap;

public class NormalRule implements Rule {
    public boolean validate(CardGroup cards){
        int numOfcards=cards.size();

        if(numOfcards>0&&numOfcards<4){
            if(cards.isSameRanks()){
                switch(numOfcards) {
                    case 1:
                        cards.setCardGroupType(CardGroupType.SINGLE);//单牌
                        cards.sort();
                        cards.setMaxCard(cards.getCards().get(0));
                        break;
                    case 2:
                        cards.setCardGroupType(CardGroupType.PAIR);//对子
                        cards.sort();
                        cards.setMaxCard(cards.getCards().get(1));
                        break;
                    case 3:
                        cards.setCardGroupType(CardGroupType.TRIPLET);//三牌
                        cards.sort();
                        cards.setMaxCard(cards.getCards().get(2));
                        break;
                }
                return true;
            }
        } else if (numOfcards==5) {
            HashMap<CardRank,Integer>groupOfcards=new HashMap<>();
            for(int i = 0; i<cards.size(); i++){
                Card temp=cards.getCards().get(i);
                if(groupOfcards.containsKey(temp.getRank())){
                    int num= groupOfcards.get(temp.getRank());
                    num++;
                    groupOfcards.put(temp.getRank(),num);
                }
            }
            if(groupOfcards.size()==2){
                if(groupOfcards.containsValue(3)){
                    cards.setCardGroupType(CardGroupType.TRIPLETWITHPAIR);//三带一对
                    /*
                    CardRank maxRank=null;
                    for(CardRank tempRank:groupOfcards.keySet()){
                        if(groupOfcards.get(tempRank)==3){
                            maxRank=tempRank;
                        }
                    }
                    ArrayList<Card> tempMaxCards=new ArrayList<>();
                    for(int j=0;j<cards.getCardCounter();j++){
                        if(cards.getCards().get(j).getRank().equals(maxRank)){
                            tempMaxCards.add(cards.getCards().get(j));
                        }
                    }
                    Collections.sort(tempMaxCards);
                    cards.setMaxCard(tempMaxCards.get(tempMaxCards.size()-1));
                     */
                    cards.sort();
                    if(cards.getCards().get(2).equals(cards.getCards().get(3))){
                        cards.setMaxCard(cards.getCards().get(4));
                    }else{
                        cards.setMaxCard(cards.getCards().get(2));
                    }
                    return true;


                } else if (groupOfcards.containsValue(4)) {
                    cards.setCardGroupType(CardGroupType.QUADRUPLEWITHSINGLE);//四带一
                    cards.sort();
                    if(cards.getCards().get(0).equals(cards.getCards().get(1))){
                        cards.setMaxCard(cards.getCards().get(3));
                    }else{
                        cards.setMaxCard(cards.getCards().get(4));
                    }
                    return true;
                }
                cards.setCardGroupType(CardGroupType.UNKNOWN);
                return false;

            } else if (groupOfcards.size()==5) {


                if(cards.containCardsRank(CardRank.Card_2)){
                    if(cards.containCardsRank(CardRank.Card_A)){

                        cards.sequenceSort(); //A2345....

                        if(cards.getCards().get(4).getRank().equals(CardRank.Card_5)){
                            if(cards.isSameSuits()){
                                cards.setCardGroupType(CardGroupType.S_SEQUENCE);//同花顺
                            }else{
                                cards.setCardGroupType(CardGroupType.D_SEQUENCE);//杂顺
                            }
                            cards.setMaxCard(cards.getCards().get(4));
                            return true;
                        }else{
                            if(cards.isSameSuits()){
                                cards.setCardGroupType(CardGroupType.SAMESUITS);//同花五
                                cards.setMaxCard(cards.getCards().get(4));
                                return true;
                            }else
                                return false;
                        }
                    }else{
                        cards.sequenceAMaxSort();//2....10JQKA

                        if(cards.getCards().get(4).getRank().equals(CardRank.Card_6)){
                            if(cards.isSameSuits()){
                                cards.setCardGroupType(CardGroupType.S_SEQUENCE);//同花顺
                            }else{
                                cards.setCardGroupType(CardGroupType.D_SEQUENCE);//杂顺
                            }
                            cards.setMaxCard(cards.getCards().get(4));
                            return true;
                        }else{
                            if(cards.isSameSuits()){
                                cards.setCardGroupType(CardGroupType.SAMESUITS);//同花五
                                cards.setMaxCard(cards.getCards().get(4));
                                return true;
                            }else
                                return false;
                        }
                    }
                }
                else{
                    cards.sort();
                    Card temp=cards.getCards().get(0);
                    for(int i = 1; i<cards.size(); i++){
                        if(cards.getCards().get(i).getRank().getWeight()  - temp.getRank().getWeight()!=1){
                            if(cards.isSameSuits())
                            {
                                cards.setCardGroupType(CardGroupType.SAMESUITS);//同花五
                                cards.sort();
                                cards.setMaxCard(cards.getCards().get(4));
                                return true;
                            }else{
                                cards.setCardGroupType(CardGroupType.UNKNOWN);
                                return false;
                            }
                        }
                    }
                    if(cards.isSameSuits()){
                        cards.setCardGroupType(CardGroupType.S_SEQUENCE);//同花顺
                        cards.sort();
                        cards.setMaxCard(cards.getCards().get(4));
                    }
                    else{
                        cards.sort();
                        cards.setMaxCard(cards.getCards().get(4));
                        cards.setCardGroupType(CardGroupType.D_SEQUENCE);//杂顺
                    }
                    return true;
                }



            }
        }
        cards.setCardGroupType(CardGroupType.UNKNOWN);
        return false;
    }

    public boolean compareToCards(CardGroup preCardGroup,CardGroup curCardGroup){
        return curCardGroup.compare(preCardGroup);
    }
}
