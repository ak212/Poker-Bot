package poker.model;

import java.util.ArrayList;
import java.util.Collections;

import poker.Main;
import poker.model.cards.Card;
import poker.model.game.BetPeriod;
import poker.model.game.Dealer;
import poker.model.game.DealerUtils;
import poker.model.game.PositionComparator;
import poker.model.game.PreFlopComparator;
import poker.model.player.Bot;
import poker.model.player.Player;

/**
 * Class to run the main poker function.
 * 
 * @author Aaron Koeppel
 *
 */

public class Poker {
   public static Dealer dealer;
   public static Bot bot;
   public Main mainApp;

   /**
    * Function that controls the poker game. Handles all aspects of the game.
    */
   public void playPoker() {
      dealer = new Dealer();
      dealer.setMainApp(mainApp);

      int gameState = 0;
      boolean playGame = true;
      int playerId = 0;

      int startingChips = 2500;
      ArrayList<Player> players = new ArrayList<Player>();
      ArrayList<Player> playersEliminated = new ArrayList<Player>();
      players.add(new Player(playerId++, startingChips));
      players.add(new Bot(playerId++, startingChips));
      // players.add(new Bot(playerId++, startingChips));

      for (Player player : players) {
         player.setMainApp(mainApp);
         if (player instanceof Bot) {
            bot = (Bot)player;
         }
      }
      

      dealer.setBetPeriod(BetPeriod.getBetPeriod(gameState));

      while (playGame) {
         mainApp.updatePot(Integer.toString(dealer.getPot()));

         switch (dealer.getBetPeriod()) {
         case PREFLOP:
            mainApp.getCommunityCards(dealer.getCommunityCards());

            if (DealerUtils.getPlayersToBeDealt(players) < 2) {
               playGame = false;
               break;
            }

            players = dealer.determinePositions(players);

            for (Player player : players) {
               if (player instanceof Bot) {
                  mainApp.toggleDealerOne(player.isDealerButton());
               }
               else {
                  mainApp.toggleDealerZero(player.isDealerButton());
               }
               if (player.isDealerButton()) {
                  if (player instanceof Bot) {
                     System.out.println("Bot " + player.getId() + " dealer button");
                  }
                  else {
                     System.out.println("Player " + player.getId() + " dealer button");
                  }
               }
               if (player.isBigBlind()) {
                  player.blind(dealer.getBigBlindAmount());
                  dealer.setPot(dealer.getPot() + dealer.getBigBlindAmount());
                  dealer.setCurrentBet(dealer.getBigBlindAmount());
               }
               else if (player.isSmallBlind()) {
                  player.blind(dealer.getSmallBlindAmount());
                  dealer.setPot(dealer.getPot() + dealer.getSmallBlindAmount());
               }
            }

            players = dealer.dealHoleCards(players);

            for (Player player : players) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + player.getId());
                  // mainApp.updateConsole("Bot " + player.getId());
                  ((Bot) player).evalHoleCards();
                  mainApp.updateStackOne(Integer.toString(player.getStack()));
               }
               else {
                  System.out.println("Player " + player.getId());
                  // mainApp.updateConsole("Player " + player.getId());
                  mainApp.updateStackZero(Integer.toString(player.getStack()));
                  mainApp.getHoleCards(player.getHoleCards());
               }

               player.printHoleCards();
            }

            Collections.sort(players, new PreFlopComparator());
            players = DealerUtils.evaluateHandStrength(players, new ArrayList<Card>());
            DealerUtils.printHandValues(players);
            mainApp.updatePot(Integer.toString(dealer.getPot()));
            players = dealer.betPeriod(players);
            break;

         case FLOP:
            dealer.flop();
            mainApp.getCommunityCards(dealer.getCommunityCards());
            Collections.sort(players, new PositionComparator());
            players = dealer.completeRound(players);
            break;

         case TURN:
            dealer.turn();
            mainApp.getCommunityCards(dealer.getCommunityCards());
            players = dealer.completeRound(players);
            break;

         case RIVER:
            dealer.river();
            mainApp.getCommunityCards(dealer.getCommunityCards());
            players = dealer.completeRound(players);
            break;

         case EVAL:
            if (dealer.getPlayersInHand() == 1) {
               for (Player player : players) {
                  if (player.isInHand()) {
                     if (player instanceof Bot) {
                        System.out.println("Bot " + player.getId() + " wins " + dealer.getPot());
                        mainApp.updateConsole("Bot " + player.getId() + " wins " + dealer.getPot());
                     }
                     else {
                        System.out.println("Player " + player.getId() + " wins " + dealer.getPot());
                        mainApp.updateConsole("Player " + player.getId() + " wins " + dealer.getPot());
                     }
                     player.setStack(player.getStack() + dealer.getPot());
                     player.stats.wins++;
                     player.stats.setWinnings(dealer.getPot());
                     if (dealer.getPot() > player.stats.biggestWin) {
                        player.stats.biggestWin = dealer.getPot();
                     }
                  }

                  if (player instanceof Bot) {
                     mainApp.updateStackOne(Integer.toString(player.getStack()));
                  }
                  else {
                     mainApp.updateStackZero(Integer.toString(player.getStack()));
                  }
               }
            }
            else {
               for (Player player : players) {
                  if (player.getCurrentHand().hand.getValue() > dealer.getWinner().getCurrentHand().hand.getValue()) {
                     dealer.setWinner(player);
                     dealer.getPlayersTied().clear();
                  }
                  else if (player.getCurrentHand().hand.getValue() == dealer.getWinner().getCurrentHand().hand.getValue()) {
                     dealer.getPlayersTied().add(player);
                  }  
               }
               
               players = dealer.determineWinner(players);
            }

            DealerUtils.printStackValues(players);
            playersEliminated.addAll(DealerUtils.retrieveEliminatedPlayers(players));
            players = DealerUtils.removeEliminatedPlayers(players, mainApp);

            System.out.println("\n\n");
            // mainApp.updateConsole("\n\n");
            dealer.setBetPeriod(BetPeriod.getBetPeriod(gameState = -1));

            for (Player player : players) {
               player.stats.overallAggression += player.stats.shortTermAggression[dealer.numHandsPlayed % 10];

               if (player instanceof Bot) {
                  System.out.println("Bot overall playstyle: " + player.stats.overallAggression / player.stats.overallDecisions);
               }
               else {
                  System.out.println("Player overall playstyle: " + player.stats.overallAggression / player.stats.overallDecisions);
               }

               player.stats.shortTermDecisions[(dealer.numHandsPlayed + 1) % 10] = 0;
               player.stats.shortTermAggression[(dealer.numHandsPlayed + 1) % 10] = 0;
            }

            dealer.newHand();
            mainApp.updatePot("0");

            for (Player player : players) {
               player.nextHand();
            }
            try {
               Thread.sleep(3000);
            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }
            break;

         default:
            break;
         }

         dealer.setCurrentBet(0);
         dealer.setTotalBet(0);
         dealer.setBetPeriod(BetPeriod.getBetPeriod(++gameState));
         System.out.println("Current Pot: " + dealer.getPot());

         mainApp.updateBetAmountZero(null);
         mainApp.updateBetAmountOne(null);

         for (Player player : players) {
            player.setPlayerActed(false);
            player.setTotalBet(0);
         }

         if (dealer.getPlayersInHand() == 1) {
            dealer.setBetPeriod(BetPeriod.EVAL);
         }
      }
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
}
