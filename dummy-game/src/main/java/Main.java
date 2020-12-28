import ch.taburett.jass.game.ChatMessage;
import ch.taburett.jass.game.Game;
import ch.taburett.jass.game.spi.def.ModeDesider;
import cli.CLIRouter;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {

        var modes = new ModeDesider();

        var game = new Game(modes);

        var cliRouter = new CLIRouter();
//        game.start(cliRouter);

//        game.receive( new ChatMessage(new Game.PlayerReference("a"), new Game.PlayerReference("b"), "GAAAAGI"));

    }
}
