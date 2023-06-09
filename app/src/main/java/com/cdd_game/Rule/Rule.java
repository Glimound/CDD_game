package com.cdd_game.Rule;

import com.cdd_game.Card.CardGroup;

public interface Rule {
    abstract boolean validate(CardGroup cards);
}
