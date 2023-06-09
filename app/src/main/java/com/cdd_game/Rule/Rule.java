package com.cdd_game.Rule;
import com.cdd_game.Card.*;

import java.util.ArrayList;
import java.util.HashMap;

public interface Rule {
    abstract boolean validate(CardGroup cards);

}
