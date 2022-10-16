package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import summer.rest.worldcupfantasy.entities.League;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.Optional;

/**
 * This interface manage all the actions between the League table in the database.
 */
public interface LeagueRepo extends JpaRepository<League, Long> {

    /**
     * This method find a league by his Id if existed, and if not, throws an exception.
     * @param id
     * @return
     * @throws ApiErrorResponse
     */
    default League getOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "League with id " + id + " not found"));
    }

    /**
     * This method find a league by his name.
     * @param name
     * @return
     */
    Optional<League> findByName(String name);

}


