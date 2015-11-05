package testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.cards.Rank;
import poker.model.cards.Suit;
import poker.model.hand.Hand;
import poker.model.hand.HandEvaluator;
import poker.model.hand.HandStrength;
import poker.model.player.Player;

public class HandEvaluatorTest {

   @Test
   public void testHighCard() {
      ArrayList<Card> highCardBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.TWO), new Card(Suit.CLUBS, Rank.THREE));
      
      assertEquals(Hand.HighCard.getValue(), HandEvaluator.evaluateForHand(highCardBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testOnePair() {
      ArrayList<Card> onePairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.THREE));
      
      assertEquals(Hand.OnePair.getValue(), HandEvaluator.evaluateForHand(onePairBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testTwoPair() {
      ArrayList<Card> twoPairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
      
      assertEquals(Hand.TwoPair.getValue(), HandEvaluator.evaluateForHand(twoPairBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testThreeOfAKind() {
      ArrayList<Card> threeOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      
      assertEquals(Hand.ThreeOfAKind.getValue(), HandEvaluator.evaluateForHand(threeOfAKindBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testStraight() {
      ArrayList<Card> straightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.JACK), new Card(Suit.CLUBS, Rank.TEN));
      
      assertEquals(Hand.Straight.getValue(), HandEvaluator.evaluateForHand(straightBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testWheelStraight() {
      ArrayList<Card> wheelStraightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.TWO), new Card(Suit.SPADES, Rank.THREE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.FOUR), new Card(Suit.CLUBS, Rank.FIVE));
      
      assertEquals(Hand.Straight.getValue(), HandEvaluator.evaluateForHand(wheelStraightBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testFlush() {
      ArrayList<Card> flushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING));
      
      assertEquals(Hand.Flush.getValue(), HandEvaluator.evaluateForHand(flushBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testFullHouse() {
      ArrayList<Card> fullHouseBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.HEARTS, Rank.ACE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
      
      assertEquals(Hand.FullHouse.getValue(), HandEvaluator.evaluateForHand(fullHouseBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testFourOfAKind() {
      ArrayList<Card> fourOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.DIAMONDS, Rank.ACE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      
      assertEquals(Hand.FourOfAKind.getValue(), HandEvaluator.evaluateForHand(fourOfAKindBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testStraightFlush() {
      ArrayList<Card> straightFlushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.SPADES, Rank.JACK), new Card(Suit.SPADES, Rank.TEN));
      
      assertEquals(Hand.StraightFlush.getValue(), HandEvaluator.evaluateForHand(straightFlushBoard, holeCards).hand.getValue());
   }
   
   @Test
   public void testKickers() {
      
      // Test High Card
      ArrayList<Card> highCardBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.THREE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.TWO), new Card(Suit.CLUBS, Rank.FOUR));
      assertEquals(HandEvaluator.evaluateForHand(highCardBoard, holeCards).kickers, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 9, 4)));
      
      // Test One Pair
      ArrayList<Card> onePairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCards1 = new HoleCards(new Card(Suit.CLUBS, Rank.KING), new Card(Suit.CLUBS, Rank.THREE));
      assertEquals(HandEvaluator.evaluateForHand(onePairBoard, holeCards1).kickers, new ArrayList<Integer>(Arrays.asList(13, 14, 12, 9)));
      
      // Test Two Pair
      ArrayList<Card> twoPairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCards2 = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.QUEEN));
      assertEquals(HandEvaluator.evaluateForHand(twoPairBoard, holeCards2).kickers, new ArrayList<Integer>(Arrays.asList(14, 12, 13)));
      
      // Test Three Of A Kind
      ArrayList<Card> threeOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCards3 = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      assertEquals(HandEvaluator.evaluateForHand(threeOfAKindBoard, holeCards3).kickers, new ArrayList<Integer>(Arrays.asList(14, 13, 12)));
      
      // Test Straight
      ArrayList<Card> straightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCardsS = new HoleCards(new Card(Suit.CLUBS, Rank.JACK), new Card(Suit.CLUBS, Rank.TEN));
      assertEquals(HandEvaluator.evaluateForHand(straightBoard, holeCardsS).kickers, new ArrayList<Integer>(Arrays.asList(14)));
      
      // Test Wheel Straight
      ArrayList<Card> wheelStraightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.TWO), new Card(Suit.SPADES, Rank.THREE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCardsWS = new HoleCards(new Card(Suit.CLUBS, Rank.FOUR), new Card(Suit.CLUBS, Rank.FIVE));
      assertEquals(HandEvaluator.evaluateForHand(wheelStraightBoard, holeCardsWS).kickers, new ArrayList<Integer>(Arrays.asList(5)));
      
      // Test Flush
      ArrayList<Card> flushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCardsF = new HoleCards(new Card(Suit.SPADES, Rank.TWO), new Card(Suit.SPADES, Rank.THREE));
      assertEquals(HandEvaluator.evaluateForHand(flushBoard, holeCardsF).kickers, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 3, 2)));
      
      // Test Full House
      ArrayList<Card> fullHouseBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.HEARTS, Rank.ACE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCardsFH = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
      assertEquals(HandEvaluator.evaluateForHand(fullHouseBoard, holeCardsFH).kickers, new ArrayList<Integer>(Arrays.asList(14, 13)));
      
      // Test Four Of A Kind
      ArrayList<Card> fourOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.DIAMONDS, Rank.ACE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCards4 = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      assertEquals(HandEvaluator.evaluateForHand(fourOfAKindBoard, holeCards4).kickers, new ArrayList<Integer>(Arrays.asList(14, 13)));
      
      // Test Straight Flush
      ArrayList<Card> straightFlushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.FOUR)));
      HoleCards holeCardsSF = new HoleCards(new Card(Suit.SPADES, Rank.JACK), new Card(Suit.SPADES, Rank.TEN));
      assertEquals(HandEvaluator.evaluateForHand(straightFlushBoard, holeCardsSF).kickers, new ArrayList<Integer>(Arrays.asList(14)));
   }
   
   @Test
   public void testTieBreakers() {
      Player kicker1 = new Player(0, 5000), kicker2 = new Player(1, 5000), kicker3 = new Player(2, 5000);
      
      // Test one kickers
      kicker1.setCurrentHand(new HandStrength(Hand.Straight, new ArrayList<Integer>(Arrays.asList(14))));
      kicker2.setCurrentHand(new HandStrength(Hand.Straight, new ArrayList<Integer>(Arrays.asList(14))));
      kicker3.setCurrentHand(new HandStrength(Hand.Straight, new ArrayList<Integer>(Arrays.asList(6))));
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker2))).size(), 2);
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker3))).get(0).getId(), 0);
      
      // Test two kickers
      kicker1.setCurrentHand(new HandStrength(Hand.FullHouse, new ArrayList<Integer>(Arrays.asList(14, 13))));
      kicker2.setCurrentHand(new HandStrength(Hand.FullHouse, new ArrayList<Integer>(Arrays.asList(14, 13))));
      kicker3.setCurrentHand(new HandStrength(Hand.FullHouse, new ArrayList<Integer>(Arrays.asList(14, 6))));
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker2))).size(), 2);
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker3))).get(0).getId(), 0);
      
      // Test three kickers
      kicker1.setCurrentHand(new HandStrength(Hand.ThreeOfAKind, new ArrayList<Integer>(Arrays.asList(14, 13, 12))));
      kicker2.setCurrentHand(new HandStrength(Hand.ThreeOfAKind, new ArrayList<Integer>(Arrays.asList(14, 13, 12))));
      kicker3.setCurrentHand(new HandStrength(Hand.ThreeOfAKind, new ArrayList<Integer>(Arrays.asList(14, 13, 11))));
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker2))).size(), 2);
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker3))).get(0).getId(), 0);
      
      // Test four kickers
      kicker1.setCurrentHand(new HandStrength(Hand.OnePair, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11))));
      kicker2.setCurrentHand(new HandStrength(Hand.OnePair, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11))));
      kicker3.setCurrentHand(new HandStrength(Hand.OnePair, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 10))));
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker2))).size(), 2);
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker3))).get(0).getId(), 0);
      
      // Test five kickers
      kicker1.setCurrentHand(new HandStrength(Hand.HighCard, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11, 9))));
      kicker2.setCurrentHand(new HandStrength(Hand.HighCard, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11, 9))));
      kicker3.setCurrentHand(new HandStrength(Hand.HighCard, new ArrayList<Integer>(Arrays.asList(14, 13, 12, 11, 8))));
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker2))).size(), 2);
      assertEquals(HandEvaluator.breakTie(new ArrayList<Player>(Arrays.asList(kicker1, kicker3))).get(0).getId(), 0);
   }
}
