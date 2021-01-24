package ch.taburett.gameserver.spi;

import ch.taburett.jass.game.spi.IRoundSupplier;

public interface IProxyInstanceableGame {
   /**
    * Implies to start the game, once all players are set
    * -> this does not mean starting the game immediately,
    * but the exterior setup [number of players] is fixed
    * and the the GameProxy takes over
    * @return
    */
   default boolean isFixedNumPlayers() {
      return minPlayers() == maxPlayers();
   }
   default int maxPlayers() {
      return minPlayers();
   }

   int minPlayers();

   String getCaption();

   IRoundSupplier create(String owner);

}
