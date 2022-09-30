package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.GameResult;

public interface GameResultRepo extends JpaRepository<GameResult, Long> {
}
