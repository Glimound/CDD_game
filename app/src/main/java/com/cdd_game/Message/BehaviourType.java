package com.cdd_game.Message;

/**
 * 行为类型：
 * PLAY_CARD: 出牌，
 * PASS: 跳过出牌，
 * WIN: 玩家获胜，
 */
public enum BehaviourType {
    PLAY_CARD,
    PASS,
    NEXT_TURN,
    PLAYER_JOINED,
    SHAKE_HAND,
    READY,
    GAME_START,
    GAME_END
}
