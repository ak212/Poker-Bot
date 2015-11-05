package poker.model.player;

import poker.model.game.Dealer;

public class Bot extends Player {
   int holeCardsValue;
   private BotTurn botTurn;

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
         { 0, 0, 5, 4, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1 } // A
   };
        // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10, J, Q, K, A

   public Bot(int position, int stack) {
      super(position, stack);
   }

   public void evalHoleCards() {
      int idx1 = 0, idx2 = 0, temp = 0;

      idx1 = Math.max(this.getHoleCards().getCard1().getRank().getValue(), this.getHoleCards().getCard2().getRank().getValue());
      idx2 = Math.min(this.getHoleCards().getCard1().getRank().getValue(), this.getHoleCards().getCard2().getRank().getValue());

      if (this.getHoleCards().getCard1().getSuit().getValue() != this.getHoleCards().getCard2().getSuit().getValue()) {
         temp = idx2;
         idx2 = idx1;
         idx1 = temp;
      }

      this.holeCardsValue = holeCardValues[idx1][idx2];
   }

   public void action(int currentBet) {
      this.setBotTurn(new BotTurn(Action.CHECKCALL, currentBet));
   }

   public void determinePreFlopAction(int currentBet, int totalBet, int bbAmount) {
      boolean raise;

      if (this.isSmallBlind() && !this.isCalledSB()) {
         raise = totalBet != bbAmount;
      }
      else {
         raise = totalBet > this.getTotalBet();
      }

      int betAmount = totalBet - this.getTotalBet();

      if (!raise) {
         if (this.isBigBlind()) {
            if (this.holeCardsValue <= 3) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (7 / this.holeCardsValue)));
            }
            else {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 2) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (6 / this.holeCardsValue)));
               this.setCalledSB(true);
            }
            else if (this.holeCardsValue <= 7) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
               this.setCalledSB(true);
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
      }
      else {
         if (this.isBigBlind()) {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue)));
               this.setCalledSB(true);
            }
            else if (this.holeCardsValue <= 4) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
               this.setCalledSB(true);
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 3) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
      }
   }

   public void determinePostFlopAction(int currentBet, int totalBet, Dealer dealer) {
      boolean raise = totalBet > this.getTotalBet();
      int betAmount = totalBet - this.getTotalBet();
      int numPlayers = dealer.getPlayersInHand();
      double positionDivider = (numPlayers == 2) ? 1.5 : numPlayers / 3;

      //need to have information about current hand, current bet, position, relative stack size
      int seatPosition = (this.getPosition() <= positionDivider) ? 1 : (this.getPosition() <= positionDivider * 2) ? 2 : 3;

      if (!raise) {
         if (seatPosition == 1) { // Early position
            if (this.getPosition() == 1) {   // Under the Gun
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (7 / this.holeCardsValue)));
            }
            else {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
         }
         else if (seatPosition == 2) {   // Middle position
            if (this.holeCardsValue <= 2) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (6 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 7) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else {   // Late Position
            if (this.getPosition() == numPlayers) {   // On the Button
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
      }
      else {
         if (seatPosition == 1) {   // Early position
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else if (seatPosition == 2) {   // Middle Position
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 4) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
         else {   // Late Position
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new BotTurn(Action.BET, currentBet * (2 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 3) {
               this.setBotTurn(new BotTurn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new BotTurn(Action.FOLD, currentBet));
            }
         }
      }
   }

   public BotTurn getBotTurn() {
      return botTurn;
   }

   public void setBotTurn(BotTurn botTurn) {
      this.botTurn = botTurn;
   }
}
