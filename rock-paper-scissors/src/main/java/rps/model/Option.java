package rps.model;

import lombok.Getter;

@Getter
public enum Option {
    ROCK, PAPER, SCISSORS;

    private Option killer;

    static {
        ROCK.killer = PAPER;
        PAPER.killer = SCISSORS;
        SCISSORS.killer = ROCK;
    }
}
