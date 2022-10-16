package summer.rest.worldcupfantasy.models;

import lombok.Value;

/**
 * This class represent a sing-up request.
 */
@Value
public class SignUpRequest {

    private String nickname;
    private String password;
}
