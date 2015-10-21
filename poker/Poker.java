package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Poker {
   public static Dealer dealer;

   public static Player playerInput(Player p) {
      Player player = p;
      Scanner scan = new Scanner(System.in);
      String playerAction = scan.next();

      switch (playerAction) {
      case "b":
         dealer.currentBet = scan.nextInt();
         player.bet(dealer.currentBet);
         dealer.pot += dealer.currentBet;
         break;
      case "c":
         int betAmount = 0;
         if (dealer.betPeriod.equals(BetPeriod.PREFLOP)) {
            if (player.bigBlind) {
               betAmount = dealer.currentBet - dealer.bigBlindAmount;
            }
            else if (player.smallBlind) {
               betAmount = dealer.currentBet - dealer.smallBlindAmount;
            }
            else {
               betAmount = dealer.currentBet;
            }
         }
         else {
            betAmount = dealer.currentBet;
         }

         player.bet(betAmount);
         dealer.pot += betAmount;
         break;
      case "f":
         player.inHand = false;
         dealer.playersInHand--;
         System.out.println("Player " + player.id + " folds");
         break;
      default:
         break;
      }

      return player;
   }

   public static Player botInput(Player p) {
      Bot b = (Bot) p;

      if (dealer.betPeriod.equals(BetPeriod.PREFLOP)) {
         if (b.bigBlind) {
            b.action(dealer.currentBet - dealer.bigBlindAmount);
         }
         else if (b.smallBlind) {
            b.action(dealer.currentBet - dealer.smallBlindAmount);
         }
         else {
            b.action(dealer.currentBet);
         }
      }
      else {
         b.action(dealer.currentBet);
      }

      switch (b.botTurn.botAction) {
      case CHECKCALL:
         b.bet(b.botTurn.betAmount);
         dealer.pot += b.botTurn.betAmount;
         break;
      case BET:
         b.bet(b.botTurn.betAmount);
         dealer.currentBet = b.botTurn.betAmount;
         dealer.pot += b.botTurn.betAmount;
         break;
      case FOLD:
         b.inHand = false;
         dealer.playersInHand--;
         System.out.println("Bot " + b.id + " folds");
      }

      return b;
   }

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
                  ((Bot) player).evalHoleCards();
                  System.out.println("Bot " + player.id);
               }
               else {
                  System.out.println("Player " + player.id);
               }

               player.holeCards.printHoleCards();
            }

            Collections.sort(players, new PreFlopComparator());

            while (!dealer.betSettled(players)) {
               for (Player player : players) {
                  if (player.inHand && !player.playerActed) {
                     int curBet = dealer.currentBet;

                     if (player.getClass() == Bot.class) {
                        players.set(player.preFlopPosition - 1, botInput(players.get(player.preFlopPosition - 1)));
                     }
                     else {
                        players.set(player.preFlopPosition - 1, playerInput(players.get(player.preFlopPosition - 1)));
                     }
                     if (dealer.currentBet != curBet) {
                        dealer.resetPlayersActed(players, player.preFlopPosition);
                     }
                  }
               }
            }

            // TODO need bet period function. players to global vars?

            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();
            Collections.sort(players, new PositionComparator());

            if (dealer.playersInHand > 1) {
               while (!dealer.betSettled(players)) {
                  for (Player player : players) {
                     if (player.inHand && !player.playerActed) {
                        int curBet = dealer.currentBet;

                        if (player.getClass() == Bot.class) {
                           players.set(player.position - 1, botInput(players.get(player.position - 1)));
                        }
                        else {
                           players.set(player.position - 1, playerInput(players.get(player.position - 1)));
                        }
                        if (dealer.currentBet != curBet) {
                           dealer.resetPlayersActed(players, player.position - 1);
                        }
                     }
                  }
               }
            }
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            if (dealer.playersInHand > 1) {
               while (!dealer.betSettled(players)) {
                  for (Player player : players) {
                     if (player.inHand && !player.playerActed) {
                        int curBet = dealer.currentBet;

                        if (player.getClass() == Bot.class) {
                           players.set(player.position - 1, botInput(players.get(player.position - 1)));
                        }
                        else {
                           players.set(player.position - 1, playerInput(players.get(player.position - 1)));
                        }
                        if (dealer.currentBet != curBet) {
                           dealer.resetPlayersActed(players, player.position - 1);
                        }
                     }
                  }
               }
            }

            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            if (dealer.playersInHand > 1) {
               while (!dealer.betSettled(players)) {
                  for (Player player : players) {
                     if (player.inHand && !player.playerActed) {
                        int curBet = dealer.currentBet;

                        if (player.getClass() == Bot.class) {
                           players.set(player.position - 1, botInput(players.get(player.position - 1)));
                        }
                        else {
                           players.set(player.position - 1, playerInput(players.get(player.position - 1)));
                        }
                        if (dealer.currentBet != curBet) {
                           dealer.resetPlayersActed(players, player.position - 1);
                        }
                     }
                  }
               }
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

         if (gameState > BetPeriod.RIVER.getValue() || dealer.playersInHand == 1) {
            if (dealer.playersInHand == 1) {
               for (Player player : players) {
                  if (player.inHand) {
                     System.out.println("Player " + player.id + " wins " + dealer.pot);
                     player.stack += dealer.pot;
                  }
               }
            }

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
