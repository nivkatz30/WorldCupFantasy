package summer.rest.worldcupfantasy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Gamble extends Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long gambleId;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    Game game;

    public Gamble(Integer homeScore, Integer awayScore, User user, Game game) {
        super(homeScore, awayScore);
        this.user = user;
        this.game = game;
    }

    public Gamble(User user, Game game) {
        this.game = game;
        this.user = user;
    }
}
