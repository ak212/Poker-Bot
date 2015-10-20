package poker;

public class Player {
   int id;
   int position;
   int preFlopPosition;
   HoleCards holeCards;
   int stack;
   int potCommitment;
   boolean playerActed;
   boolean inHand;
   boolean dealerButton;
   boolean bigBlind;
   boolean smallBlind;

   public Player(int id, int stack) {
      this.id = id;
      this.stack = stack;
   }

   public void bet(int bet) {
      // TODO need to handle case where bet exceeds stack, exception? shouldn't
      // be problem when we have a gui
      this.stack -= bet;
      this.potCommitment += bet;
      this.playerActed = true;

      System.out.println((this.id == 0 ? "Player " : "Bot ") + this.id + (bet == 0 ? " checks" : " bets " + bet));
   }

   public void blind(int blind) {
      this.stack -= blind;
      this.potCommitment += blind;

      System.out.println((this.id == 0 ? "Player " : "Bot ") + this.id + (bigBlind ? " big blind" : " small blind"));
   }
}
