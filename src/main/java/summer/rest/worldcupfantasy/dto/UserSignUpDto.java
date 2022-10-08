package summer.rest.worldcupfantasy.dto;


import summer.rest.worldcupfantasy.entities.User;

public class UserSignUpDto extends UserDTO {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserSignUpDto(User user, Integer score, String token) {
        super(user, score);
        this.token = token;
    }
}
