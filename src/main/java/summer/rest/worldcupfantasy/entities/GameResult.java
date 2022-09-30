package summer.rest.worldcupfantasy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Integer homeScore;
    Integer awayScore;
    String result;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "game_id", nullable = false)
    Game game;

    public GameResult(Game game,Integer homeScore, Integer awayScore) {
        this.game = game;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.result = homeScore > awayScore ? "1" : awayScore > homeScore ? "2" : "X";
    }
}
