package poker.model.game;

import java.util.ArrayList;
import java.util.Collections;

import poker.Main;
import poker.model.cards.Card;
import poker.model.cards.DeckOfCards;
import poker.model.cards.HoleCards;
import poker.model.hand.Hand;
import poker.model.hand.HandEvaluator;
import poker.model.hand.HandStrength;
import poker.model.player.Bot;
import poker.model.player.Player;
import poker.model.player.Turn;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   private BetPeriod betPeriod;
   private Player winner;
   private ArrayList<Player> playersTied;
   private int smallBlindAmount;
   private int bigBlindAmount;
   int dealerButtonPosition;
   int smallBlindPosition;
   int bigBlindPosition;
   private int pot;
   private int currentBet;
   private int totalBet;
   ArrayList<Integer> sidePots;
   private int playersInHand;
   public Main mainApp;

   public int minBetAmount;
   public int halfPotBetAmount;
   public int potBetAmount;
   public int maxBetAmount;
   public int minAmount;

   public Dealer() {
      this.dealerButtonPosition = 0;
      this.smallBlindPosition = 1;
      this.bigBlindPosition = 2;
      this.setSmallBlindAmount(25);
      this.setBigBlindAmount(50);
      this.setPot(0);
      this.setCurrentBet(0);
      this.setWinner(new Player(-1, 0));
      this.getWinner().setCurrentHand(new HandStrength(Hand.NoHand, new ArrayList<Integer>()));
      this.setPlayersTied(new ArrayList<Player>());
      this.communityCards = new ArrayList<Card>();
      this.burnCards = new ArrayList<Card>();
      this.deckOfCards = new DeckOfCards();
      this.sidePots = new ArrayList<Integer>();
   }

   public Card drawCard() {
      return this.deckOfCards.getDeck().remove(0);
   }

   public Player playerInput(Player p) {
      Player player = p;
      int betAmount = 0;
      Turn turn = mainApp.getPlayerInput();
      mainApp.disablePlayerInput();

      switch (turn.getAction()) {
      case BET:
         betAmount = turn.getBetAmount();
         if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
            if (player.isSmallBlind() && !player.isCalledSB()) {
               this.setCurrentBet(betAmount - this.getSmallBlindAmount());
               player.call(this.getBigBlindAmount() - this.getSmallBlindAmount());
               player.setCalledSB(true);
               this.setPot(this.getPot() + betAmount);
               this.setTotalBet(this.getTotalBet() + this.getCurrentBet());
            }
            else {
               int callAmount = this.getTotalBet() - p.getTotalBet();
               if (callAmount > 0) {
                  player.call(callAmount);
                  this.setPot(this.getPot() + callAmount);
               }
               betAmount = betAmount - callAmount;
               this.setCurrentBet(betAmount);
               this.setTotalBet(this.getTotalBet() + betAmount);
            }
         }
         else {
            int callAmount = this.getTotalBet() - p.getTotalBet();
            if (callAmount > 0) {
               player.call(callAmount);
               this.setPot(this.getPot() + callAmount);
            }
            betAmount = betAmount - callAmount;
            this.setCurrentBet(betAmount);
            this.setTotalBet(this.getTotalBet() + betAmount);
         }

         player.bet(this.getCurrentBet());
         this.setPot(this.getPot() + this.getCurrentBet());

         mainApp.updateBetAmountZero(Integer.toString(player.getTotalBet()));

         break;
      case CHECKCALL:
         player.call(turn.getBetAmount());
         this.setPot(this.getPot() + turn.getBetAmount());

         mainApp.updateBetAmountZero(Integer.toString(player.getTotalBet()));

         break;
      case FOLD:
         player.setInHand(false);
         this.setPlayersInHand(this.getPlayersInHand() - 1);
         System.out.println("Player " + player.getId() + " folds");
         mainApp.updateConsole("Player " + player.getId() + " folds");

         player.stats.folds++;
         switch (this.betPeriod) {
         case PREFLOP:
            player.stats.preFlopFolds++;
            break;
         case FLOP:
            player.stats.flopFolds++;
            break;
         case TURN:
            player.stats.turnFolds++;
            break;
         case RIVER:
            player.stats.riverFolds++;
            break;
         case EVAL:
            break;
         default:
            break;
         }
         break;
      default:
         break;
      }

      return player;
   }

   public Player botInput(Player p, ArrayList<Player> players) {
      Bot b = (Bot) p;

      b.getOpponentStackSizes(players);

      if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
         b.determinePreFlopAction(this.getCurrentBet(), this.getTotalBet(), this.getBigBlindAmount());
      } 
      else {
         b.determinePostFlopAction(this.getCurrentBet(), this.getTotalBet(), this);
      }

      switch (b.getBotTurn().getAction()) {
      case CHECKCALL:
         b.call(b.getBotTurn().getBetAmount());
         this.setPot(this.getPot() + b.getBotTurn().getBetAmount());

         if (b.getTotalBet() == 0) {
            mainApp.updateBetAmountOne(null);
         }
         else {
            mainApp.updateBetAmountOne(Integer.toString(b.getTotalBet()));
         }

         break;
      case BET:
         System.out.println("Total Bet: " + this.getTotalBet() + ", Bot bet: " + b.getTotalBet());
         int callAmount = this.getTotalBet() - b.getTotalBet();
         if (callAmount > 0) {
            b.call(callAmount);
            this.setPot(this.getPot() + callAmount);
         }
         int betAmount = b.getBotTurn().getBetAmount() - callAmount;
         b.bet(betAmount);
         this.setCurrentBet(betAmount);
         this.setTotalBet(this.getTotalBet() + betAmount);
         this.setPot(this.getPot() + betAmount);

         mainApp.updateBetAmountOne(Integer.toString(b.getTotalBet()));

         break;
      case FOLD:
         b.setInHand(false);
         this.setPlayersInHand(this.getPlayersInHand() - 1);
         System.out.println("Bot " + b.getId() + " folds");

         mainApp.updateConsole("Bot " + b.getId() + " folds");
         mainApp.updateBetAmountOne(null);

         b.stats.folds++;
         switch (this.betPeriod) {
         case PREFLOP:
            b.stats.preFlopFolds++;
            break;
         case FLOP:
            b.stats.flopFolds++;
            break;
         case TURN:
            b.stats.turnFolds++;
            break;
         case RIVER:
            b.stats.riverFolds++;
            break;
         case EVAL:
            break;
         default:
            break;
         }
      }

      return b;
   }



   public ArrayList<Player> determinePositions(ArrayList<Player> players) {
      this.setPlayersInHand(DealerUtils.getPlayersToBeDealt(players));
      Collections.sort(players, new IdComparator());

      players.get(this.dealerButtonPosition % this.getPlayersInHand()).setDealerButton(true);
      if (this.getPlayersInHand() == 2) {
         players.get(this.dealerButtonPosition % this.getPlayersInHand()).setSmallBlind(true);
         players.get(this.dealerButtonPosition % this.getPlayersInHand()).setPosition(2);
         players.get(this.dealerButtonPosition % this.getPlayersInHand()).setPreFlopPosition(1);
         players.get((this.dealerButtonPosition + 1) % this.getPlayersInHand()).setBigBlind(true);
         players.get((this.dealerButtonPosition + 1) % this.getPlayersInHand()).setPosition(1);
         players.get((this.dealerButtonPosition + 1) % this.getPlayersInHand()).setPreFlopPosition(2);
      }
      else {
         players.get(this.smallBlindPosition % this.getPlayersInHand()).setSmallBlind(true);
         players.get(this.smallBlindPosition % this.getPlayersInHand()).setPosition(1);
         players.get(this.bigBlindPosition % this.getPlayersInHand()).setBigBlind(true);
         players.get(this.bigBlindPosition % this.getPlayersInHand()).setPosition(2);

         for (int i = 2; i < this.getPlayersInHand(); i++) {
            players.get((this.smallBlindPosition + i) % this.getPlayersInHand()).setPosition(i + 1);
         }
         for (int i = 2; i < this.getPlayersInHand() + 2; i++) {
            players.get((this.smallBlindPosition + i) % this.getPlayersInHand()).setPreFlopPosition(i - 1);
         }
      }

      return players;
   }

   public ArrayList<Player> resetPlayersActed(ArrayList<Player> players, int exception) {
      for (Player player : players) {
         if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
            if (player.getPreFlopPosition() != exception) {
               player.setPlayerActed(false);
            }
         }
         else {
            if (player.getPosition() - 1 != exception) {
               player.setPlayerActed(false);
            }
         }
      }

      return players;
   }

   public ArrayList<Player> dealHoleCards(ArrayList<Player> players) {
      Card[] cardsDealt = new Card[players.size() * 2 + 1];
      for (int i = 1; i < players.size() * 2 + 1; i++) {
         try {
            if (this.deckOfCards.getDeck().size() == 0) {
               throw new Exception("No cards in deck");
            }
            cardsDealt[i] = drawCard();
         } catch (Exception e) {
            System.err.println(e.getMessage());
         }
      }

      for (Player player : players) {
         player.setHoleCards(new HoleCards(cardsDealt[player.getPosition()], cardsDealt[player.getPosition() + players.size()]));
         player.setInHand(true);
         player.stats.hands++;
      }

      return players;
   }

   public ArrayList<Player> betPeriod(ArrayList<Player> players) {
      this.setTotalBet(this.getCurrentBet());


      while (!DealerUtils.betSettled(players)) {
         for (Player player : players) {
            if (player.isInHand() && !player.isPlayerActed()) {
               int curBet = this.getTotalBet();
               if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
                  if (player instanceof Bot) {
                     try {
                        Thread.sleep(2000);
                     }
                     catch (InterruptedException e) {
                        e.printStackTrace();
                     }
                     players.set(player.getPreFlopPosition() - 1, botInput(players.get(player.getPreFlopPosition() - 1), players));
                     mainApp.updateStackOne(Integer.toString(player.getStack()));
                  } 
                  else {
                     getBetAmounts(player);

                     players.set(player.getPreFlopPosition() - 1, playerInput(players.get(player.getPreFlopPosition() - 1)));
                     mainApp.updateStackZero(Integer.toString(player.getStack()));
                  }
                  if (this.getTotalBet() != curBet) {
                     resetPlayersActed(players, player.getPreFlopPosition());
                  }
               }
               else {
                  if (player instanceof Bot) {
                     try {
                        Thread.sleep(2000);
                     }
                     catch (InterruptedException e) {
                        e.printStackTrace();
                     }
                     players.set(player.getPosition() - 1, botInput(players.get(player.getPosition() - 1), players));
                     mainApp.updateStackOne(Integer.toString(player.getStack()));
                  }
                  else {
                     getBetAmounts(player);

                     players.set(player.getPosition() - 1, playerInput(players.get(player.getPosition() - 1)));
                     mainApp.updateStackZero(Integer.toString(player.getStack()));
                  }
                  if (this.getCurrentBet() != curBet) {
                     this.resetPlayersActed(players, player.getPosition() - 1);
                  }
               }
            }
            if (!player.isInHand()) {
               this.setPlayersInHand(DealerUtils.getPlayersInHand(players));
               if (this.getPlayersInHand() == 1) {
                  break;
               }
            }

            mainApp.updatePot(Integer.toString(this.pot));
         }
      }

      try {
         Thread.sleep(2000);
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }

      return players;
   }

   public void compareHandStrength(Player p) {
      // use hand strength to compare p against this.winner
      // if tie, keep winner the same, add p to playersTied
      // if not, set winner to p, clear playersTied
      getPlayersTied().add(p);
   }

   public ArrayList<Player> determineWinner(ArrayList<Player> players) {
      if (this.getPlayersTied().isEmpty()) { //no tie
         for (Player player : players) {
            if (player.getId() == this.getWinner().getId()) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + this.getWinner().getId() + " wins " + this.getPot() + " with "
                        + this.winner.getCurrentHand().hand.toString());
                  mainApp.updateConsole("Bot " + this.getWinner().getId() + " wins " + this.getPot() + " with "
                        + this.winner.getCurrentHand().hand.toString());
               }
               else {
                  System.out.println("Player " + this.getWinner().getId() + " wins " + this.getPot() + " with "
                        + this.winner.getCurrentHand().hand.toString());
                  mainApp.updateConsole("Player " + this.getWinner().getId() + " wins " + this.getPot() + " with "
                        + this.winner.getCurrentHand().hand.toString());
               }

               player.setStack(player.getStack() + this.getPot());
               player.stats.wins++;
               player.stats.setWinnings(this.getPot());
               if (this.getPot() > player.stats.biggestWin) {
                  player.stats.biggestWin = this.getPot();
               }
            }
            else {
               player.stats.losses--;
            }

            if (player instanceof Bot) {
               mainApp.updateStackOne(Integer.toString(player.getStack()));
            }
            else {
               mainApp.updateStackZero(Integer.toString(player.getStack()));
            }
         }
      }
      else { //tie
         this.playersTied.add(this.winner);

         this.playersTied = HandEvaluator.breakTie(this.playersTied);
         int split = this.pot / this.playersTied.size();
         for (Player p : playersTied) {
            if (p instanceof Bot) {
               System.out
                     .println("Bot " + p.getId() + " wins " + split + " with " + p.getCurrentHand().hand.toString());
               mainApp.updateConsole(
                     "Bot " + p.getId() + " wins " + split + " with " + p.getCurrentHand().hand.toString());
            }
            else {
               System.out
                     .println("Player " + p.getId() + " wins " + split + " with " + p.getCurrentHand().hand.toString());
               mainApp.updateConsole(
                     "Player " + p.getId() + " wins " + split + " with " + p.getCurrentHand().hand.toString());
            }
            for (Player player : players) {
               if (player.getId() == p.getId()) {
                  player.setStack(player.getStack() + split);

                  player.stats.wins++;
                  player.stats.setWinnings(split);
                  if (split > player.stats.biggestWin) {
                     player.stats.biggestWin = split;
                  }
               }
               else {
                  player.stats.losses++;
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

   public ArrayList<Player> completeRound(ArrayList<Player> players) {
      this.printCommunityCards();
      players = DealerUtils.updatePosition(players);
      players = DealerUtils.evaluateHandStrength(players, this.communityCards);
      DealerUtils.printHandValues(players);
      players = this.betPeriod(players);

      return players;
   }

   public int getMinAmount(Player player) {
      int betAmount = 0;

      if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
         if (player.isBigBlind() && player.getTotalBet() == this.getBigBlindAmount()) {
            betAmount = this.getCurrentBet() - this.getBigBlindAmount();
         }
         else if (player.isSmallBlind() && player.getTotalBet() == this.getSmallBlindAmount()) {
            betAmount = this.getCurrentBet() - this.getSmallBlindAmount();
         }
         else {
            betAmount = this.getCurrentBet();
         }
      }
      else {
         betAmount = this.getCurrentBet();
      }

      return betAmount;
   }

   public int getMinBet(Player player) {
      int betAmount = 0;

      if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
         if (player.isBigBlind() && player.getTotalBet() == this.getBigBlindAmount()) {
            if (this.getCurrentBet() == this.getBigBlindAmount()) {
               betAmount = this.getBigBlindAmount();
            }
            else {
               betAmount = this.getCurrentBet() * 2;
            }
         }
         else if (player.isSmallBlind() && player.getTotalBet() == this.getSmallBlindAmount()) {
            betAmount = this.getCurrentBet() * 2 - this.getSmallBlindAmount();
         }
         else {
            betAmount = this.getCurrentBet();
         }
      }
      else if (this.getCurrentBet() == 0) {
         betAmount = this.getBigBlindAmount();
      }
      else {
         betAmount = this.getCurrentBet() * 2;
      }

      return betAmount;
   }

   public int getHalfPotBet() {
      int amount = (int) Math.ceil(this.getPot() / 2);

      if (amount < minBetAmount) {
         return minBetAmount;
      }
      else {
         return amount;
      }
   }

   public int getPotBet() {
      return this.getPot();
   }

   public int getMaxBet(Player player) {
      return player.getStack();
   }

   public void getBetAmounts(Player player) {
      minAmount = getMinAmount(player);
      minBetAmount = getMinBet(player);
      halfPotBetAmount = getHalfPotBet();
      potBetAmount = getPotBet();
      maxBetAmount = getMaxBet(player);
   }

   public void newHand() {
      this.dealerButtonPosition++;
      this.smallBlindPosition++;
      this.bigBlindPosition++;
      this.setPlayersInHand(0);
      this.burnCards.clear();
      this.communityCards.clear();
      this.setPot(0);
      this.setCurrentBet(0);
      this.setTotalBet(0);
      this.sidePots.clear();
      this.deckOfCards = new DeckOfCards();
      this.getPlayersTied().clear();
      this.setWinner(new Player (-1, 0));
      this.getWinner().setCurrentHand(new HandStrength(Hand.NoHand, new ArrayList<Integer>()));
   }

   public ArrayList<Card> getCommunityCards() {
      return this.communityCards;
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      // mainApp.updateConsole("On the board:");
      String cards = "";

      for (Card card : this.communityCards) {
         cards += card.shorten();
         System.out.print(card.shorten());
      }
      // mainApp.updateConsole(cards);
      System.out.println();
   }

   public int getPlayersInHand() {
      return playersInHand;
   }

   public void setPlayersInHand(int playersInHand) {
      this.playersInHand = playersInHand;
   }

   public int getBigBlindAmount() {
      return bigBlindAmount;
   }

   public void setBigBlindAmount(int bigBlindAmount) {
      this.bigBlindAmount = bigBlindAmount;
   }

   public int getSmallBlindAmount() {
      return smallBlindAmount;
   }

   public void setSmallBlindAmount(int smallBlindAmount) {
      this.smallBlindAmount = smallBlindAmount;
   }

   public BetPeriod getBetPeriod() {
      return betPeriod;
   }

   public void setBetPeriod(BetPeriod betPeriod) {
      this.betPeriod = betPeriod;
   }

   public int getCurrentBet() {
      return currentBet;
   }

   public void setCurrentBet(int currentBet) {
      this.currentBet = currentBet;
   }

   public int getPot() {
      return pot;
   }

   public void setPot(int pot) {
      this.pot = pot;
   }

   public Player getWinner() {
      return winner;
   }

   public void setWinner(Player winner) {
      this.winner = winner;
   }

   public ArrayList<Player> getPlayersTied() {
      return playersTied;
   }

   public void setPlayersTied(ArrayList<Player> playersTied) {
      this.playersTied = playersTied;
   }

   public int getTotalBet() {
      return totalBet;
   }

   public void setTotalBet(int totalBet) {
      this.totalBet = totalBet;
   }

   public void setMainApp(Main mainApp) {
      this.mainApp = mainApp;
   }
}
