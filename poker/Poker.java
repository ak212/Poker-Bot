package poker;

import java.util.ArrayList;
import java.util.Collections;

public class Poker {
   public static Dealer dealer;

   public static void main(String[] args) {
      dealer = new Dealer();

      int gameState = 0;
      boolean playGame = true;
      int playerId = 0;

      int startingChips = 10000;
      ArrayList<Player> players = new ArrayList<Player>();
      players.add(new Player(playerId++, startingChips));
      players.add(new Bot(playerId++, startingChips));
      // players.add(new Bot(playerId++, startingChips));

      dealer.betPeriod = BetPeriod.getBetPeriod(gameState);

      while (playGame) {
         switch (dealer.betPeriod) {
         case PREFLOP:
            players = dealer.determinePositions(players);

            for (Player player : players) {
               if (player.dealerButton) {
                  if (player instanceof Bot) {
                     System.out.println("Bot " + player.id + " dealer button");
                  }
                  else {
                     System.out.println("Player " + player.id + " dealer button");
                  }
               }
               if (player.bigBlind) {
                  player.blind(dealer.bigBlindAmount);
                  dealer.pot += dealer.bigBlindAmount;
                  dealer.currentBet = dealer.bigBlindAmount;
               }
               else if (player.smallBlind) {
                  player.blind(dealer.smallBlindAmount);
                  dealer.pot += dealer.smallBlindAmount;
               }
            }

            players = dealer.dealHoleCards(players);

            for (Player player : players) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + player.id);
                  ((Bot) player).evalHoleCards();
               }
               else {
                  System.out.println("Player " + player.id);
               }

               player.holeCards.printHoleCards();
            }

            Collections.sort(players, new PreFlopComparator());
            players = dealer.evaluateHandStrength(players, new ArrayList<Card>());
            players = dealer.betPeriod(players);
            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();
            Collections.sort(players, new PositionComparator());
            players = dealer.evaluateHandStrength(players, dealer.communityCards);
            players = dealer.betPeriod(players);
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();
            players = dealer.evaluateHandStrength(players, dealer.communityCards);
            players = dealer.betPeriod(players);
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();
            players = dealer.evaluateHandStrength(players, dealer.communityCards);
            players = dealer.betPeriod(players);
            break;

         case EVAL:
            if (dealer.playersInHand == 1) {
               for (Player player : players) {
                  if (player.inHand) {
                     System.out.println("Player " + player.id + " wins " + dealer.pot);
                     player.stack += dealer.pot;
                  }
               }
            }
            else {
               // TODO Determine winner with hand strengths
               // TODO New class with Hand, card value of hand and arraylist of kickers to be used in case of tie
               // TODO Hand comparisons i.e. pair of Js vs pair of Qs.

            }

            // TODO way to remove players from the game when they are out of chips

            System.out.println("\n\n");
            dealer.betPeriod = BetPeriod.getBetPeriod(gameState = -1);
            dealer.newHand();

            for (Player player : players) {
               player.nextHand();
            }
            break;

         default:
            break;
         }

         dealer.currentBet = 0;
         dealer.betPeriod = BetPeriod.getBetPeriod(++gameState);
         System.out.println("Current Pot: " + dealer.pot);

         for (Player player : players) {
            player.playerActed = false;
         }

         try {
            for (Player player : players) {
               if (player instanceof Bot) {
                  System.out.println("Bot " + player.id + " has hand " + player.currentHand.toString());
               }
               else {
                  System.out.println("Player " + player.id + " has hand " + player.currentHand.toString());
               }
            }
         }
         catch (NullPointerException e) {
            // New hand
         }

         if (dealer.playersInHand == 1) {
            dealer.betPeriod = BetPeriod.EVAL;
         }
      }
   }
}
