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

/**
 * Class to act as the poker game expert, keeping track of all information needed by the game.
 * 
 * @author Aaron Koeppel
 *
 */
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
   private boolean allInSituation;

   ArrayList<Integer> sidePots;
   private int playersInHand;
   public Main mainApp;

   public int minBetAmount;
   public int halfPotBetAmount;
   public int potBetAmount;
   public int maxBetAmount;
   public int minAmount;

   /**
    * Construct the dealer, setting member variables to their default values.
    */
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

   /**
    * Remove card from the deck and return it.
    * 
    * @return card from the deck
    */
   public Card drawCard() {
      return this.deckOfCards.getDeck().remove(0);
   }

   /**
    * Get the input from the player. The input is the action the player wants to perform (check/call, bet, fold) and the
    * amount of chips they are putting in the pot.
    * 
    * @param p
    *           the player acting
    * @return the updated player with their action performed
    */
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

   /**
    * Get the input from the bot. The input is the action the bot wants to perform (check/call, bet, fold) and the
    * amount of chips they are putting in the pot.
    * 
    * @param p
    *           the bot acting
    * @param players
    *           the players in the hand
    * @return the updated bot with their action performed
    */
   public Player botInput(Player p, ArrayList<Player> players) {
      Bot b = (Bot) p;

      b.getOpponentStackSizes(players);

      if (this.getBetPeriod().equals(BetPeriod.PREFLOP)) {
         b.determinePreFlopAction(this.getCurrentBet(), this.getTotalBet(), this.getBigBlindAmount());
      } 
      else {
         b.determinePostFlopAction(this.getCurrentBet(), this.getTotalBet(), this);
      }

      int betAmount = b.getBotTurn().getBetAmount() > this.maxBetAmount ? this.maxBetAmount
            : b.getBotTurn().getBetAmount();

      switch (b.getBotTurn().getAction()) {
      case CHECKCALL:
         b.call(betAmount);
         this.setPot(this.getPot() + betAmount);
         this.setCurrentBet(b.getTotalBet());

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
         System.out.println("Bot bet amount: " + b.getBotTurn().getBetAmount());
         betAmount -= callAmount;
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

   /**
    * Function to determine the preflop and postflop positions for players in the hand.
    * 
    * @param players
    *           the players in the hand
    * @return the players in the hand with their positions for the hand set
    */
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

   /**
    * Function to reset the players acted field for players except the player whose position is that of the exception.
    * 
    * @param players
    *           the players in the hand
    * @param exception
    *           the position of the player to not reset
    * @return
    */
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

   /**
    * Deal hole cards to the players in the hand
    * 
    * @param players
    *           the players in the hand
    * @return the players in the ahnd with their hole cards
    */
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

   /**
    * The period in which the players make bets.
    * 
    * @param players
    *           the players in the hand
    * @return the players still in the hand after they have made bets or folded
    */
   public ArrayList<Player> betPeriod(ArrayList<Player> players) {
      this.setTotalBet(this.getCurrentBet());

      this.setAllInSituation(DealerUtils.isAllInSituation(players));

      if (this.isAllInSituation()) {
         for (Player player : players) {
            if (player instanceof Bot) {
               mainApp.showBotHoleCards(player.getHoleCards());
            }
         }
      }

      while (!DealerUtils.betSettled(players) && !this.isAllInSituation()) {
         for (Player player : players) {
            if (player.isInHand() && !player.isPlayerActed()) {
               this.getBetAmounts(player, players);
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
                     players.set(player.getPosition() - 1, playerInput(players.get(player.getPosition() - 1)));
                     mainApp.updateStackZero(Integer.toString(player.getStack()));
                  }
                  if (this.getTotalBet() != curBet) {
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

   /**
    * Function to figure who won the hand.
    * 
    * @param players
    *           the players in the hand
    * @return the players in the hand with the winner(s) stack updated
    */
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
               mainApp.showBotHoleCards(player.getHoleCards());
               try {
                  Thread.sleep(2000);
               }
               catch (InterruptedException e) {
                  e.printStackTrace();
               }
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

   /**
    * Function to perform the flop, burning one card, adding three to the community cards.
    */
   public void flop() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
      this.communityCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   /**
    * Function to perform the turn, burning one card, adding one to the community cards.
    */
   public void turn() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   /**
    * Function to perform the river, burning one card, adding one to the community cards.
    */
   public void river() {
      this.burnCards.add(drawCard());
      this.communityCards.add(drawCard());
   }

   /**
    * Function to perform actions that are performed for each bet period.
    * 
    * @param players
    *           the players in the hand
    * @return the players after the bet period
    */
   public ArrayList<Player> completeRound(ArrayList<Player> players) {
      this.printCommunityCards();
      players = DealerUtils.updatePosition(players);
      players = DealerUtils.evaluateHandStrength(players, this.communityCards);
      DealerUtils.printHandValues(players);

      players = this.betPeriod(players);

      return players;
   }

   /**
    * Function to get the minimum amount the player can put in the pot to stay in the hand
    * 
    * @param player
    *           the player who would be making the bet
    * @return the amount of the bet
    */
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

   /**
    * Function to get the minimum bet for the player
    * 
    * @param player
    *           the player who would be making the bet
    * @return the amount of the bet
    */
   public int getMinBet(Player player, ArrayList<Player> players) {
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

      return validateBetAmount(betAmount, player, players);
   }

   /**
    * Function to get the amount of half of the pot
    * 
    * @param player
    *           the player who would be making the bet
    * @return the amount of the bet
    */
   public int getHalfPotBet(Player player, ArrayList<Player> players) {
      int amount = (int) Math.ceil(this.getPot() / 2);
      amount = amount < minBetAmount ? minBetAmount : amount;

      return validateBetAmount(amount, player, players);
   }

   /**
    * Function to get the amount of the pot
    * 
    * @param player
    *           the player who would be making the bet
    * @return the amount of the bet
    */
   public int getPotBet(Player player, ArrayList<Player> players) {
      return validateBetAmount(this.getPot(), player, players);
   }

   /**
    * Function to get the maximum bet for the player
    * 
    * @param player
    *           the player who would be making the bet
    * @return the amount of the bet
    */
   public int getMaxBet(Player player, ArrayList<Player> players) {
      return validateBetAmount(player.getStack(), player, players);
   }

   /**
    * Function to make sure that the bet being made is not greater than the stack size of anyone still in the hand
    * 
    * @param amount
    *           the amount of chips being bet
    * @param players
    *           the players still in the game
    * @return the legal amount of chips for the bet
    */
   public int validateBetAmount(int amount, Player player, ArrayList<Player> players) {
      for (Player p : players) {
         if (p.isInHand()) {
            amount = amount > p.getStack() + p.getTotalBet() - player.getTotalBet()
                  ? p.getStack() + p.getTotalBet() - player.getTotalBet() : amount;
         }
      }

      return amount;
   }

   /**
    * Function to get the bet amounts
    * 
    * @param player
    *           the player who would be making the bet
    */
   public void getBetAmounts(Player player, ArrayList<Player> players) {
      minAmount = getMinAmount(player);
      minBetAmount = getMinBet(player, players);
      halfPotBetAmount = getHalfPotBet(player, players);
      potBetAmount = getPotBet(player, players);
      maxBetAmount = getMaxBet(player, players);
   }

   /**
    * Set the dealer member values for the next hand
    */
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
      this.setAllInSituation(false);
      this.getWinner().setCurrentHand(new HandStrength(Hand.NoHand, new ArrayList<Integer>()));
   }

   /**
    * Get the community cards.
    * 
    * @return the community cards
    */
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

   /**
    * Get the numbers of players in the hand.
    * 
    * @return the numbers of players
    */
   public int getPlayersInHand() {
      return playersInHand;
   }

   /**
    * Set the number of players in the hand.
    */
   public void setPlayersInHand(int playersInHand) {
      this.playersInHand = playersInHand;
   }

   /**
    * Get the big blind amount.
    * 
    * @return the big blind amount
    */
   public int getBigBlindAmount() {
      return bigBlindAmount;
   }

   /**
    * Set the big blind amount.
    */
   public void setBigBlindAmount(int bigBlindAmount) {
      this.bigBlindAmount = bigBlindAmount;
   }

   /**
    * Get the small blind amount.
    * 
    * @return the big blind amount
    */
   public int getSmallBlindAmount() {
      return smallBlindAmount;
   }

   /**
    * Set the small blind amount.
    */
   public void setSmallBlindAmount(int smallBlindAmount) {
      this.smallBlindAmount = smallBlindAmount;
   }

   /**
    * Get the bet period.
    * 
    * @return the bet period
    */
   public BetPeriod getBetPeriod() {
      return betPeriod;
   }

   /**
    * Set the bet period.
    * 
    * @param betPeriod
    */
   public void setBetPeriod(BetPeriod betPeriod) {
      this.betPeriod = betPeriod;
   }

   /**
    * Get the current bet.
    * 
    * @return the current bet
    */
   public int getCurrentBet() {
      return currentBet;
   }

   /**
    * Set the current bet.
    * 
    * @param currentBet
    */
   public void setCurrentBet(int currentBet) {
      this.currentBet = currentBet;
   }

   /**
    * Get the pot amount.
    * 
    * @return the pot amount
    */
   public int getPot() {
      return pot;
   }

   /**
    * Set the pot amount.
    * 
    * @param pot
    */
   public void setPot(int pot) {
      this.pot = pot;
   }

   /**
    * Get the current winner of the hand.
    * 
    * @return the winner
    */
   public Player getWinner() {
      return winner;
   }

   /**
    * Set the current winner of the hand.
    * 
    * @param winner
    */
   public void setWinner(Player winner) {
      this.winner = winner;
   }

   /**
    * Get the players tied for winning.
    * 
    * @return the players tied
    */
   public ArrayList<Player> getPlayersTied() {
      return playersTied;
   }

   /**
    * Set the players tied.
    * 
    * @param playersTied
    */
   public void setPlayersTied(ArrayList<Player> playersTied) {
      this.playersTied = playersTied;
   }

   /**
    * Get the total bet.
    * 
    * @return the total bet
    */
   public int getTotalBet() {
      return totalBet;
   }

   /**
    * Set the total bet.
    * 
    * @param totalBet
    */
   public void setTotalBet(int totalBet) {
      this.totalBet = totalBet;
   }

   /**
    * Get if it is an all in situation.
    * 
    * @return if it is an all in situation
    */
   public boolean isAllInSituation() {
      return allInSituation;
   }

   /**
    * Set if it is an all in situation
    * 
    * @param allInSituation
    */
   public void setAllInSituation(boolean allInSituation) {
      this.allInSituation = allInSituation;
   }

   /**
    * Is called by the main application to give a reference back to itself. Used to hand off control to the main
    * application to load the appropriate content.
    * 
    * @param mainApp
    *           Representation of the main application for reference to hand off control.
    */
   public void setMainApp(Main mainApp) {
      this.mainApp = mainApp;
   }

   /**
    * Get the deck of cards.
    * 
    * @return the deck of cards
    */
   public ArrayList<Card> getDeck() {
      return this.deckOfCards.getDeck();
   }
}
