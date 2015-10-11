public class Player {
   int playerId;
   Hand hand;
   int stack;

   public Player(int id, int stack) {
      this.playerId = id;
      this.stack = stack;
   }

   public void bet(int bet) {
      this.stack -= bet;
   }
}
