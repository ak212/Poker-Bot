package poker.model.hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.cards.Rank;
import poker.model.player.Player;

public class HandEvaluator {
   
   public static HandStrength evaluateForHand(ArrayList<Card> board, HoleCards holeCards) {
      // Default to High Card
      Hand highestHand = Hand.HighCard;
      ArrayList<Integer> kickers = new ArrayList<Integer>(5);
      ArrayList<Integer> t_kickers = new ArrayList<Integer>(5);
      
      // Check for a pocket pair if there is no board
      if (board.isEmpty()) {
         if (holeCards.getCard1().getRank() == holeCards.getCard2().getRank()) {
            return new HandStrength(Hand.OnePair, new ArrayList<Integer>(Arrays.asList(holeCards.getCard1().getRank().getValue())));
         }
         else {
            return new HandStrength(Hand.HighCard, new ArrayList<Integer>(Arrays.asList(holeCards.getCard1().getRank().getValue())));
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
               return left.getRank().getValue() - right.getRank().getValue();             
            }
         });
         
         sortedHand = hand;
         
         // Get tallies
         for (Card card : hand) {
            rank[card.getRank().getValue()] += 1;
            suit[card.getSuit().getValue()] += 1;
         }
         
         // Test for same card hands (One Pair, Two Pair, Three of a Kind, Four of a Kind)
         for (int i = 0; i < rank.length; i++) {
            // One Pair or Two Pair
            if (rank[i] == 2) {
               if (onePair == false) {
                  onePair = true;
                  if (highestHand.getValue() <= Hand.OnePair.getValue()) {
                     t_kickers.clear();
                     t_kickers.add(i);
                     for (int j = rank.length - 1; j >= 0; j--) {
                        if (rank[j] == 1) {
                           t_kickers.add(j);
                        }
                     }
                     highestHand = Hand.OnePair;
                     kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
                  }
               }
               else {
                  onePair = false;
                  if (highestHand.getValue() <= Hand.TwoPair.getValue()) {
                     t_kickers.clear();
                     t_kickers.add(i);
                     for (int j = rank.length - 1; j >= 0; j--) {
                        if (rank[j] == 2 && j != i) {
                           t_kickers.add(j);
                        }
                     }
                     for (int j = rank.length - 1; j >= 0; j--) {
                        if (rank[j] == 1) {
                           t_kickers.add(j);
                        }
                     }
                     highestHand = Hand.TwoPair;
                     kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
                  }
               }
            }
            // Three of a Kind
            if (rank[i] == 3) {
               threeOfAKind = true;
               if (highestHand.getValue() <= Hand.ThreeOfAKind.getValue()) {
                  t_kickers.clear();
                  t_kickers.add(i);
                  for (int j = rank.length - 1; j >= 0; j--) {
                     if (rank[j] == 1) {
                        t_kickers.add(j);
                     }
                  }
                  highestHand = Hand.ThreeOfAKind;
                  kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
               }
            }
            // Four of a Kind
            if (rank[i] == 4) {
               if (highestHand.getValue() <= Hand.FourOfAKind.getValue()) {
                  t_kickers.clear();
                  t_kickers.add(i);
                  for (int j = rank.length - 1; j >= 0; j--) {
                     if (rank[j] == 1) {
                        t_kickers.add(j);
                     }
                  }
                  highestHand = Hand.FourOfAKind;
                  kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
               }
            }
         }
         
         // Test for Straight
         if ( (sortedHand.get(4).getRank().getValue() - sortedHand.get(3).getRank().getValue() == 1) &&
              (sortedHand.get(3).getRank().getValue() - sortedHand.get(2).getRank().getValue() == 1) &&
              (sortedHand.get(2).getRank().getValue() - sortedHand.get(1).getRank().getValue() == 1) &&
              (sortedHand.get(1).getRank().getValue() - sortedHand.get(0).getRank().getValue() == 1)) {
            straight = true;
            if (highestHand.getValue() <= Hand.Straight.getValue()) {
               t_kickers.clear();
               t_kickers.add(sortedHand.get(4).getRank().getValue());
               highestHand = Hand.Straight;
               kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
            }
         }
         
         // Test for Wheel Straight
         if ( (sortedHand.get(0).getRank().getValue() == Rank.TWO.getValue()) &&
              (sortedHand.get(1).getRank().getValue() == Rank.THREE.getValue()) &&
              (sortedHand.get(2).getRank().getValue() == Rank.FOUR.getValue()) &&
              (sortedHand.get(3).getRank().getValue() == Rank.FIVE.getValue()) &&
              (sortedHand.get(4).getRank().getValue() == Rank.ACE.getValue())) {
             straight = true;
             if (highestHand.getValue() <= Hand.Straight.getValue()) {
                t_kickers.clear();
                t_kickers.add(sortedHand.get(3).getRank().getValue());
                highestHand = Hand.Straight;
                kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
             }
          }
         
         // Test for Flush
         for (int count : suit) {
            if (count == 5) {
               flush = true;
               if (highestHand.getValue() <= Hand.Flush.getValue()) {
                  t_kickers.clear();
                  t_kickers.add(sortedHand.get(4).getRank().getValue());
                  t_kickers.add(sortedHand.get(3).getRank().getValue());
                  t_kickers.add(sortedHand.get(2).getRank().getValue());
                  t_kickers.add(sortedHand.get(1).getRank().getValue());
                  t_kickers.add(sortedHand.get(0).getRank().getValue());
                  highestHand = Hand.Flush;
                  kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
               }
            }
         }
         
         // Test for Full House
         if (onePair && threeOfAKind) {
            if (highestHand.getValue() <= Hand.FullHouse.getValue()) {
               kickers.clear();
               for (int j = rank.length - 1; j >= 0; j--) {
                  if (rank[j] == 3) {
                     kickers.add(j);
                  }
                  if (rank[j] == 2) {
                     kickers.add(j);
                  }
               }
               highestHand = Hand.FullHouse;
               kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
            }
         }
         
         // Test for Straight Flush
         if (straight && flush) {
            if (highestHand.getValue() <= Hand.StraightFlush.getValue()) {
               kickers.clear();
               kickers.add(sortedHand.get(4).getRank().getValue());
               highestHand = Hand.StraightFlush;
               kickers = (kickers.isEmpty()) ? t_kickers : compareKickers(kickers, t_kickers);
            }
         }
         
         // Test for Best High Hand
         if (highestHand == Hand.HighCard) {
            if (kickers.isEmpty()) {
               kickers.add(sortedHand.get(4).getRank().getValue());
               kickers.add(sortedHand.get(3).getRank().getValue());
               kickers.add(sortedHand.get(2).getRank().getValue());
               kickers.add(sortedHand.get(1).getRank().getValue());
               kickers.add(sortedHand.get(0).getRank().getValue());
            }
            
            for (int i = 0; i < kickers.size(); i++) {
               if (sortedHand.get(4 - i).getRank().getValue() > kickers.get(i)) {
                  kickers.clear();
                  kickers.add(sortedHand.get(4).getRank().getValue());
                  kickers.add(sortedHand.get(3).getRank().getValue());
                  kickers.add(sortedHand.get(2).getRank().getValue());
                  kickers.add(sortedHand.get(1).getRank().getValue());
                  kickers.add(sortedHand.get(0).getRank().getValue());
               }
            }
         }
      }
      
      // Return the best hand strength
      return new HandStrength(highestHand, kickers);
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
   
   
   // Takes in tied players and returns the winners
   public static ArrayList<Player> breakTie(ArrayList<Player> tiedPlayers) {
      ArrayList<Player> winningPlayers = new ArrayList<Player>();
      ArrayList<Integer> highestKickers = tiedPlayers.get(0).getCurrentHand().kickers;
      winningPlayers.add(tiedPlayers.get(0));

      System.err.println("break tie: number of tied players: " + tiedPlayers.size());
      
      for (Player player : tiedPlayers) {
         System.err.print("HE.breakTie: Id: " + player.getId() + " kickers: ");
         for (int i = 0; i < player.getCurrentHand().kickers.size(); i++)
            System.out.print(player.getCurrentHand().kickers.get(i) + ",");
         System.err.println();

         for (int i = 0; i < player.getCurrentHand().kickers.size(); i++) {
            if (player.getCurrentHand().kickers.get(i) > highestKickers.get(i)) {
               winningPlayers.clear();
               winningPlayers.add(player);
               highestKickers = player.getCurrentHand().kickers;
            }
            if (player.getCurrentHand().kickers.get(i) == highestKickers.get(i)) {
               if (i == (player.getCurrentHand().kickers.size() - 1)) {
                  winningPlayers.add(player);
               }
            }
         }
      }
      
      Set<Player> noDup = new HashSet<Player>(winningPlayers);
      winningPlayers.clear();
      winningPlayers.addAll(noDup);

      return winningPlayers;
   }

   public static ArrayList<Integer> compareKickers(ArrayList<Integer> kickers, ArrayList<Integer> testKickers) {  

      /*System.out.println("inside compare Kickers");
      System.err.println("kickers size: " + kickers.size() + " test size: " + testKickers.size());
      for (int i = 0; i < Math.min(kickers.size(), testKickers.size()); i++) {
         if (kickers.get(i) < testKickers.get(i)) {
            return testKickers;
         }
         else if (kickers.get(i) > testKickers.get(i)) {
            return kickers;
         }
      }*/
      return kickers;
   }
}

