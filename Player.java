public class Player {
   int playerPosition;
   HoleCards holeCards;
   int stack;
   boolean inHand;
   boolean dealerButton;
   boolean bigBlind;
   boolean smallBlind;

   public Player(int position, int stack) {
      this.playerPosition = position;
      this.stack = stack;
   }

   public void bet(int bet) {
      // TODO need to handle case where bet exceeds stack, exception? shouldn't
      // be problem when we have a gui
      this.stack -= bet;

      System.out.println("Player " + this.playerPosition + (bet == 0 ? " checks" : " bets " + bet));
   }

   public void blind(int blind) {
      this.stack -= blind;

      System.out.println("Player " + this.playerPosition + (bigBlind ? " big blind" : " small blind"));

   }
}
