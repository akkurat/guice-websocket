
package ch.taburett.gamemodi.std;

import ch.taburett.gameserver.spi.IGameProvider;
import ch.taburett.gameserver.spi.IProxyInstanceableGame;

import java.util.Map;


public class JassModeProvider implements IGameProvider {

    @Override
    public Map<String, IProxyInstanceableGame> getGames() {

        var list = Map.<String,IProxyInstanceableGame>of(
                "all", new AllAllowedJassGameI(),
                "boring", new BoringAllowedJassGameI()
        );
        return list;
    }
}
