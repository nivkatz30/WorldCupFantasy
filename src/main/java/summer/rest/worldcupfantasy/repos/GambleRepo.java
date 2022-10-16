package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * This interface manage all the actions between the Gamble table in the database.
 */
public interface GambleRepo extends JpaRepository<Gamble,Long> {
    /**
     * This method finds a gambles by the related game.
     * @param game
     * @return
     */
    List<Gamble> findByGame(Game game);

    /**
     * This method finds a gamble by the related user and game.
     * @param user
     * @param game
     * @return
     */
    Optional<Gamble> findByUserAndGame(User user, Game game);
}
