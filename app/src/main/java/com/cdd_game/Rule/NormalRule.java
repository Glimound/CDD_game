package com.cdd_game.Rule;
import com.cdd_game.Card.*;
import com.cdd_game.Player.Player;

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

    /**通过调用计算牌分的函数 computeCardScores()来计算每个玩家的得分
     * 传入的参数为玩家到玩家剩余牌的映射
     * 返回值为玩家到玩家得分的映射
     * @param remainingCards
     * @return
     */
    public HashMap<Player,Integer> computeGameScore(HashMap<Player, CardPool> remainingCards){
        HashMap<Player,Integer> playerGameScores=new HashMap<>();
        HashMap<Player,Integer> cardScores=this.computeCardScore(remainingCards);
        for(Player player:cardScores.keySet()){
            int scores=0;
            for(Player player1:cardScores.keySet()){
                if(player1.equals(player))
                    scores-=3*cardScores.get(player1);
                else scores+=cardScores.get(player1);
            }
            playerGameScores.put(player,scores);
        }
        return playerGameScores;
    }

    /**计算玩家的牌分
     * 规则：记剩余牌为n,
     * (1)n<8时，牌分为n
     * (2)8≤n<l0时，牌分为2n
     * (3)10≤n<13时，牌分为3n
     * (4)n=13时，牌分为4n
     * (5)如果游戏结束时，手上还有8张或更多的牌，同时有黑桃2，牌分还要再乘以2
     * 传入的参数为玩家到玩家剩余牌的映射
     * 返回值为玩家到玩家得分的映射
     * @param remainingCards
     * @return
     */
    private HashMap<Player,Integer> computeCardScore(HashMap<Player, CardPool> remainingCards){
        HashMap<Player,Integer> playerCardScores=new HashMap<>();
        for(Player player:remainingCards.keySet()){
            //根据剩余牌数计算分数
            if(remainingCards.get(player).size()<8){
                player.setOwnCardScore(remainingCards.get(player).size());
            }
            else if(remainingCards.get(player).size()<10&&remainingCards.get(player).size()>=8){
                player.setOwnCardScore(remainingCards.get(player).size()*2);
            }
            else if (remainingCards.get(player).size()<13&&remainingCards.get(player).size()>=10) {
                player.setOwnCardScore(remainingCards.get(player).size()*3);
            }
            else if (remainingCards.get(player).size()==14) {
                player.setOwnCardScore(remainingCards.get(player).size()*4);
            }
            //如果玩家手中剩余牌不少于8，且有黑桃2则得分再翻倍
            if(remainingCards.get(player).size()>=8&&remainingCards.get(player).getCardBySuitAndRank(CardSuit.SPADE,CardRank.Card_2)!=null){
                player.setOwnCardScore(player.getOwnCardScore()*2);
            }
        }
        return playerCardScores;
    }
}
