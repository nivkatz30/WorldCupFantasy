package summer.rest.worldcupfantasy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import summer.rest.worldcupfantasy.models.UserRole;
import summer.rest.worldcupfantasy.repos.UserRepo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a User.
 */
@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    @JsonIgnore
    String password;
    @JsonIgnore
    UserRole role;
    String nickname;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Gamble> gambles = new ArrayList<>();

    public User(String nickname, String password,UserRole role) {
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }
}
