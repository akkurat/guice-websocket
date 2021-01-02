package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.*;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
