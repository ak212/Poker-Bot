package poker;

public class Player {
   int id;
   int position;
   int preFlopPosition;
   HoleCards holeCards;
   Hand currentHand;
   int stack;
   int potCommitment;
   int totalBet;
   boolean playerActed;
   boolean inHand;
   boolean dealerButton;
   boolean bigBlind;
   boolean smallBlind;
   boolean calledSB;

   public Player(int id, int stack) {
      this.id = id;
      this.stack = stack;
   }

   public void bet(int bet) {
      // TODO need to handle case where bet exceeds stack, exception? shouldn't
      // be problem when we have a gui
      this.stack -= bet;
      this.potCommitment += bet;
      this.totalBet += bet;
      this.playerActed = true;

      if (this instanceof Bot) {
         System.out.println("Bot " + this.id + (bet == 0 ? " checks" : " bets " + bet));
      }
      else {
         System.out.println("Player " + this.id + (bet == 0 ? " checks" : " bets " + bet));
      }
   }

   public void call(int call) {
      this.stack -= call;
      this.potCommitment += call;
      this.totalBet += call;
      this.playerActed = true;

      if (this instanceof Bot) {
         System.out.println("Bot " + this.id + (call == 0 ? " checks " : (" calls " + call)));
      }
      else {
         System.out.println("Player " + this.id + (call == 0 ? " checks " : (" calls " + call)));
      }
   }

   public void blind(int blind) {
      this.stack -= blind;
      this.potCommitment += blind;
      this.totalBet += blind;

      if (this instanceof Bot) {
         System.out.println("Bot " + this.id + (bigBlind ? " big blind" : " small blind"));
      }
      else {
         System.out.println("Player " + this.id + (bigBlind ? " big blind" : " small blind"));
      }
   }

   public void nextHand() {
      this.potCommitment = 0;
      this.preFlopPosition = 0;
      this.position = 0;
      this.bigBlind = false;
      this.dealerButton = false;
      this.smallBlind = false;
      this.calledSB = false;
   }
}
