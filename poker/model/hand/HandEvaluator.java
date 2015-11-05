package poker.model.hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.cards.Rank;
import poker.model.player.Player;

public class HandEvaluator {
   
   // TODO Convert to return a HandStrength 
   public static Hand evaluateForHand(ArrayList<Card> board, HoleCards holeCards) {
      // Default to High Card
      Hand highestHand = Hand.HighCard;
      
      // Check for a pocket pair if there is no board
      if (board.isEmpty()) {
         if (holeCards.getCard1().getRank() == holeCards.getCard2().getRank()) {
            return Hand.OnePair;
         }
         else {
            return Hand.HighCard;
         }
      }
      
      ArrayList<Card> boardAndHoleCards = board;
      boardAndHoleCards.add(holeCards.getCard1());
      boardAndHoleCards.add(holeCards.getCard2());
      
      ArrayList<ArrayList<Card>> subsets = getSubsets(boardAndHoleCards, 5);
      
      // If a wrong number of board cards are there, return null
      if (boardAndHoleCards.size() < 5 || boardAndHoleCards.size() > 7) {
         return null;
      }
      
      for (ArrayList<Card> hand : subsets) {
         // Hand Booleans
         boolean onePair = false, threeOfAKind = false, straight = false, flush = false;
         
         // Card and Rank tallies
         int rank[] = new int[15], suit[] = new int[4];
         
         ArrayList<Card> sortedHand;
         
         // Sort the hand from lowest to highest
         Collections.sort(hand, new Comparator<Card>() {
            public int compare(Card left, Card right) {
                return left.getRank().getValue() - right.getRank().getValue();             }
         });
         
         sortedHand = hand;
         
         // Get tallies
         for (Card card : hand) {
            rank[card.getRank().getValue()] += 1;
            suit[card.getSuit().getValue()] += 1;
         }
         
         // Test for same card hands (One Pair, Two Pair, Three of a Kind, Four of a Kind)
         for (int count : rank) {
            // One Pair or Two Pair
            if (count == 2) {
               if (onePair == false) {
                  onePair = true;
                  if (highestHand.getValue() < Hand.OnePair.getValue()) {
                     highestHand = Hand.OnePair;
                  }
               }
               else {
                  onePair = false;
                  if (highestHand.getValue() < Hand.TwoPair.getValue()) {
                     highestHand = Hand.TwoPair;
                  }
               }
            }
            // Three of a Kind
            if (count == 3) {
               threeOfAKind = true;
               if (highestHand.getValue() < Hand.ThreeOfAKind.getValue()) {
                  highestHand = Hand.ThreeOfAKind;
               }
            }
            // Four of a Kind
            if (count == 4) {
               if (highestHand.getValue() < Hand.FourOfAKind.getValue()) {
                  highestHand = Hand.FourOfAKind;
               }
            }
         }
         
         // Test for Straight
         if ( (sortedHand.get(4).getRank().getValue() - sortedHand.get(3).getRank().getValue() == 1) &&
              (sortedHand.get(3).getRank().getValue() - sortedHand.get(2).getRank().getValue() == 1) &&
              (sortedHand.get(2).getRank().getValue() - sortedHand.get(1).getRank().getValue() == 1) &&
              (sortedHand.get(1).getRank().getValue() - sortedHand.get(0).getRank().getValue() == 1)) {
            straight = true;
            if (highestHand.getValue() < Hand.Straight.getValue()) {
               highestHand = Hand.Straight;
            }
         }
         
         // Test for Wheel Straight
         if ( (sortedHand.get(0).getRank().getValue() == Rank.TWO.getValue()) &&
              (sortedHand.get(1).getRank().getValue() == Rank.THREE.getValue()) &&
              (sortedHand.get(2).getRank().getValue() == Rank.FOUR.getValue()) &&
              (sortedHand.get(3).getRank().getValue() == Rank.FIVE.getValue()) &&
              (sortedHand.get(4).getRank().getValue() == Rank.ACE.getValue())) {
             straight = true;
             if (highestHand.getValue() < Hand.Straight.getValue()) {
                highestHand = Hand.Straight;
             }
          }
         
         // Test for Flush
         for (int count : suit) {
            if (count == 5) {
               flush = true;
               if (highestHand.getValue() < Hand.Flush.getValue()) {
                  highestHand = Hand.Flush;
               }
            }
         }
         
         // Test for Full House
         if (onePair && threeOfAKind) {
            if (highestHand.getValue() < Hand.FullHouse.getValue()) {
               highestHand = Hand.FullHouse;
            }
         }
         
         // Test for Straight Flush
         if (straight && flush) {
            if (highestHand.getValue() < Hand.StraightFlush.getValue()) {
               highestHand = Hand.StraightFlush;
            }
         }
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
   
   
   // Currently only works for same card hands and odesn't account for kickers
   public static Player breakTie(ArrayList<Player> tiedPlayers, ArrayList<Card> board, Hand hand) {
      
      Player winningPlayer = null;
      int bestRank = Rank.TWO.getValue();
      
      
      for (Player player : tiedPlayers) {
         int rank[] = new int[15];
         
         ArrayList<Card> boardAndHoleCards = board;
         boardAndHoleCards.add(player.getHoleCards().getCard1());
         boardAndHoleCards.add(player.getHoleCards().getCard2());
         
         ArrayList<Card> sortedHand;
         
         // Sort the hand from lowest to highest
         Collections.sort(boardAndHoleCards, new Comparator<Card>() {
            public int compare(Card left, Card right) {
                return left.getRank().getValue() - right.getRank().getValue();
            }
         });
         
         sortedHand = boardAndHoleCards;
         
         // Get tallies
         for (Card card : boardAndHoleCards) {
            rank[card.getRank().getValue()] += 1;
         }
      
         switch (hand) {
         case HighCard:
            winningPlayer = tiedPlayers.get(0);
            break;
         case OnePair:
            for (int i = 0; i < rank.length; i++) {
               if (rank[i] == 2) {
                  if (i > bestRank) {
                     bestRank = i;
                     winningPlayer = player;
                  }
               }
            }
            break;
         case TwoPair:
            for (int i = 0; i < rank.length; i++) {
               if (rank[i] == 2) {
                  if (i > bestRank) {
                     bestRank = i;
                     winningPlayer = player;
                  }
               }
            }
            break;
         case ThreeOfAKind:
            for (int i = 0; i < rank.length; i++) {
               if (rank[i] == 3) {
                  if (i > bestRank) {
                     bestRank = i;
                     winningPlayer = player;
                  }
               }
            }
            break;
         case Straight:
            winningPlayer = tiedPlayers.get(0);
            break;
         case Flush:
            winningPlayer = tiedPlayers.get(0);
            break;
         case FullHouse:
            winningPlayer = tiedPlayers.get(0);
            break;
         case FourOfAKind:
            for (int i = 0; i < rank.length; i++) {
               if (rank[i] == 4) {
                  if (i > bestRank) {
                     bestRank = i;
                     winningPlayer = player;
                  }
               }
            }
            break;
         case StraightFlush:
            winningPlayer = tiedPlayers.get(0);
            break;
         default:
            break;
         }
      }
      
      
      return winningPlayer;
   }
}

