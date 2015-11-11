package poker.model.hand;

import java.util.ArrayList;
import poker.model.cards.Card;
import poker.model.cards.Suit;
import poker.model.cards.Rank;

public class HandStrength {
   public Hand hand;
   public ArrayList<Integer> kickers;
   public boolean flushDraw;
   public boolean gutshotStraightDraw;
   public boolean openendedStraightDraw;
   public int numRelevantOverCards;
   public int numCardsOnBoard;
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers) {
      this.hand = hand;
      this.kickers = kickers;
      this.flushDraw = false;
      this.gutshotStraightDraw = false;
      this.openendedStraightDraw = false;
      this.numRelevantOverCards = 0;
      this.numCardsOnBoard = 0;
   }
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers, boolean flushDraw,
         boolean gutshotStraightDraw, boolean openendedStraightDraw, 
         int numRelevantOverCards, int numCardsOnBoard) {
      this.hand = hand;
      this.kickers = kickers;
      this.flushDraw = flushDraw;
      this.gutshotStraightDraw = gutshotStraightDraw;
      this.openendedStraightDraw = openendedStraightDraw;
      this.numRelevantOverCards = numRelevantOverCards;
      this.numCardsOnBoard = numCardsOnBoard;
   }
}
