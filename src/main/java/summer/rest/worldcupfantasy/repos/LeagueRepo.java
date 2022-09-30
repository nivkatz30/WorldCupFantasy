package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import summer.rest.worldcupfantasy.entities.League;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;

import java.util.Optional;


public interface LeagueRepo extends JpaRepository<League, Long> {

    default League getOrThrowById(Long id) throws ApiErrorResponse {
        return this.findById(id).orElseThrow(() -> new ApiErrorResponse(HttpStatus.BAD_REQUEST, "League with id " + id + " not found"));
    }

    Optional<League> findByName(String name);

}


