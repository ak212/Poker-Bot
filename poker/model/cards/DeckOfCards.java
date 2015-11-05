package poker.model.cards;
import java.util.ArrayList;
import java.util.Collections;

public class DeckOfCards {
   private ArrayList<Card> deck;

   public DeckOfCards() {
      setDeck(new ArrayList<Card>());

      for (int i = 0; i < 4; i++) {
         for (int j = 2; j < 15; j++) {
            getDeck().add(new Card(Suit.getSuit(i), Rank.getRank(j)));
         }
      }

      Collections.shuffle(getDeck());
   }

   public ArrayList<Card> getDeck() {
      return deck;
   }

   public void setDeck(ArrayList<Card> deck) {
      this.deck = deck;
   }
}
