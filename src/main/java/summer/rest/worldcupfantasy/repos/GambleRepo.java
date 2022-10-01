package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.User;

import java.util.List;
import java.util.Optional;

public interface GambleRepo extends JpaRepository<Gamble,Long> {
    List<Gamble> findByGame(Game game);
    Optional<Gamble> findByUserAndGame(User user, Game game);
}
