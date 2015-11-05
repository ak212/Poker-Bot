package poker.model.hand;

import java.util.ArrayList;

public class HandStrength {
   public Hand hand;
   public ArrayList<Integer> kickers;
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers) {
      this.hand = hand;
      this.kickers = kickers;
   }
}
