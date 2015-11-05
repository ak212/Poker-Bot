package poker.model.hand;

import java.util.ArrayList;

import poker.model.cards.Card;

public class HandStrength {
   public Hand hand;
   public ArrayList<Card> kickers;
   
   public HandStrength(Hand hand) {
      this.hand = hand;
      kickers = new ArrayList<Card>();
   }
   
   public void addKicker(Card kicker) {
      this.kickers.add(kicker);
   }
}
