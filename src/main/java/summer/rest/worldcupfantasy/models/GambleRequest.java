package summer.rest.worldcupfantasy.models;

import lombok.Value;

/**
 * This class represent a gamble request.
 */
@Value
public class GambleRequest {
    private Long gameId;
    private Long userId;
    private Integer homeScore;
    private Integer awayScore;
}
