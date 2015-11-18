package poker.model.game;

import java.util.ArrayList;

import poker.Main;
import poker.model.cards.Card;
import poker.model.hand.HandEvaluator;
import poker.model.player.Bot;
import poker.model.player.Player;

public class DealerUtils {
   public static int getPlayersToBeDealt(ArrayList<Player> players) {
      int count = 0;

      for (Player player : players) {
         if (player.getStack() > 0) {
            count++;
         }
      }

      return count;
   }

   public static int getPlayersInHand(ArrayList<Player> players) {
      int count = 0;

      for (Player player : players) {
         if (player.isInHand()) {
            count++;
         }
      }

      return count;
   }

   public static ArrayList<Player> evaluateHandStrength(ArrayList<Player> players, ArrayList<Card> board) {
      for (Player player : players) {
         player.setCurrentHand(HandEvaluator.evaluateForHand(new ArrayList<Card>(board), player.getHoleCards()));

         if (player.getCurrentHand().hand.compareTo(player.stats.bestHand) < 0) {
            player.stats.bestHand = player.getCurrentHand().hand;
         }
      }

      return players;
   }

   public static boolean isAllInSituation(ArrayList<Player> players) {
      int playersInHand = getPlayersInHand(players);
      int playersAllIn = 0;
      boolean allInSituation = false;

      for (Player player : players) {
         if (player.isAllIn()) {
            playersAllIn++;
         }
      }

      if (playersAllIn + 1 >= playersInHand) {
         allInSituation = true;
      }

      return allInSituation;
   }

   public static boolean betSettled(ArrayList<Player> players) {
      int playersInHand = getPlayersInHand(players);
      int playersActed = 0;

      for (Player player : players) {
         if (player.isInHand() && player.isPlayerActed()) {
            playersActed++;
         }
      }

      return playersInHand == playersActed || playersInHand == 1;
   }
   
   public static ArrayList<Player> updatePosition(ArrayList<Player> players) {
      int i = 1;

      for (Player player : players) {
         if (player.isInHand()) {
            player.setPosition(i++);
         }
      }

      return players;
   }

   public static ArrayList<Player> retrieveEliminatedPlayers(ArrayList<Player> players) {
      ArrayList<Player> playersToBeRemoved = new ArrayList<Player>();

      for (Player player : players) {
         if (player.getStack() <= 0) {
            playersToBeRemoved.add(player);
         }
      }

      return playersToBeRemoved;
   }

   public static ArrayList<Player> removeEliminatedPlayers(ArrayList<Player> players, Main mainApp) {
      ArrayList<Player> playersToBeRemoved = DealerUtils.retrieveEliminatedPlayers(players);

      if (!playersToBeRemoved.isEmpty()) {
         for (Player player : playersToBeRemoved) {
            System.out.println("Player " + player.getId() + " eliminated");
            mainApp.updateConsole("Player " + player.getId() + " eliminated");
            players.remove(player);
         }
      }

      return players;
   }

   public static void printHandValues(ArrayList<Player> players) {
      for (Player player : players) {
         if (player.isInHand()) {
            if (player instanceof Bot) {
               System.out.print("Bot " + player.getId() + " has hand " + player.getCurrentHand().hand.toString());
            }
            else {
               System.out.print("Player " + player.getId() + " has hand " + player.getCurrentHand().hand.toString());
            }
            System.out.print(" Kickers: ");
            for (Integer i : player.getCurrentHand().kickers)
               System.out.print(i + ",");
            System.out.println();
         }
      }
   }

   public static void printStackValues(ArrayList<Player> players) {
      for (Player player : players) {
         if (player instanceof Bot) {
            System.out.println("Bot " + player.getId() + " stack: " + player.getStack());
         }
         else {
            System.out.println("Player " + player.getId() + " stack: " + player.getStack());
         }
      }
   }
}
