public class CardDealer {
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
      Card card1 = dealCard();
      Card card2 = dealCard();

      return new Hand(card1, card2);
   }

   public static void main(String[] args) {
      dealer = new Dealer();

      int gameState = 0;
      boolean playGame = true;

      int startingChips = 1000;
      Player player1 = new Player(startingChips);
      Player player2 = new Player(startingChips);
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

            player1.bet(betAmount);
            dealer.pot += betAmount;
            player2.bet(betAmount);
            dealer.pot += betAmount;
            break;

         case FLOP:
            dealer.flop();
            dealer.printCommunityCards();

            player1.bet(betAmount);
            dealer.pot += betAmount;
            player2.bet(betAmount);
            dealer.pot += betAmount;
            break;

         case TURN:
            dealer.turn();
            dealer.printCommunityCards();

            player1.bet(betAmount);
            dealer.pot += betAmount;
            player2.bet(betAmount);
            dealer.pot += betAmount;
            break;

         case RIVER:
            dealer.river();
            dealer.printCommunityCards();

            player1.bet(betAmount);
            dealer.pot += betAmount;
            player2.bet(betAmount);
            dealer.pot += betAmount;
            break;
         }

         gameState += 1;

         if (gameState > RIVER) {
            gameState = PRE_FLOP;
            dealer.newHand();
         }
      }
   }
}
