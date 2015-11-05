package poker.model.player;

import poker.model.hand.Hand;

public class PlayerStats {
   int hands;
   int wins;
   int folds;
   int losses;
   int preFlopFolds;
   int flopFolds;
   int turnFolds;
   int riverFolds;
   int raises;
   int averageWinningPerHand;
   Hand bestHand;
   int biggestWin;

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
      this.bestHand = Hand.NoHand;
      this.biggestWin = 0;
   }
}
