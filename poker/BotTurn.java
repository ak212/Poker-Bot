package poker;

public class BotTurn {
   Action botAction;
   int betAmount;

   public BotTurn(Action action, int bet) {
      this.botAction = action;
      this.betAmount = bet;
   }
}
