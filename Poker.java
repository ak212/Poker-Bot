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

   public static Hand dealHand() {
      dealer.playersInHand++;
      Card card1 = dealCard();
      Card card2 = dealCard();

      return new Hand(card1, card2);
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
         System.out.println("Player " + player.playerId + " folds");
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
      int playerId = 1;

      int startingChips = 1000;
      Player player1 = new Player(playerId++, startingChips);
      Player player2 = new Player(playerId++, startingChips);
      int betAmount = 100;

      while (playGame) {
         switch (gameState) {
         case PRE_FLOP:
            player1.hand = dealHand();
            player2.hand = dealHand();

            System.out.println("Player 1");
            player1.hand.printHand();
            System.out.println("Player 2");
            player2.hand.printHand();

            // TODO need bet period function. players to global vars?
            player1 = playerInput(player1);

            if (dealer.playersInHand > 1) {
               player2.bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();

            player1 = playerInput(player1);
            if (dealer.playersInHand > 1) {
               player2.bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            player1 = playerInput(player1);
            if (dealer.playersInHand > 1) {
               player2.bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            player1 = playerInput(player1);
            if (dealer.playersInHand > 1) {
               player2.bet(dealer.currentBet);
               dealer.pot += dealer.currentBet;
            }
            break;
         }

         dealer.currentBet = 0;
         gameState += 1;
         System.out.println("Current Pot: " + dealer.pot);

         if (gameState > RIVER || dealer.playersInHand == 1) {
            dealer.playersInHand = 0;
            gameState = PRE_FLOP;
            dealer.newHand();
         }
      }
   }
}
