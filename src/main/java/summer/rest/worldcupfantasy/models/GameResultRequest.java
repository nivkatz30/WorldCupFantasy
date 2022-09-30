package summer.rest.worldcupfantasy.models;

import lombok.Data;
import lombok.Value;

@Value
public class GameResultRequest {
    private Long gameId;
    private Integer homeScore;
    private Integer awayScore;
}
