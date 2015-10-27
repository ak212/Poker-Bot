package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   BetPeriod betPeriod;
   //HandStrength strongestHand;
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
      this.dealerButtonPosition = 0;
      this.smallBlindPosition = 1;
      this.bigBlindPosition = 2;
      this.smallBlindAmount = 25;
      this.bigBlindAmount = 50;
      this.pot = 0;
      this.currentBet = 0;
      //strongestHand = new HandStrength();
      this.communityCards = new ArrayList<Card>();
      this.burnCards = new ArrayList<Card>();
      this.deckOfCards = new DeckOfCards();
      this.sidePots = new ArrayList<Integer>();
   }

   public Card drawCard() {
      return this.deckOfCards.deck.remove(0);
   }

   public Player playerInput(Player p) {
      Player player = p;
      Scanner scan = new Scanner(System.in);
      String playerAction = scan.next();

      switch (playerAction) {
      case "b":
         this.currentBet = scan.nextInt();
         player.bet(this.currentBet);
         this.pot += this.currentBet;
         break;
      case "c":
         int betAmount = 0;
         if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
            if (player.bigBlind) {
               betAmount = this.currentBet - this.bigBlindAmount;
            } else if (player.smallBlind) {
               betAmount = this.currentBet - this.smallBlindAmount;
            } else {
               betAmount = this.currentBet;
            }
         } else {
            betAmount = this.currentBet;
         }

         player.bet(betAmount);
         this.pot += betAmount;
         break;
      case "f":
         player.inHand = false;
         this.playersInHand--;
         System.out.println("Player " + player.id + " folds");
         break;
      default:
         break;
      }

      return player;
   }

   public Player botInput(Player p) {
      Bot b = (Bot) p;

      if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
         if (b.bigBlind) {
            b.determinePreFlopAction(this.currentBet - this.bigBlindAmount, this.bigBlindAmount);
         } else if (b.smallBlind) {
            b.determinePreFlopAction(this.currentBet - this.smallBlindAmount, this.bigBlindAmount);
         } else {
            b.determinePreFlopAction(this.currentBet, this.bigBlindAmount);
         }
      } else {
         b.action(this.currentBet);
      }

      switch (b.botTurn.botAction) {
      case CHECKCALL:
         b.bet(b.botTurn.betAmount);
         this.pot += b.botTurn.betAmount;
         break;
      case BET:
         b.bet(b.botTurn.betAmount);
         this.currentBet = b.botTurn.betAmount;
         this.pot += b.botTurn.betAmount;
         break;
      case FOLD:
         b.inHand = false;
         this.playersInHand--;
         System.out.println("Bot " + b.id + " folds");
      }

      return b;
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

   public ArrayList<Player> determinePositions(ArrayList<Player> players) {
      this.playersInHand = this.getPlayersToBeDealt(players);
      Collections.sort(players, new IdComparator());

      players.get(this.dealerButtonPosition % this.playersInHand).dealerButton = true;
      if (this.playersInHand == 2) {
         players.get(this.dealerButtonPosition % this.playersInHand).smallBlind = true;
         players.get(this.dealerButtonPosition % this.playersInHand).position = 2;
         players.get(this.dealerButtonPosition % this.playersInHand).preFlopPosition = 1;
         players.get((this.dealerButtonPosition + 1) % this.playersInHand).bigBlind = true;
         players.get((this.dealerButtonPosition + 1) % this.playersInHand).position = 1;
         players.get((this.dealerButtonPosition + 1) % this.playersInHand).preFlopPosition = 2;
      }
      else {
         players.get(this.smallBlindPosition % this.playersInHand).smallBlind = true;
         players.get(this.smallBlindPosition % this.playersInHand).position = 1;
         players.get(this.bigBlindPosition % this.playersInHand).bigBlind = true;
         players.get(this.bigBlindPosition % this.playersInHand).position = 2;

         for (int i = 2; i < this.playersInHand; i++) {
            players.get((this.smallBlindPosition + i) % this.playersInHand).position = i + 1;
         }
         for (int i = 2; i < this.playersInHand + 2; i++) {
            players.get((this.smallBlindPosition + i) % this.playersInHand).preFlopPosition = i - 1;
         }
      }

      return players;
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
            if (this.deckOfCards.deck.size() == 0) {
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

   public ArrayList<Player> evaluateHandStrength(ArrayList<Player> players, ArrayList<Card> board) {
      for (Player player : players) {
         player.currentHand = HandEvaluator.evaluateForHand(new ArrayList<Card>(board), player.holeCards);
      }

      return players;
   }

   public ArrayList<Player> betPeriod(ArrayList<Player> players) {
      while (!betSettled(players)) {
         for (Player player : players) {
            if (player.inHand && !player.playerActed) {
               int curBet = this.currentBet;

               if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
                  if (player.getClass() == Bot.class) {
                     players.set(player.preFlopPosition - 1, botInput(players.get(player.preFlopPosition - 1)));
                  } else {
                     players.set(player.preFlopPosition - 1, playerInput(players.get(player.preFlopPosition - 1)));
                  }
                  if (this.currentBet != curBet) {
                     resetPlayersActed(players, player.preFlopPosition);
                  }
               }
               else {
                  if (player.getClass() == Bot.class) {
                     players.set(player.position - 1, botInput(players.get(player.position - 1)));
                  } else {
                     players.set(player.position - 1, playerInput(players.get(player.position - 1)));
                  }
                  if (this.currentBet != curBet) {
                     this.resetPlayersActed(players, player.position - 1);
                  }
               }
            }
         }
      }

      return players;
   }

   public void flop() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
      this.communityCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   public void turn() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   public void river() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   public void newHand() {
      this.dealerButtonPosition++;
      this.smallBlindPosition++;
      this.bigBlindPosition++;
      this.playersInHand = 0;
      this.burnCards.clear();
      this.communityCards.clear();
      this.pot = 0;
      this.currentBet = 0;
      this.sidePots.clear();
      this.deckOfCards = new DeckOfCards();
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      for (Card card : this.communityCards) {
         System.out.print(card.shorten());
      }
      System.out.println();
   }
}
