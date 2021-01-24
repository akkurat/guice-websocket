package ch.taburett.gameworld.wiring;

import lombok.Data;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Set;

@Table(name = "users")
@Entity
@Data
@EnableAutoConfiguration
public class User {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column()
    private String username;

    @Column()
    private String password;

    @Column()
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
