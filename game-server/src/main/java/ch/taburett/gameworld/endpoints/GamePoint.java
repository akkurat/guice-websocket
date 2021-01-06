package ch.taburett.gameworld.endpoints;

import ch.taburett.gameworld.services.GameFactory;
import ch.taburett.gameworld.services.GameStorage;
import ch.taburett.gameworld.services.ProxyInstanceableGame;
import ch.taburett.gameworld.services.ProxyGame;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
public class GamePoint {

    private GameStorage gs;
    private GameFactory gf;

    GamePoint(GameStorage gs, GameFactory gf) {
        this.gs = gs;
        this.gf = gf;
    }

    @RequestMapping("/api/games")
    public List<ProxyGame> list() {
        return gs.getAllGames();
    }

    @RequestMapping("/api/gametypes")
    public Map<String, ProxyInstanceableGame> listPossibleGames() {
        return gf.getPossibleGames();
    }

    @RequestMapping("/api/user")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
