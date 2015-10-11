public class Player {
   int playerId;
   Hand hand;
   int stack;
   boolean inHand;

   public Player(int id, int stack) {
      this.playerId = id;
      this.stack = stack;
   }

   public void bet(int bet) {
      // TODO need to handle case where bet exceeds stack, exception? shouldn't
      // be problem when we have a gui
      this.stack -= bet;
      System.out.println("Player " + this.playerId
            + (bet == 0 ? " checks" : " bets " + bet));
   }
}
