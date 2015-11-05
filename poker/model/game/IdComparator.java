package poker.model.game;

import java.util.Comparator;

import poker.model.player.Player;

public class IdComparator implements Comparator<Player> {

   public int compare(Player player1, Player player2) {
      return player1.getId() - player2.getId();
   }
}
