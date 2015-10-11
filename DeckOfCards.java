import java.util.ArrayList;
import java.util.Collections;

public class DeckOfCards {
   ArrayList<Card> deck;

   public DeckOfCards() {
      deck = new ArrayList<Card>();

      for (int i = 0; i < 4; i++) {
         for (int j = 2; j < 15; j++) {
            deck.add(new Card(Suit.getSuit(i), Rank.getRank(j)));
         }
      }

      Collections.shuffle(deck);
   }
}
