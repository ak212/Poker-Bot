package poker.model.player;

import java.util.ArrayList;
import java.util.Random;

import poker.model.game.Dealer;

public class Bot extends Player {
   int holeCardsValue;
   private Turn botTurn;
   private ArrayList<Integer> opponentStackSizes;

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
      this.opponentStackSizes = new ArrayList<Integer>();
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

      // modify this value to change bot profile
      // i.e: if value > 1, value - 1 to simulate an aggressive player (add profile characteristic to bot)
      this.holeCardsValue = holeCardValues[idx1][idx2];
      //this.holeCardsValue = 1;
   }

   public void action(int currentBet) {
      this.setBotTurn(new Turn(Action.CHECKCALL, currentBet));
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
      
      System.out.println("raise: " + raise + ", betAmount: " + betAmount + ", currentBet: " + currentBet);

      if (!raise) {
         if (this.isBigBlind()) {
            if (this.holeCardsValue <= 3) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 7 / this.holeCardsValue)));
            }
            else {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 2) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 6 / this.holeCardsValue)));
               this.setCalledSB(true);
            }
            else if (this.holeCardsValue <= 7) {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
               this.setCalledSB(true);
            }
            else {
               this.setBotTurn(new Turn(Action.FOLD, currentBet));
            }
         }
         else {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new Turn(Action.FOLD, currentBet));
            }
         }
      }
      else {
         if (this.isBigBlind()) {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 3 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 5) {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new Turn(Action.FOLD, currentBet));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 2 / this.holeCardsValue)));
               this.setCalledSB(true);
            }
            else if (this.holeCardsValue <= 4) {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
               this.setCalledSB(true);
            }
            else {
               this.setBotTurn(new Turn(Action.FOLD, currentBet));
            }
         }
         else {
            if (this.holeCardsValue <= 1) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet, 2 / this.holeCardsValue)));
            }
            else if (this.holeCardsValue <= 3) {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
            else {
               this.setBotTurn(new Turn(Action.FOLD, currentBet));
            }
         }
      }
   }

   public void determinePostFlopAction(int currentBet, int totalBet, Dealer dealer) {

      boolean raise = totalBet > this.getTotalBet();
      int betAmount = totalBet - this.getTotalBet();
      int numPlayers = dealer.getPlayersInHand();
      int playersBehind = numPlayers - this.getPosition();
      int playersInFront = numPlayers - playersBehind - 1;
      int potSize = dealer.getPot();
      int numCardsLeft = dealer.getDeck().size();
      System.out.println("Num Cards Left: " + numCardsLeft);
      
      /* factors to consider:
       - if there is a raise and how large it is relative to size of pot
       - position of the Bot
       - stack size of the Bot relative to other players
       - what type of hand the bot has:
          - if the bot has a hand, how strong is it relative to the board (using HandEvaluator)
             - if the pair is on the board or in hand, number of relevant overcards, draws (straight, flush) -> (4 cards needed)
      */

      double betPercentageOfPot = betAmount / (double)(potSize - betAmount);
      System.out.println("bet %: " + betPercentageOfPot);
      
      /*if (!raise) {
         if (playersInFront == 0) {   // Under the Gun

         }
         else if (playersBehind == 0) {   // On the Button

         }
         else {
            //use playersInfront and playersbehind
         }
      }
      else {
         if (playersInFront == 0) {   // Under the Gun

         }
         else if (playersBehind == 0) {   // On the Button

         }
         else {
            //use playersinfront and playersbehind
         }
      }*/

      this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
   }

   public Turn getBotTurn() {
      return botTurn;
   }

   public void setBotTurn(Turn botTurn) {
      this.botTurn = botTurn;
   }

   public void getOpponentStackSizes(ArrayList<Player> players) {
      for (Player player : players) {
         if (player.isInHand()) {
            opponentStackSizes.add(player.getStack());
         }
      }
   }
   
   public int randomizeBet(int bet, int multiplier) {
	  int max = (int)(bet * multiplier * 1.5);
	  int min = (int)(bet * multiplier * 0.5);
	  Random rand = new Random();
	  
	  int newBet = rand.nextInt((max - min) + 1) + min;
	
     return (int)Math.ceil(newBet / (double)bet) * bet;
   }
}
