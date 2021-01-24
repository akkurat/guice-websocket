package ch.taburett.gameworld.wiring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

//    @Query("select u from User u where u.name = ?1")
    User findByUsername(String username);


}
