package summer.rest.worldcupfantasy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long gameId;

    Integer gameDay;
    String homeTeam;
    String awayTeam;

    LocalDateTime dateOfGame;

    @OneToMany(mappedBy = "game")
    @JsonIgnore
    List<Gamble> gambles = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "gameResult_id", nullable = true)
    GameResult gameResult;


    public Game(Integer gameDay, String homeTeam, String awayTeam, LocalDateTime dateOfGame) {
        this.gameDay = gameDay;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.dateOfGame = dateOfGame;
    }

}
