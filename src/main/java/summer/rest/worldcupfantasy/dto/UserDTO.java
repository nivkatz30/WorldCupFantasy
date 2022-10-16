package summer.rest.worldcupfantasy.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import summer.rest.worldcupfantasy.entities.User;

/**
 *This class expose only the client necessary arguments of User.
 */
@JsonPropertyOrder({"user,score"})
public class UserDTO {

    private Integer score;
    private final User user;

    public Integer getScore() {
        return this.score;
    }

    public User getUser() {
        return user;
    }

    public UserDTO(User user, Integer score) {
        this.user = user;
        this.score = score;
    }
}
