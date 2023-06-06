package com.cdd_game.Rule;
import com.cdd_game.Card.*;

import java.util.HashMap;

public class Rule {
    public boolean validate(CardGroup cards){
        int numOfcards=cards.getCardCounter();
        
        if(numOfcards>0&&numOfcards<4){
            if(cards.isSameRanks()){
                switch(numOfcards) {
                    case 1:
                        cards.setCardGroupType("单牌");
                        break;
                    case 2:
                        cards.setCardGroupType("对子");
                        break;
                    case 3:
                        cards.setCardGroupType("三牌");
                        break;
                }
                return true;
            }
        } else if (numOfcards==5) {
            HashMap<CardRank,Integer>groupOfcards=new HashMap<>();
            for(int i=0;i<cards.getCardCounter();i++){
                Card temp=cards.getCards().get(i);
                if(groupOfcards.containsKey(temp.getRank())){
                    int num= groupOfcards.get(temp.getRank());
                    num++;
                    groupOfcards.put(temp.getRank(),num);
                }
            }
            if(groupOfcards.size()==2){
                if(groupOfcards.containsValue(3)){
                    cards.setCardGroupType("三带一对");
                    return true;
                } else if (groupOfcards.containsValue(4)) {
                    cards.setCardGroupType("四带一");
                    return true;
                }
                cards.setCardGroupType("识别错误");
                return false;

            } else if (groupOfcards.size()==5) {
                cards.sort();

                Card temp=cards.getCards().get(0);
                for(int i=1;i<cards.getCardCounter();i++){
                    if(cards.getCards().get(i).getRank().getWeight()  - temp.getRank().getWeight()!=1){
                        if(cards.isSameSuits())
                        {
                            cards.setCardGroupType("同花五");
                            return true;
                        }else{
                            cards.setCardGroupType("识别错误");
                            return false;
                        }
                    }
                }
                if(cards.isSameSuits()){
                    cards.setCardGroupType("同花顺");
                }
                else{
                    cards.setCardGroupType("杂顺");
                }
                return true;
            }
        }
        cards.setCardGroupType("识别错误");
        return false;
    }
}
