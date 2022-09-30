package summer.rest.worldcupfantasy.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    String nickname;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    List<League> leagues = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    List<Gamble> gambles = new ArrayList<>();

    public User(String nickname) {
        this.nickname = nickname;
    }

}
