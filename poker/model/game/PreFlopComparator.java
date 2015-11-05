package poker.model.game;

import java.util.Comparator;

import poker.model.player.Player;

public class PreFlopComparator implements Comparator<Player> {

   public int compare(Player player1, Player player2) {
      return player1.getPreFlopPosition() - player2.getPreFlopPosition();
   }
}
