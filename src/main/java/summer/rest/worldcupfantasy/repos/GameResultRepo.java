package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.GameResult;

import java.util.Optional;

/**
 * This interface manage all the actions between the Game result table in the database.
 */
public interface GameResultRepo extends JpaRepository<GameResult, Long> {

    /**
     * This method find a game result by specific game.
     * @param game
     * @return
     */
    Optional<GameResult> findByGame(Game game);
}
