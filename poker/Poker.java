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
      players.add(new Bot(playerId++, startingChips));

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

            if (dealer.playersInHand > 1) {
               players = dealer.betPeriod(players);
            }
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            if (dealer.playersInHand > 1) {
               players = dealer.betPeriod(players);
            }

            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            if (dealer.playersInHand > 1) {
               players = dealer.betPeriod(players);
            }

            break;
	 case EVAL:
	   
	    // must be at least 2 player in hand to reach here
            for (Player player : players) {
		//loop through all players and store the strongest hand found so far and its owner
	    }
	    // store such that 
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

	 //maybe change gameState check to BetPeriod.EVAL.getValue()
         if (gameState > BetPeriod.RIVER.getValue() || dealer.playersInHand == 1) {
            if (dealer.playersInHand == 1) {
               for (Player player : players) {
                  if (player.inHand) {
                     System.out.println("Player " + player.id + " wins " + dealer.pot);
                     player.stack += dealer.pot;
                  }
               }
            }
	    //else
		//strongest Hand owner, determined in EVAL, wins

            // TODO better way to remove players from the game when they are out of chips
            for (Player player : players) {
               if (player.stack == 0) {
                  players.remove(player);
               }
            }

            System.out.println("\n\n");
            dealer.betPeriod = BetPeriod.getBetPeriod(gameState = 0);
            dealer.newHand();

            for (Player player : players) {
               player.nextHand();
            }
         }
      }
   }
}
