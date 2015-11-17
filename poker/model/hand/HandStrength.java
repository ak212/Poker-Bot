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
   public int openendedStraightDrawHighCard;
   public int numCardsOnBoard;
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers) {
      this.hand = hand;
      this.kickers = kickers;
      this.flushDraw = false;
      this.gutshotStraightDraw = false;
      this.openendedStraightDraw = false;
      this.numCardsOnBoard = 0;
      this.openendedStraightDrawHighCard = 0;
   }
   
   public HandStrength(Hand hand, ArrayList<Integer> kickers, boolean flushDraw,
         boolean gutshotStraightDraw, boolean openendedStraightDraw, 
         int numCardsOnBoard, int openendedStraightDrawHighCard) {
      this.hand = hand;
      this.kickers = kickers;
      this.flushDraw = flushDraw;
      this.gutshotStraightDraw = gutshotStraightDraw;
      this.openendedStraightDraw = openendedStraightDraw;
      this.numCardsOnBoard = numCardsOnBoard;
      this.openendedStraightDrawHighCard = openendedStraightDrawHighCard;
   }
}
