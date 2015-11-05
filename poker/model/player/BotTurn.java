package poker.model.player;


public class BotTurn {
   private Action botAction;
   private int betAmount;

   public BotTurn(Action action, int bet) {
      this.setBotAction(action);
      this.setBetAmount(bet);
   }

   public Action getBotAction() {
      return botAction;
   }

   public void setBotAction(Action botAction) {
      this.botAction = botAction;
   }

   public int getBetAmount() {
      return betAmount;
   }

   public void setBetAmount(int betAmount) {
      this.betAmount = betAmount;
   }
}
