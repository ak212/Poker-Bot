import java.util.ArrayList;

public class DeckOfCards {
   ArrayList<Card> deck;

   public DeckOfCards() {
      deck = new ArrayList<Card>();

      for (int i = 0; i < 4; i++) {
         for (int j = 1; j < 14; j++) {
            deck.add(new Card(Suit.getSuit(i), Rank.getRank(j)));
         }
      }
   }
}
