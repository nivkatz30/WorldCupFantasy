package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.GameResult;

import java.util.Optional;

public interface GameResultRepo extends JpaRepository<GameResult, Long> {

    Optional<GameResult> findByGame(Game game);
}
