package com.cdd_game.Rule;
import com.cdd_game.Card.*;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: 实现策略模式，拼接游戏规则：几人局、校验规则等待
public interface Rule {
    abstract boolean validate(CardGroup cards);

}
