package poker.model.player;


public class Turn {
   private Action action;
   private int betAmount;

   public Turn(Action action, int bet) {
      this.setAction(action);
      this.setBetAmount(bet);
   }

   public Action getAction() {
      return action;
   }

   public void setAction(Action botAction) {
      this.action = botAction;
   }

   public int getBetAmount() {
      return betAmount;
   }

   public void setBetAmount(int betAmount) {
      this.betAmount = betAmount;
   }
}
