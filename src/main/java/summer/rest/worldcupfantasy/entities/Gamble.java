package summer.rest.worldcupfantasy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Gamble {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long gambleId;

    Integer homeScore;
    Integer awayScore;
    String result;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    Game game;

    public void setResult() {
        this.result = homeScore > awayScore ? "1" : awayScore > homeScore ? "2" : "X";
    }

    public Gamble(Integer homeScore, Integer awayScore, User user, Game game) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.user = user;
        this.game = game;
        this.setResult();
    }

    public Gamble(User user, Game game) {
        this.game = game;
        this.user = user;
    }
}
