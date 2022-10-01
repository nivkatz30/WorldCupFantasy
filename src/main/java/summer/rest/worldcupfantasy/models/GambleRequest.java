package summer.rest.worldcupfantasy.models;

import lombok.Value;

@Value
public class GambleRequest {
    private Long gameId;
    private Long userId;
    private Integer homeScore;
    private Integer awayScore;
}
