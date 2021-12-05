package rps.model;

import java.util.Comparator;

public class OptionComparator implements Comparator<Option> {

    @Override
    public int compare(Option o1, Option o2) {
        if (o1 == o2) {
            return 0;
        }
        if ((o1 == Option.SCISSORS && o2 == Option.PAPER) ||
                (o1 == Option.PAPER && o2 == Option.ROCK) ||
                (o1 == Option.ROCK && o2 == Option.SCISSORS)) {
            return 1;
        } else {
            return -1;
        }
    }
}
