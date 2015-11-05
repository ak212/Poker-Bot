package poker.model;

import java.util.ArrayList;
import java.util.Collections;

import poker.model.cards.Card;
import poker.model.game.BetPeriod;
import poker.model.game.Dealer;
import poker.model.game.PositionComparator;
import poker.model.game.PreFlopComparator;
import poker.model.player.Bot;
import poker.model.player.Player;

public class Poker {
   public static Dealer dealer;

   public static void main(String[] args) {
      dealer = new Dealer();

      int gameState = 0;
      boolean playGame = true;
      int playerId = 0;

      int startingChips = 10000;
      ArrayList<Player> players = new ArrayList<Player>();
      ArrayList<Player> playersEliminated = new ArrayList<Player>();
      players.add(new Player(playerId++, startingChips));
      players.add(new Bot(playerId++, startingChips));
      // players.add(new Bot(playerId++, startingChips));

      dealer.setBetPeriod(BetPeriod.getBetPeriod(gameState));

      while (playGame) {
         switch (dealer.getBetPeriod()) {
         case PREFLOP:
            if (dealer.getPlayersToBeDealt(players) < 2) {
               playGame = false;
               break;
            }

            players = dealer.determinePositions(players);

            for (Player player : players) {
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
                  ((Bot) player).evalHoleCards();
               }
               else {
                  System.out.println("Player " + player.getId());
               }

               player.getHoleCards().printHoleCards();
            }

            Collections.sort(players, new PreFlopComparator());
            players = dealer.evaluateHandStrength(players, new ArrayList<Card>());
            dealer.printHandValues(players);
            players = dealer.betPeriod(players);
            break;

         case FLOP:
            dealer.flop();
            Collections.sort(players, new PositionComparator());
            players = dealer.completeRound(players);
            break;

         case TURN:
            dealer.turn();
            players = dealer.completeRound(players);
            break;

         case RIVER:
            dealer.river();
            players = dealer.completeRound(players);
            break;

         case EVAL:
            if (dealer.getPlayersInHand() == 1) {
               for (Player player : players) {
                  if (player.isInHand()) {
                     if (player instanceof Bot) {
                        System.out.println("Bot " + player.getId() + " wins " + dealer.getPot());
                     }
                     else {
                        System.out.println("Player " + player.getId() + " wins " + dealer.getPot());
                     }
                     player.setStack(player.getStack() + dealer.getPot());
                  }
               }
            }
            else {
               // TODO Determine winner with hand strengths
               // TODO New class with Hand, card value of hand and arraylist of kickers to be used in case of tie
               // TODO Hand comparisons i.e. pair of Js vs pair of Qs.
               
               for (Player player : players) {
                  if (player.getCurrentHand().getValue() > dealer.getWinner().getCurrentHand().getValue()) {
                     dealer.setWinner(player);
                     dealer.getPlayersTied().clear();
                  }
                  else if (player.getCurrentHand().getValue() == dealer.getWinner().getCurrentHand().getValue()) {
                     //dealer.compareHandStrength(player);
                     dealer.getPlayersTied().add(player);
                  }  
               }
               
               players = dealer.determineWinner(players);
            }

            dealer.printStackValues(players);
            playersEliminated.addAll(dealer.retrieveEliminatedPlayers(players));
            players = dealer.removeEliminatedPlayers(players);

            System.out.println("\n\n");
            dealer.setBetPeriod(BetPeriod.getBetPeriod(gameState = -1));
            dealer.newHand();

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

         for (Player player : players) {
            player.setPlayerActed(false);
            player.setTotalBet(0);
         }

         if (dealer.getPlayersInHand() == 1) {
            dealer.setBetPeriod(BetPeriod.EVAL);
         }
      }
   }
}
