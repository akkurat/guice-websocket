package com.asafalima.websocket.endpoints;

import com.asafalima.websocket.services.GameFactory;
import com.asafalima.websocket.services.GameList;
import com.asafalima.websocket.services.ProxyGame;
import com.asafalima.websocket.services.ProxyInstanceableGame;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
public class GamePoint {

    private GameList gl;
    private GameFactory gf;

    GamePoint(GameList gl, GameFactory gf) {
        this.gl = gl;
        this.gf = gf;
    }

    @RequestMapping("/api/games")
    public List<ProxyGame> list() {
        return gl.getAllGames();
    }

    @RequestMapping("/api/gametypes")
    public Map<String, ProxyInstanceableGame> listPossibleGames() {
        return gf.getPossibleGames();
    }

    @RequestMapping("/api/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    @RequestMapping("/api/user")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
