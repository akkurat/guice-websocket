package ch.taburett.gameworld.endpoints;

import ch.taburett.gameserver.spi.IProxyInstanceableGame;
import ch.taburett.gameworld.services.GameFactory;
import ch.taburett.gameworld.services.GameStorage;
import ch.taburett.gameworld.services.ProxyGame;
import ch.taburett.gameworld.wiring.User;
import ch.taburett.gameworld.wiring.UserDto;
import ch.taburett.gameworld.wiring.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
public class GamePoint {

    private GameStorage gs;
    private GameFactory gf;
    private UserService userService;

    GamePoint(GameStorage gs, GameFactory gf, UserService userService) {
        this.gs = gs;
        this.gf = gf;
        this.userService = userService;
    }

    @RequestMapping("/api/games")
    public List<ProxyGame> list() {
        return gs.getAllGames();
    }

    @RequestMapping("/api/gametypes")
    public Map<String, IProxyInstanceableGame> listPossibleGames() {
        return gf.getPossibleGames();
    }

    @RequestMapping("/api/user")
    public Principal getUser(Principal principal) {
        return principal;
    }


    @RequestMapping(value="/api/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/api/registration", method = RequestMethod.POST)
    public String createNewUser(
            @RequestBody UserDto user

            ) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUsername());
        if (userExists != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a user registered with the user name provided");
        } else {
            User userf = new User(user.getUsername(), user.getPassword());
            userService.saveUser(userf);
            return userf.getUsername();
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    private void sendConflict() {
    }
}
