package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HandEvaluator {
   public static Hand evaluateForHand(ArrayList<Card> board, HoleCards holeCards) {
      // Default to High Card
      Hand highestHand = Hand.HighCard;
      
      // Hand Booleans
      boolean onePair = false, threeOfAKind = false, straight = false, flush = false;
      
      // Check for a pocket pair if there is no board
      if (board.isEmpty()) {
         if (holeCards.card1.rank == holeCards.card2.rank) {
            return Hand.OnePair;
         }
         else {
            return Hand.HighCard;
         }
      }
      
      ArrayList<Card> boardAndHoleCards = board;
      boardAndHoleCards.add(holeCards.card1);
      boardAndHoleCards.add(holeCards.card2);
      
      ArrayList<ArrayList<Card>> subsets = getSubsets(boardAndHoleCards, 5);
      
      // If a wrong number of board cards are there, return null
      if (boardAndHoleCards.size() < 5 || boardAndHoleCards.size() > 7) {
         return null;
      }
      
      for (ArrayList<Card> hand : subsets) {
         ArrayList<Card> sortedHand;
         
         // Sort the hand from highest to lowest
         Collections.sort(hand, new Comparator<Card>() {
            public int compare(Card left, Card right) {
                return left.rank.getValue() - right.rank.getValue();             }
         });
         
         sortedHand = hand;
         
         // Test for same card hands (One Pair, Two Pair, Three of a Kind, Four of a Kind)
         for (Card card : hand) {
            
         }
         
         // Test for Straight
         if ( (sortedHand.get(0).rank.getValue() - sortedHand.get(1).rank.getValue() == 1) &&
              (sortedHand.get(1).rank.getValue() - sortedHand.get(2).rank.getValue() == 1) &&
              (sortedHand.get(2).rank.getValue() - sortedHand.get(3).rank.getValue() == 1) &&
              (sortedHand.get(3).rank.getValue() - sortedHand.get(4).rank.getValue() == 1)) {
            straight = true;
            if (highestHand.getValue() < Hand.Straight.getValue()) {
               highestHand = Hand.Straight;
            }
         }
         
         // Test for Wheel Straight
         
         // Test for Flush
         
         // Test for Full House
         
         // Test for Four of a Kind
         
         // Test for Straight Flush
      }
      
      // Return the highest hand
      return highestHand;
   }
   
   private static ArrayList<ArrayList<Card>> getSubsets(ArrayList<Card> superSet, int k) {
      ArrayList<ArrayList<Card>> res = new ArrayList<>();
      getSubsets(superSet, k, 0, new ArrayList<Card>(), res);
      return res;
   }
   
   private static void getSubsets(ArrayList<Card> superSet, int k, int idx, ArrayList<Card> current, ArrayList<ArrayList<Card>> solution) {
      if (current.size() == k) {
          solution.add(new ArrayList<>(current));
          return;
      }
      
      if (idx == superSet.size()) return;
      
      Card x = superSet.get(idx);
      current.add(x);
      getSubsets(superSet, k, idx+1, current, solution);
      
      current.remove(x);
      getSubsets(superSet, k, idx+1, current, solution);
  }
}

