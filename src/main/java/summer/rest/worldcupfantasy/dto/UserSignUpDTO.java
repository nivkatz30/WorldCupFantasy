package summer.rest.worldcupfantasy.dto;


import summer.rest.worldcupfantasy.entities.User;

/**
 *This class expose only the client necessary arguments of User in the sign-up step.
 */
public class UserSignUpDTO extends UserDTO {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserSignUpDTO(User user, Integer score, String token) {
        super(user, score);
        this.token = token;
    }
}
