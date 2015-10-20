package poker;

import java.util.Comparator;

public class PositionComparator implements Comparator<Player> {

   public int compare(Player player1, Player player2) {
      return player1.position - player2.position;
   }
}
