package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import summer.rest.worldcupfantasy.entities.Game;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface GameRepo extends JpaRepository<Game,Long> {

    @Query(value = "Select Game.* \n" +
            "from Game Join (Select Min(Game_Day) as Game_Day From Game Where Date_Of_Game > current_timestamp) as Not_Played_Games\n" +
            "on Game.Game_Day = Not_Played_Games.Game_Day", nativeQuery = true)
    List<Game> getNextMatchDay();


    @Query(value = "Select * from Game Where Game.Date_Of_Game >= ?1 And Game.Date_Of_Game <= ?2", nativeQuery = true)
    List<Game> findAll(String fromDate, String toDate);

    default Game findOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST,"Game with id " + id + " not found"));
    }

}
