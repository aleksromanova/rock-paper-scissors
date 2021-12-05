package rps.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticsResponse {
    public StatisticsResponse(int won, int lost, int draw) {
        this.won = won;
        this.lost = lost;
        this.draw = draw;
    }

    private int won;
    private int lost;
    private int draw;
}
