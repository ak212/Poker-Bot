package poker;

public class Bot extends Player {
   int holeCardsValue;
   BotTurn botTurn;

   // Unsuited: start with column
   // Suited: start with row
   int[][] holeCardValues = new int[][] {
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 0
         { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // 1
         { 0, 0, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 5 }, // 2
         { 0, 0, 8, 6, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 5 }, // 3
         { 0, 0, 8, 8, 5, 8, 8, 8, 8, 8, 8, 8, 8, 6, 5 }, // 4
         { 0, 0, 8, 8, 8, 4, 8, 8, 8, 8, 8, 8, 7, 6, 4 }, // 5
         { 0, 0, 8, 8, 8, 8, 3, 8, 8, 8, 8, 8, 7, 6, 4 }, // 6
         { 0, 0, 8, 8, 8, 8, 8, 2, 8, 8, 8, 7, 6, 5, 4 }, // 7
         { 0, 0, 8, 8, 8, 8, 8, 8, 2, 8, 7, 7, 6, 5, 4 }, // 8
         { 0, 0, 8, 8, 8, 8, 8, 8, 7, 2, 6, 6, 5, 4, 3 }, // 9
         { 0, 0, 8, 8, 8, 8, 8, 7, 6, 6, 1, 5, 5, 4, 3 }, // 10
         { 0, 0, 8, 8, 8, 7, 7, 6, 6, 5, 4, 1, 4, 3, 3 }, // J
         { 0, 0, 7, 7, 6, 6, 6, 6, 5, 4, 4, 4, 1, 3, 3 }, // Q
         { 0, 0, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 1, 2 }, // K
         { 0, 0, 5, 4, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1 }  // A
   };
        // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10, J, Q, K, A

   public Bot(int position, int stack) {
      super(position, stack);
   }

   public void evalHoleCards() {
      int idx1 = 0, idx2 = 0, temp = 0;

      idx1 = Math.max(this.holeCards.card1.rank.getValue(), this.holeCards.card2.rank.getValue());
      idx2 = Math.min(this.holeCards.card1.rank.getValue(), this.holeCards.card2.rank.getValue());

      if (this.holeCards.card1.suit.getValue() != this.holeCards.card2.suit.getValue()) {
         temp = idx2;
         idx2 = idx1;
         idx1 = temp;
      }

      this.holeCardsValue = holeCardValues[idx1][idx2];
   }

   public void action(int currentBet) {
      this.botTurn = new BotTurn(Action.CHECKCALL, currentBet);
   }

   public void determinePreFlopAction(int currentBet, int totalBet) {
      boolean raise = totalBet > this.totalBet;
      int betAmount = totalBet - this.totalBet;

      if (!raise) {
         if (this.bigBlind) {
	    if (this.holeCardsValue <= 3) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (7 / this.holeCardsValue));	
	    }
	    else {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
	    }
         }
         else if (this.smallBlind) {
	    if (this.holeCardsValue <= 2) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (6 / this.holeCardsValue));	
	    }
            else if (this.holeCardsValue <= 7) {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
            }
            else {
               this.botTurn = new BotTurn(Action.FOLD, currentBet);
            }
         }
         else {
	    if (this.holeCardsValue <= 1) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue));	
	    }
            else if (this.holeCardsValue <= 5) {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
            }
            else {
               this.botTurn = new BotTurn(Action.FOLD, currentBet);
            }
         }
      }
      else {
         if (this.bigBlind) {
	    if (this.holeCardsValue <= 1) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue));	
	    }	
            else if (this.holeCardsValue <= 5) {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
            }
            else {
               this.botTurn = new BotTurn(Action.FOLD, currentBet);
            }
         }
         else if (this.smallBlind) {
	    if (this.holeCardsValue <= 1) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue));	
	    }
            else if (this.holeCardsValue <= 4) {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
            }
            else {
               this.botTurn = new BotTurn(Action.FOLD, currentBet);
            }
         }
         else {
	    if (this.holeCardsValue <= 1) {
	       this.botTurn = new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue));	
	    }
            else if (this.holeCardsValue <= 3) {
               this.botTurn = new BotTurn(Action.CHECKCALL, betAmount);
            }
            else {
               this.botTurn = new BotTurn(Action.FOLD, currentBet);
            }
         }
      }
   }
}
