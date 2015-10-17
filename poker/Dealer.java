package poker;

import java.util.ArrayList;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   int smallBlindAmount;
   int bigBlindAmount;
   int dealerButtonPosition;
   int smallBlindPosition;
   int bigBlindPosition;
   int pot;
   int currentBet;
   ArrayList<Integer> sidePots;
   int playersInHand;

   public Dealer() {
      dealerButtonPosition = 0;
      smallBlindPosition = 1;
      bigBlindPosition = 2;
      smallBlindAmount = 25;
      bigBlindAmount = 50;
      pot = 0;
      currentBet = 0;
      communityCards = new ArrayList<Card>();
      burnCards = new ArrayList<Card>();
      deckOfCards = new DeckOfCards();
      sidePots = new ArrayList<Integer>();
   }

   public Card drawCard() {
      return deckOfCards.deck.remove(0);
   }

   public int getPlayersInHand(ArrayList<Player> players) {
      int count = 0;

      for (Player player : players) {
         if (player.stack > 0) {
            count++;
         }
      }

      return count;
   }

   public void flop() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
      communityCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void turn() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void river() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void newHand() {
      dealerButtonPosition++;
      smallBlindPosition++;
      bigBlindPosition++;
      playersInHand = 0;
      burnCards.clear();
      communityCards.clear();
      pot = 0;
      currentBet = 0;
      sidePots.clear();
      deckOfCards = new DeckOfCards();
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      for (Card card : communityCards) {
         System.out.print(card.shorten());
      }
      System.out.println();
   }

}
