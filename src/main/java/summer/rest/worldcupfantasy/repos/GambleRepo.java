package summer.rest.worldcupfantasy.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.rest.worldcupfantasy.entities.Gamble;

public interface GambleRepo extends JpaRepository<Gamble,Long> {

}
