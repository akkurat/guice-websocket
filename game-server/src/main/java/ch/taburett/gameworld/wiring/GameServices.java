package ch.taburett.gameworld.wiring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GameServices {

//    @Bean
//    public GameList mainGameList() {
//        return new GameList();
//    }

//    @Bean
//    public GameLogic gameLogic() {
//        return new GameLogic();
//    }

//    @Bean
//    public CmdSendingService() service {
//        return new CmdSendingService();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

}
