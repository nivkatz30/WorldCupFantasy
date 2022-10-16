package summer.rest.worldcupfantasy.models;

import lombok.Data;
import lombok.Value;

/**
 * This class represent a gamble result request.
 */
@Value
public class GameResultRequest {
    private Long gameId;
    private Integer homeScore;
    private Integer awayScore;
}
