package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import summer.rest.worldcupfantasy.interfaces.Score;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.List;
import java.util.Optional;

/**
 * This interface manage all the actions between the User table in the database.
 */
public interface UserRepo extends JpaRepository<User,Long> {

    /**
     * This method calculate the current score of a user by checking the result of all his gambles.
     * @param ids
     * @return
     */
    @Query(value = "SELECT Gamble.User_Id as userId, Gamble.home_Score as gambleHomeScore, Gamble.away_Score as gambleAwayScore ,Game_Result.home_Score as resultHomeScore, Game_Result.away_Score as resultAwayScore " +
            "From Gamble Inner Join Game_Result on Game_Result.game_game_Id = Gamble.game_Id " +
            "Where Gamble.result = Game_Result.result And Gamble.user_Id in ?1", nativeQuery = true)
    List<Score> getUsersScore(List<Long> ids);

    /**
     * This method find a user by his nickname.
     * @param nickname
     * @return
     */
    Optional<User> findByNickname(String nickname);

    /**
     * This method find user by his ID if existed, and id not, throws an exception.
     * @param id
     * @return
     * @throws ApiErrorResponse
     */
    default User getOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "User with id " + id + " not exist"));
    }
}
