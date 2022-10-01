package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.Game;

import java.util.List;

public interface GambleRepo extends JpaRepository<Gamble,Long> {
    List<Gamble> findByGame(Game game);
}
