package com.cdd_game.Rule;
import com.cdd_game.Card.*;
import com.cdd_game.Player.Player;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: 实现策略模式，拼接游戏规则：几人局、校验规则等待
public interface Rule {
    abstract boolean validate(CardGroup cards);
    abstract HashMap<Player,Integer> computeGameScore(HashMap<Player, CardPool> remainingCards);

}
