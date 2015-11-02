package poker;

import java.util.ArrayList;

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
