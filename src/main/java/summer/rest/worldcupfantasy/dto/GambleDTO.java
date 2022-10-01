package summer.rest.worldcupfantasy.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import summer.rest.worldcupfantasy.entities.Gamble;
import summer.rest.worldcupfantasy.entities.User;

@Value
@JsonPropertyOrder({"gamble", "user"})
public class GambleDTO {
    private Gamble gamble;

    public GambleDTO(Gamble gamble) {
        this.gamble = gamble;
    }

    public User getUser(){
        return this.gamble.getUser();
    }
}
