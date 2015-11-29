package poker.model.player;

import poker.model.hand.Hand;

public class PlayerStats {
   public int hands;
   public int wins;
   public int folds;
   public int losses;
   public int preFlopFolds;
   public int flopFolds;
   public int turnFolds;
   public int riverFolds;
   public int raises;
   public double averageWinningPerHand;
   public int winnings;
   public Hand bestHand;
   public int biggestWin;
   public int overallDecisions;
   public double overallAggression;
   public int[] shortTermDecisions;
   public double[] shortTermAggression;

   public PlayerStats() {
      this.hands = 0;
      this.wins = 0;
      this.folds = 0;
      this.losses = 0;
      this.preFlopFolds = 0;
      this.flopFolds = 0;
      this.turnFolds = 0;
      this.riverFolds = 0;
      this.raises = 0;
      this.averageWinningPerHand = 0;
      this.winnings = 0;
      this.bestHand = Hand.NoHand;
      this.biggestWin = 0;
      this.overallDecisions = 0;
      this.overallAggression = 1.0;
      this.shortTermDecisions = new int[10];
      this.shortTermAggression = new double[10];
   }

   public void setWinnings(int num) {
      this.winnings += num;
      this.averageWinningPerHand = this.winnings / this.wins;
   }
}
