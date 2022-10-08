package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import summer.rest.worldcupfantasy.interfaces.Score;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<User,Long> {

    @Query(value = "SELECT Gamble.User_Id as userId, Gamble.home_Score as gambleHomeScore, Gamble.away_Score as gambleAwayScore ,Game_Result.home_Score as resultHomeScore, Game_Result.away_Score as resultAwayScore " +
            "From Gamble Inner Join Game_Result on Game_Result.game_game_Id = Gamble.game_Id " +
            "Where Gamble.result = Game_Result.result And Gamble.user_Id in ?1", nativeQuery = true)
    List<Score> getUsersScore(List<Long> ids);

    Optional<User> findByNickname(String nickname);

    default User getOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "User with id " + id + " not exist"));
    }
}
