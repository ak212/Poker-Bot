package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   BetPeriod betPeriod;
   Player winner;
   ArrayList<Player> playersTied;
   int smallBlindAmount;
   int bigBlindAmount;
   int dealerButtonPosition;
   int smallBlindPosition;
   int bigBlindPosition;
   int pot;
   int currentBet;
   int totalBet;
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
      this.winner = new Player(-1, 0);
      this.winner.currentHand = Hand.NoHand;
      this.playersTied = new ArrayList<Player>();
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
      int betAmount = 0;

      switch (playerAction) {
      case "b":
         while (betAmount + player.totalBet < this.totalBet || betAmount == 0) {
            betAmount = scan.nextInt();
         }

         if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
            if (player.smallBlind && !player.calledSB) {
               this.currentBet = betAmount - this.smallBlindAmount;
               player.call(this.bigBlindAmount - this.smallBlindAmount);
               player.calledSB = true;
               this.pot += (this.bigBlindAmount - this.smallBlindAmount);
               this.totalBet += this.currentBet;
            }
            else {
               int callAmount = this.totalBet - p.totalBet;
               if (callAmount > 0) {
                  player.call(callAmount);
                  this.pot += callAmount;
               }
               betAmount = betAmount - callAmount;
               this.currentBet = betAmount;
               this.totalBet += betAmount;
            }
         }
         else {
            int callAmount = this.totalBet - p.totalBet;
            if (callAmount > 0) {
               player.call(callAmount);
               this.pot += callAmount;
            }
            betAmount = betAmount - callAmount;
            this.currentBet = betAmount;
            this.totalBet += betAmount;
         }

         player.bet(this.currentBet);
         this.pot += this.currentBet;
         break;
      case "c":
         if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
            if (player.bigBlind && player.totalBet == this.bigBlindAmount) {
               betAmount = this.currentBet - this.bigBlindAmount;
            }
            else if (player.smallBlind && player.totalBet == this.smallBlindAmount) {
               betAmount = this.currentBet - this.smallBlindAmount;
               player.calledSB = true;
            }
            else {
               betAmount = this.currentBet;
            }
         }
         else {
            betAmount = this.currentBet;
         }

         player.call(betAmount);
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
         b.determinePreFlopAction(this.currentBet, this.totalBet);
      } 
      else {
         b.action(this.currentBet);
      }

      switch (b.botTurn.botAction) {
      case CHECKCALL:
         b.call(b.botTurn.betAmount);
         this.pot += b.botTurn.betAmount;
         break;
      case BET:
         int callAmount = this.totalBet - b.totalBet;
         if (callAmount > 0) {
            b.call(callAmount);
            this.pot += callAmount;
         }
         int betAmount = b.botTurn.betAmount - callAmount;
         b.bet(betAmount);
         this.currentBet = betAmount;
         this.totalBet += betAmount;
         this.pot += betAmount;
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

      return playersInHand == playersActed || playersInHand == 1;
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
      this.totalBet = this.currentBet;

      // System.out.println("Current bet: " + this.totalBet);
      while (!betSettled(players)) {
         for (Player player : players) {
            if (player.inHand && !player.playerActed) {
               int curBet = this.totalBet;

               if (this.betPeriod.equals(BetPeriod.PREFLOP)) {
                  if (player instanceof Bot) {
                     players.set(player.preFlopPosition - 1, botInput(players.get(player.preFlopPosition - 1)));
                  } else {
                     players.set(player.preFlopPosition - 1, playerInput(players.get(player.preFlopPosition - 1)));
                  }
                  if (this.totalBet != curBet) {
                     resetPlayersActed(players, player.preFlopPosition);
                  }
               }
               else {
                  if (player instanceof Bot) {
                     players.set(player.position - 1, botInput(players.get(player.position - 1)));
                  } else {
                     players.set(player.position - 1, playerInput(players.get(player.position - 1)));
                  }
                  if (this.currentBet != curBet) {
                     this.resetPlayersActed(players, player.position - 1);
                  }
               }
            }
            if (!player.inHand) {
               this.playersInHand = getPlayersInHand(players);
               if (this.playersInHand == 1) {
                  break;
               }
            }
         }
      }

      return players;
   }

   public ArrayList<Player> updatePosition(ArrayList<Player> players) {
      int i = 1;

      for (Player player : players) {
         if (player.inHand) {
            player.position = i++;
         }
      }

      return players;
   }

   public ArrayList<Player> retrieveEliminatedPlayers(ArrayList<Player> players) {
      ArrayList<Player> playersToBeRemoved = new ArrayList<Player>();

      for (Player player : players) {
         if (player.stack <= 0) {
            playersToBeRemoved.add(player);
         }
      }

      return playersToBeRemoved;
   }

   public ArrayList<Player> removeEliminatedPlayers(ArrayList<Player> players) {
      ArrayList<Player> playersToBeRemoved = retrieveEliminatedPlayers(players);

      if (!playersToBeRemoved.isEmpty()) {
         for (Player player : playersToBeRemoved) {
            System.out.println("Player " + player.id + " eliminated");
            players.remove(player);
         }
      }

      return players;
   }

   public void printHandValues(ArrayList<Player> players) {
      for (Player player : players) {
         if (player instanceof Bot) {
            System.out.println("Bot " + player.id + " has hand " + player.currentHand.toString());
         }
         else {
            System.out.println("Player " + player.id + " has hand " + player.currentHand.toString());
         }
      }
   }

   public void printStackValues(ArrayList<Player> players) {
      for (Player player : players) {
         if (player instanceof Bot) {
            System.out.println("Bot " + player.id + " stack: " + player.stack);
         }
         else {
            System.out.println("Player " + player.id + " stack: " + player.stack);
         }
      }
   }

   public void compareHandStrength(Player p) {
      // use hand strength to compare p against this.winner
      // if tie, keep winner the same, add p to playersTied
      // if not, set winner to p, clear playersTied
      playersTied.add(p);
   }

   public ArrayList<Player> determineWinner(ArrayList<Player> players) {
      if (this.playersTied.isEmpty()) { //no tie
         for (Player player : players) {
            if (player.id == this.winner.id) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + this.winner.id + " wins " + this.pot);
               }
               else {
                  System.out.println("Player " + this.winner.id + " wins " + this.pot);
               }

               player.stack += this.pot;
            }
         }
      }
      else { //tie
         this.winner = HandEvaluator.breakTie(playersTied, communityCards, players.get(0).currentHand);
         
         for (Player player : players) {
            if (player.id == this.winner.id) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + this.winner.id + " wins " + this.pot);
               }
               else {
                  System.out.println("Player " + this.winner.id + " wins " + this.pot);
               }

               player.stack += this.pot;
            }
         }
         
//         playersTied.add(this.winner);
//         int split = this.pot / this.playersTied.size();
//         for (Player p : playersTied) {
//            System.out.println("Player " + p.id + " wins " + split);
//            for (Player player : players) {
//               if (player.id == p.id) {
//                  player.stack += split;
//               }
//            }
//         }
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

   public ArrayList<Player> completeRound(ArrayList<Player> players) {
      this.printCommunityCards();
      players = this.updatePosition(players);
      players = this.evaluateHandStrength(players, this.communityCards);
      this.printHandValues(players);
      players = this.betPeriod(players);

      return players;
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
      this.totalBet = 0;
      this.sidePots.clear();
      this.deckOfCards = new DeckOfCards();
      this.playersTied.clear();
      this.winner = new Player (-1, 0);
      this.winner.currentHand = Hand.NoHand;
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      for (Card card : this.communityCards) {
         System.out.print(card.shorten());
      }
      System.out.println();
   }
}
