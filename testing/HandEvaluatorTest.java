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

public class HandEvaluatorTest {

   @Test
   public void testHighCard() {
      ArrayList<Card> highCardBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.TWO), new Card(Suit.CLUBS, Rank.THREE));
      
      assertEquals(Hand.HighCard.getValue(), HandEvaluator.evaluateForHand(highCardBoard, holeCards).getValue());
   }
   
   @Test
   public void testOnePair() {
      ArrayList<Card> onePairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.THREE));
      
      assertEquals(Hand.OnePair.getValue(), HandEvaluator.evaluateForHand(onePairBoard, holeCards).getValue());
   }
   
   @Test
   public void testTwoPair() {
      ArrayList<Card> twoPairBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
      
      assertEquals(Hand.TwoPair.getValue(), HandEvaluator.evaluateForHand(twoPairBoard, holeCards).getValue());
   }
   
   @Test
   public void testThreeOfAKind() {
      ArrayList<Card> threeOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      
      assertEquals(Hand.ThreeOfAKind.getValue(), HandEvaluator.evaluateForHand(threeOfAKindBoard, holeCards).getValue());
   }
   
   @Test
   public void testStraight() {
      ArrayList<Card> straightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.JACK), new Card(Suit.CLUBS, Rank.TEN));
      
      assertEquals(Hand.Straight.getValue(), HandEvaluator.evaluateForHand(straightBoard, holeCards).getValue());
   }
   
   @Test
   public void testWheelStraight() {
      ArrayList<Card> wheelStraightBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.TWO), new Card(Suit.SPADES, Rank.THREE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.FOUR), new Card(Suit.CLUBS, Rank.FIVE));
      
      assertEquals(Hand.Straight.getValue(), HandEvaluator.evaluateForHand(wheelStraightBoard, holeCards).getValue());
   }
   
   @Test
   public void testFlush() {
      ArrayList<Card> flushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING));
      
      assertEquals(Hand.Flush.getValue(), HandEvaluator.evaluateForHand(flushBoard, holeCards).getValue());
   }
   
   @Test
   public void testFullHouse() {
      ArrayList<Card> fullHouseBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.HEARTS, Rank.ACE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.CLUBS, Rank.KING));
      
      assertEquals(Hand.FullHouse.getValue(), HandEvaluator.evaluateForHand(fullHouseBoard, holeCards).getValue());
   }
   
   @Test
   public void testFourOfAKind() {
      ArrayList<Card> fourOfAKindBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.DIAMONDS, Rank.ACE)));
      HoleCards holeCards = new HoleCards(new Card(Suit.CLUBS, Rank.ACE), new Card(Suit.HEARTS, Rank.ACE));
      
      assertEquals(Hand.FourOfAKind.getValue(), HandEvaluator.evaluateForHand(fourOfAKindBoard, holeCards).getValue());
   }
   
   @Test
   public void testStraightFlush() {
      ArrayList<Card> straightFlushBoard = new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.KING), new Card(Suit.SPADES, Rank.QUEEN)));
      HoleCards holeCards = new HoleCards(new Card(Suit.SPADES, Rank.JACK), new Card(Suit.SPADES, Rank.TEN));
      
      assertEquals(Hand.StraightFlush.getValue(), HandEvaluator.evaluateForHand(straightFlushBoard, holeCards).getValue());
   }

}
