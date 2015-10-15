import java.util.ArrayList;
import java.util.Scanner;

public class Poker {
   public static Dealer dealer;
   public static final int PRE_FLOP = 0;
   public static final int FLOP = 1;
   public static final int TURN = 2;
   public static final int RIVER = 3;

   private static Card dealCard() {
      Card card = null;

      try {
         if (dealer.deckOfCards.deck.size() == 0) {
            throw new Exception("No cards in deck");
         }
         card = dealer.drawCard();
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }

      return card;
   }

   public static ArrayList<Player> dealHoleCards(ArrayList<Player> players) {
      Card[] cardsDealt = new Card[players.size() * 2];
      for (int i = 0; i < players.size() * 2; i++) {
         cardsDealt[i] = dealer.drawCard();
      }

      for (Player player : players) {
         player.holeCards = new HoleCards(cardsDealt[player.playerPosition],
               cardsDealt[player.playerPosition + players.size()]);
         player.inHand = true;
         dealer.playersInHand++;
      }

      return players;
   }

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
         player.bet(dealer.currentBet);
         dealer.pot += dealer.currentBet;
         break;
      case "f":
         player.inHand = false;
         dealer.playersInHand--;
         System.out.println("Player " + player.playerPosition + " folds");
         break;
      default:
         break;
      }

      return player;
   }

   public static void main(String[] args) {
      dealer = new Dealer();

      int gameState = 0;
      boolean playGame = true;
      int playerPosition = 0;

      int startingChips = 10000;
      ArrayList<Player> players = new ArrayList<Player>();
      players.add(new Player(playerPosition++, startingChips));
      players.add(new Player(playerPosition++, startingChips));
      
      while (playGame) {
         switch (gameState) {
         case PRE_FLOP:
            players = dealHoleCards(players);
            
            players.get(dealer.dealerButtonPosition % dealer.playersInHand).dealerButton = true;

            if (dealer.playersInHand == 2) {
               players.get(dealer.dealerButtonPosition % dealer.playersInHand).smallBlind = true;
               players.get((dealer.dealerButtonPosition + 1) % dealer.playersInHand).bigBlind = true;
            }
            else {
               players.get(dealer.smallBlindPosition % dealer.playersInHand).smallBlind = true;
               players.get(dealer.bigBlindPosition % dealer.playersInHand).bigBlind = true;
            }
            
            for (Player player : players) {
               if (player.bigBlind) {
                  player.blind(dealer.bigBlindAmount);
                  dealer.pot += dealer.bigBlindAmount;
               } 
               else if (player.smallBlind) {
                  player.blind(dealer.smallBlindAmount);
                  dealer.pot += dealer.smallBlindAmount;
               }
            }

            System.out.println("Player 1");
            players.get(0).holeCards.printHoleCards();
            System.out.println("Player 2");
            players.get(1).holeCards.printHoleCards();

            // TODO need bet period function. players to global vars?
            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               players.get(1).bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               players.get(1).bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               players.get(1).bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            players.set(0, playerInput(players.get(0)));

            if (dealer.playersInHand > 1) {
               players.get(1).bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;
         }

         dealer.currentBet = 0;
         gameState += 1;
         System.out.println("Current Pot: " + dealer.pot);

         if (gameState > RIVER || dealer.playersInHand == 1) {
            if (dealer.playersInHand == 1) {
               for (Player player : players) {
                  if (player.inHand) {
                     System.out.println("Player " + player.playerPosition + " wins " + dealer.pot);
                     player.stack += dealer.pot;
                  }
               }
            }

            System.out.println("\n\n");
            gameState = PRE_FLOP;
            dealer.newHand();

            for (Player player : players) {
               player.bigBlind = false;
               player.dealerButton = false;
               player.smallBlind = false;
            }
         }
      }
   }
}
