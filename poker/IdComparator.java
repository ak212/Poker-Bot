package poker;

import java.util.Comparator;

public class IdComparator implements Comparator<Player> {

   public int compare(Player player1, Player player2) {
      return player1.id - player2.id;
   }
}
