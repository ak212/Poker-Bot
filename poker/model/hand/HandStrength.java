package poker.model.hand;

import java.util.ArrayList;
import poker.model.cards.Card;
import poker.model.cards.Suit;
import poker.model.cards.Rank;

public class HandStrength {
   public Hand hand;
   public ArrayList<Integer> kickers;
   public boolean flushDraw;
   public Card highFlushDrawCard;
   public boolean straightDraw;
   public Card highStraightDrawCard;
   public int numRelevantOverCards;
   public int numHandCardsOnBoard;
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers) {
      this.hand = hand;
      this.kickers = kickers;
      this.flushDraw = false;
      this.highFlushDrawCard = new Card(Suit.getSuit(0), Rank.getRank(1));
      this.straightDraw = false;
      this.highStraightDrawCard = new Card(Suit.getSuit(0), Rank.getRank(1));
      this.numRelevantOverCards = 0;
      this.numHandCardsOnBoard = 0;
   }
}
