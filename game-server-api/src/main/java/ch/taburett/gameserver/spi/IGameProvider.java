package ch.taburett.gameserver.spi;

import java.util.Map;

public interface IGameProvider {

    Map<String, IProxyInstanceableGame> getGames();
}
