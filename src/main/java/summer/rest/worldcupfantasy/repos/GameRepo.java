package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface manage all the actions between the Game table in the database.
 */
@Repository
public interface GameRepo extends JpaRepository<Game,Long> {

    /**
     * This method find the available games of the next match day.
     * @return
     */
    @Query(value = "Select Game.* \n" +
            "from Game Join (Select Min(Game_Day) as Game_Day From Game Where Date_Of_Game > current_timestamp) as Not_Played_Games\n" +
            "on Game.Game_Day = Not_Played_Games.Game_Day Where Date_Of_Game > current_timestamp", nativeQuery = true)
    List<Game> getNextMatchDay();

    /**
     * This method finds all the existed games.
     * @param fromDate
     * @param toDate
     * @return
     */
    @Query(value = "Select * from Game Where Game.Date_Of_Game >= ?1 And Game.Date_Of_Game <= ?2", nativeQuery = true)
    List<Game> findAll(String fromDate, String toDate);

    /**
     * This method find a game by his ID if existed, and if not, throws an exception.
     * @param id
     * @return
     * @throws ApiErrorResponse
     */
    default Game findOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST,"Game with id " + id + " not found"));
    }

}
