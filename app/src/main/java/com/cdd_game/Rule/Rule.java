package com.cdd_game.Rule;
import com.cdd_game.Card.*;
public class Rule {
    public boolean validate(CardPool cards){
        int numOfcards=cards.getCardCounter();
        
        if(numOfcards>0&&numOfcards<4){
            return cards.isSameRanks();
        } else if (numOfcards==5) {
            if(cards.isSameSuits()){
                return true;
            }

        }
    }
}
