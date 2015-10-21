package poker;

import java.util.ArrayList;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   BetPeriod betPeriod;
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

   public int getPlayersToBeDealt(ArrayList<Player> players) {
      int count = 0;

      for (Player player : players) {
         if (player.stack > 0) {
            count++;
         }
      }

      return count;
   }

   public int getPlayersInHand(ArrayList<Player> players) {
      int count = 0;

      for (Player player : players) {
         if (player.inHand) {
            count++;
         }
      }

      return count;
   }

   public boolean betSettled(ArrayList<Player> players) {
      int playersInHand = getPlayersInHand(players);
      int playersActed = 0;

      for (Player player : players) {
         if (player.inHand && player.playerActed) {
            playersActed++;
         }
      }

      return playersInHand == playersActed;
   }

   public ArrayList<Player> resetPlayersActed(ArrayList<Player> players, int exception) {
      for (Player player : players) {
         if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
            if (player.preFlopPosition != exception) {
               player.playerActed = false;
            }
         }
         else {
            if (player.position - 1 != exception) {
               player.playerActed = false;
            }
         }
      }

      return players;
   }

   public ArrayList<Player> dealHoleCards(ArrayList<Player> players) {
      Card[] cardsDealt = new Card[players.size() * 2 + 1];
      for (int i = 1; i < players.size() * 2 + 1; i++) {
         try {
            if (deckOfCards.deck.size() == 0) {
               throw new Exception("No cards in deck");
            }
            cardsDealt[i] = drawCard();
         } catch (Exception e) {
            System.err.println(e.getMessage());
         }
      }

      for (Player player : players) {
         player.holeCards = new HoleCards(cardsDealt[player.position], cardsDealt[player.position + players.size()]);
         player.inHand = true;
      }

      return players;
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
