package summer.rest.worldcupfantasy.services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import summer.rest.worldcupfantasy.entities.User;

import java.util.Date;
import java.util.HashMap;

/**
 * This class manage all the Token actions.
 */
@Service
public class TokenService {

    private final String JWT_SECRET = "WorldCupFantasy.niv.david";

    /**
     * This method generate a new token for specific user.
     * @param user
     * @return
     */
    public String generateJwtToken(User user) {
        return Jwts.builder().setClaims(new HashMap<>()).setSubject(user.getUserId().toString())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();
    }

    /**\
     * This method check the validity of the givan token by the client.
     * @param token
     * @return
     */
    public Boolean validateJwtToken(String token) {
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        return true;
    }

    /**
     * This method extract the user ID from existing token.
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        String userId = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(userId);
    }
}
