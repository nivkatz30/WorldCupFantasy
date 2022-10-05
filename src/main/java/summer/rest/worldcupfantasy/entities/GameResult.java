package summer.rest.worldcupfantasy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class GameResult extends Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "game_id", nullable = false)
    Game game;

    public GameResult(Game game,Integer homeScore, Integer awayScore) {
        super(homeScore,awayScore);
        this.game = game;
    }

    public GameResult(Game game) {
        this.game = game;
    }
}
