package poker;

import java.util.Comparator;

public class PreFlopComparator implements Comparator<Player> {

   public int compare(Player player1, Player player2) {
      return player1.preFlopPosition - player2.preFlopPosition;
   }
}
