package com.cdd_game.Game;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardPool;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Card.CardRank;
import com.cdd_game.Card.CardSuit;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 游戏单例类，包含控制游戏流程的相关方法。
 * 每一局牌局都应有不同且唯一的Game实例。
 */
public class Game {
    private static Game gameInstance = null;
    private String gameID;
    private CardPool initialCards;
    private CardPool discardCards;
    private Rule rule;
    private Set<Player> players;
    private ArrayList<Player> playerOrder;
    private int playerNum;
    private int gameTurn;

    private Game(String gameID, CardPool initialCards, CardPool discardCards, Rule rule , Set<Player> players) {
        this.gameID = gameID;
        this.initialCards = initialCards;
        this.discardCards = discardCards;
        this.rule = rule;
        this.players = players;
        this.playerNum = players.size();
        this.gameTurn = 0;
        this.playerOrder = new ArrayList<>();
    }

    /**
     * 返回游戏实例
     */
    public static Game getGameInstance() {
        return gameInstance;
    }

    /**
     * 初始化游戏：创建游戏实例，设置游戏ID、卡池、规则、player集合；
     * 初始化Player对象中的卡池
     */
    private void initialize(String gameID, Rule rule, Set<Player> players) throws Exception {
        // TODO: if (rule.getGameType().equals("CDD"))
        CardPoolFactory factory = new CardPoolFactory();
        CardPool initialCards = factory.createCardPool("withoutJokers");
        CardPool discardCards = factory.createCardPool("empty");
        gameInstance = new Game(gameID, initialCards, discardCards, rule, players);

        for (Player player : players) {
            player.setOwnCards(factory.createCardPool("empty"));
        }
    }

    /**
     * 开始游戏：初始化游戏、发牌、设置玩家出牌顺序
     */
    public void start(String gameID, Rule rule, Set<Player> players, Player winner) throws Exception {
        if (gameInstance == null) {
            initialize(gameID, rule, players);
            dealCards();
            setPlayerOrder(winner);
        }
    }

    /**
     * 打乱牌堆，将牌堆均等分为n份（n为玩家人数），并分配给每个玩家
     */
    private void dealCards() {
        initialCards.shuffle();
        for (Player player : players) {
            // TODO:不限于玩家人数（使用策略模式，拼接不同的Rule）
            // i <= initialCards.size() % ruleStrategy.getPlayerNumRule().getPlayerNum()
            for (int i = 0; i < initialCards.size() % 4; i++) {
                player.getOwnCards().addCard(initialCards.removeLastCard());
            }
        }
    }

    /**
     * 设置玩家出牌顺序，按照出牌顺序将Player依次放入playerOrder中。
     * 若winner为null，则出牌顺序为：拥有方片3的Player->下家->下下家->下下下家；
     * 若winner不为null，则出牌顺序为：winner->下家->下下家->下下下家（下家通过加入房间顺序确定）
     * @param winner 上一局获胜的玩家
     */
    private void setPlayerOrder(Player winner) {
        if (winner == null) {
            for (Player player: players) {
                if (player.getOwnCards().getCardBySuitAndRank(CardSuit.DIAMOND, CardRank.Card_3) != null) {
                    playerOrder.add(player);
                    break;
                }
            }
        } else {
            playerOrder.add(winner);
        }

        GameRoom gameRoom = GameRoom.getGameRoomInstance();
        Player player2 = gameRoom.getNextPlayer(playerOrder.get(0));
        Player player3 = gameRoom.getNextPlayer(player2);
        Player player4 = gameRoom.getNextPlayer(player3);
        playerOrder.add(player2);
        playerOrder.add(player3);
        playerOrder.add(player4);

        /* TODO:不限于玩家人数（使用策略模式，拼接不同的Rule）
        Player playerTmp = playerOrder.get(0);
        for (int i = 0; i < ruleStrategy.getPlayerNumRule().getPlayerNum() - 1; i++) {
            playerTmp = gameRoom.getNextPlayer(playerTmp);
            playerOrder.add(playerTmp);
        }
         */
    }

    /**
     * 返回当前回合该出牌的玩家
     */
    public Player getPlayerToPlayCard() {
        return playerOrder.get(gameTurn % 4);
        /* TODO:不限于玩家人数（使用策略模式，拼接不同的Rule）
        return playerOrder.get(gameTurn % ruleStrategy.getPlayerNumRule().getPlayerNum());
         */
    }

    /**
     * TODO: 涉及蓝牙连接
     * 切换至下一个玩家出牌，向玩家发送信息，并更改对应的state；应当由玩家的State实例调用（可通过Controller）。
     */
    public void nextTurn() {
        Player player = getPlayerToPlayCard();
        // 发送消息给对应player，切换状态
        // 回合数+1（不应放在此处，应放在玩家出完牌后）
    }

    /**
     * TODO: 涉及蓝牙连接
     * 结束游戏，向玩家发送信息，并更改对应的state；应当由玩家的State实例调用（可通过Controller）。
     * 恢复GameRoom和Player等实例中部分具有持久性的变量恢复至Game实例初始化之前的状态
     */
    public void endGame() {
        // 获得输赢信息
        // 结算 getGameScore()
        // 根据游戏输赢发送消息给对应player

        for (Player player : players) {
            player.setOwnCards(null);
        }

        //GameRoom.getGameRoomInstance().setWinner(//    );
        // 保存称号为文件记录
    }

    /**
     * 返回牌局中每个玩家的最终得分
     */
    private HashMap<Player, Integer> getGameScore() throws Exception {
        HashMap<Player, CardPool> remainingCards = new HashMap<>();
        for (Player player : players) {
            remainingCards.put(player, player.getOwnCards());
        }
        return rule.computeGameScore(remainingCards);
    }

    public void addPlayer(Player player) {
        players.add(player);
        playerNum++;
    }


    public Rule getRule(){
        return rule;
    }

    public CardPool getInitialCards() {
        return initialCards;
    }

    public CardPool getDiscardCards() {
        return discardCards;
    }

    public String getGameID() {
        return gameID;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public ArrayList<Player> getPlayerOrder() {
        return playerOrder;
    }

    public int getGameTurn() {
        return gameTurn;
    }

    public void gameTurnPlusOne() {
        gameTurn++;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
