package summer.rest.worldcupfantasy.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import summer.rest.worldcupfantasy.entities.League;

import java.util.List;

/**
 *This class expose only the client necessary arguments of League.
 */
@JsonPropertyOrder({"leagueId","name", "users"})
public class LeagueDTO {

    private final League league;
    private List<UserDTO> users;

    public Long getLeagueId() {
        return league.getLeagueId();
    }

    public String getName() {
        return league.getName();
    }

    public List<UserDTO> getUsers() {
        return this.users;
    }

    public LeagueDTO(League league, List<UserDTO> users) {
        this.league = league;
        this.users = users;
    }
}
