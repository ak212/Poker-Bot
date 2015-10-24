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
            dealer.playersInHand = dealer.getPlayersToBeDealt(players);
            Collections.sort(players, new IdComparator());

            players.get(dealer.dealerButtonPosition % dealer.playersInHand).dealerButton = true;
            if (dealer.playersInHand == 2) {
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).smallBlind = true;
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).position = 2;
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).preFlopPosition = 1;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).bigBlind = true;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).position = 1;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).preFlopPosition = 2;
            }
            else {
               players.get(dealer.smallBlindPosition % dealer.playersInHand).smallBlind = true;
               players.get(dealer.smallBlindPosition % dealer.playersInHand).position = 1;
               players.get(dealer.bigBlindPosition % dealer.playersInHand).bigBlind = true;
               players.get(dealer.bigBlindPosition % dealer.playersInHand).position = 2;

               for (int i = 2; i < dealer.playersInHand; i++) {
                  players.get((dealer.smallBlindPosition + i) % dealer.playersInHand).position = i + 1;
               }
               for (int i = 2; i < dealer.playersInHand + 2; i++) {
                  players.get((dealer.smallBlindPosition + i) % dealer.playersInHand).preFlopPosition = i - 1;
               }
            }

            for (Player player : players) {
               if (player.dealerButton) {
                  if (player.getClass() == Bot.class) {
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
               if (player.getClass() == Bot.class) {
                  System.out.println("Bot " + player.id);
                  ((Bot) player).evalHoleCards();
               }
               else {
                  System.out.println("Player " + player.id);
               }

               player.holeCards.printHoleCards();
            }

            Collections.sort(players, new PreFlopComparator());

            players = dealer.betPeriod(players);

            // TODO need bet period function. players to global vars?

            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();
            Collections.sort(players, new PositionComparator());
            players = dealer.betPeriod(players);
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();
            players = dealer.betPeriod(players);
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();
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
               // TODO New class with Hand and arraylist of kickers to be used in case of tie
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
            ArrayList<Card> board = new ArrayList<Card>(dealer.communityCards);
            Hand hand = HandEvaluator.evaluateForHand(board, player.holeCards);
            if (player.getClass() == Bot.class) {
               System.out.println("Bot " + player.id + " has hand " + hand.toString());
            }
            else {
               System.out.println("Player " + player.id + " has hand " + hand.toString());
            }
            player.playerActed = false;
         }

         if (dealer.playersInHand == 1) {
            dealer.betPeriod = BetPeriod.EVAL;
         }
      }
   }
}
