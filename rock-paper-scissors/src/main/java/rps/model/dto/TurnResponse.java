package rps.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TurnResponse {
    private String computerTurn;
    private String roundResult;
}
