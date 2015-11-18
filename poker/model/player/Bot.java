package poker.model.player;

import java.util.ArrayList;
import java.util.Random;
import poker.model.cards.Card;
import poker.model.cards.Suit;
import poker.model.cards.Rank;

import poker.model.game.Dealer;

public class Bot extends Player {
   int holeCardsValue;
   private Turn botTurn;
   private ArrayList<Integer> opponentStackSizes;
   private Profile profile;

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
      this.profile = Profile.NORMAL;
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 7 / this.holeCardsValue, bbAmount)));
            }
            else {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 2) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 6 / this.holeCardsValue, bbAmount)));
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 3 / this.holeCardsValue, bbAmount)));
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 3 / this.holeCardsValue, bbAmount)));
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 2 / this.holeCardsValue, bbAmount)));
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 2 / this.holeCardsValue, bbAmount)));
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
      int potSize = dealer.getPot();

      int numPlayers = dealer.getPlayersInHand();
      int playersBehind = numPlayers - this.getPosition();
      int playersInFront = numPlayers - playersBehind - 1;
      int numRemainingCards = dealer.getDeck().size();
      int minBet = dealer.getBigBlindAmount();
      int checkPairs[] = new int[2];

      int cardsThatWillMakeHand = (this.getCurrentHand().flushDraw == true) ? 9 : 0;
      cardsThatWillMakeHand += getStraightCards();

      double odds = cardsThatWillMakeHand / numRemainingCards, divisor = 0, numBigBlinds = 0;
      double basebet = 0;

      Random rand = new Random();
      
      // this.opponentStackSizes is available
      
      /* factors to consider:
       - if there is a raise and how large it is relative to size of pot
       - position of the Bot
       - stack size of the Bot relative to other players
       - what type of hand the bot has:
          - if the bot has a hand, how strong is it relative to the board (using HandEvaluator)
             - if the pair is on the board or in hand, number of relevant overcards, draws (straight, flush) -> (4 cards needed)
      */

      double betPercentageOfPot = betAmount / (double)(potSize - betAmount);
      System.out.println("bet percentage: " + betPercentageOfPot);

      /*System.out.println("bet %: " + betPercentageOfPot);
      System.out.println("flushDraw?: " + this.getCurrentHand().flushDraw);
      System.out.println("gutshot?: " + this.getCurrentHand().gutshotStraightDraw);
      System.out.println("openended?: " + this.getCurrentHand().openendedStraightDraw);
      System.out.println("openendedhighcard: " + this.getCurrentHand().openendedStraightDrawHighCard);
      System.out.println("numcardsonboard?: " + this.getCurrentHand().currentBoard.size());*/

      System.out.println("raise: " + raise + ", betAmount: " + betAmount + ", currentBet: " + currentBet);
      System.out.println("players in front: " + playersInFront + ", players behind: " + playersBehind);

      checkPairs = checkPairs(this.getCurrentHand().currentBoard);

      if (raise) {
         System.out.println("Raise");
         if (playersInFront == 0) {   // Under the Gun
            if (this.getCurrentHand().hand.getValue() > 3) {
               if (rand.nextInt() % 4 == 0) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (betPercentageOfPot < 0.3) ? 1.75 : 1.25;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else if (this.getCurrentHand().hand.getValue() > 0) {
               if (this.getCurrentHand().hand.getValue() == 3) {
                  if (odds > 0.0 && betPercentageOfPot < 0.4) {
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3 / 1.75, minBet))); 
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
               else if (odds != 0) {
                  if (betPercentageOfPot < 0.6) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.FOLD, 0));
                  }
               }
               else {
                  if (betPercentageOfPot < 0.3) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.FOLD, 0));
                  }
               }
            }
            else {
               if (betPercentageOfPot < 0.2 && odds > 0.15) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  this.setBotTurn(new Turn(Action.FOLD, 0));
               }
            }
         }
         else if (playersBehind == 0) {   // On the Button
            if (this.getCurrentHand().hand.getValue() > 3) {
               if (rand.nextInt() % 10 == 0) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (betPercentageOfPot < 0.3) ? 1.25 : 1.05;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else if (this.getCurrentHand().hand.getValue() > 0) {
               if (this.getCurrentHand().hand.getValue() == 3) {
                  if (odds > 0.0 && betPercentageOfPot < 0.75) {
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3 / 1.5, minBet))); 
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
               else if (odds != 0) {
                  if (betPercentageOfPot < 0.8) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.FOLD, 0));
                  }
               }
               else {
                  if (betPercentageOfPot < 0.6) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.FOLD, 0));
                  }
               }
            }
            else {
               if (betPercentageOfPot < 0.4 && odds > 0.10) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  this.setBotTurn(new Turn(Action.FOLD, 0));
               }
            }
         }
         else {
            //use playersInfront and playersbehind
         }
      }
      else {
         if (playersInFront == 0) {   // Under the Gun
            if (this.getCurrentHand().hand.getValue() > 3) {
               if (rand.nextInt() % 5 == 0) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (this.getCurrentHand().hand.getValue() > 4) ? 1.9 : 1.5;
                  basebet = (potSize / minBet < 3) ? 2 * minBet: potSize / 1.8;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else if (this.getCurrentHand().hand.getValue() > 0) {
               if (this.getCurrentHand().hand.getValue() == 3) {
                  if (odds > 0.0) {
                     divisor = (odds > 0.2) ? 1.5 : 1.75;
                     basebet = (potSize / minBet < 3) ? 2 * minBet: potSize / 2.1;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 3 / divisor, minBet))); 
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
               else if (odds != 0) {
                  
                  if (this.getCurrentHand().hand.getValue() == 2) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 2.6;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 1.5, minBet)));
                  }
                  else if (this.getCurrentHand().hand.getValue() == 1) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 2.9;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 1.7, minBet)));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
               else {
                  if (this.getCurrentHand().hand.getValue() == 2) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 3;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 2.0, minBet)));
                  }
                  else if (this.getCurrentHand().hand.getValue() == 1){
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 3.5;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 2.2, minBet)));
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
            }
            else {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
            }
         }
         else if (playersBehind == 0) {   // On the Button
            if (this.getCurrentHand().hand.getValue() > 3) {
               if (rand.nextInt() % 7 == 0) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (odds < 0.4) ? 1.15 : 1.00;
                  basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 1.9;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else if (this.getCurrentHand().hand.getValue() > 0) {
               if (this.getCurrentHand().hand.getValue() == 3) {
                  if (odds > 0.0) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 2.2;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 3 / 1.3, minBet))); 
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
               }
               else {
                  if (this.getCurrentHand().hand.getValue() == 2) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 2.8;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 1.3, minBet)));
                  }
                  else {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 3.3;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.5 / 1.7, minBet)));
                  }
               }
            }
            else {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
            }
         }
         else {
            //use playersinfront and players behind
         }
      }

      //this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
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
   
   public int randomizeBet(double bet, int minBet) {
	  int max = (int)(bet * 1.5);
	  int min = (int)(bet * 0.5);
	  Random rand = new Random();
	  
	  int newBet = rand.nextInt((max - min) + 1) + min;
	
     return (int)(Math.ceil(newBet / (double)minBet) * minBet);
   }

   public int getStraightCards() {

      if (this.getCurrentHand().gutshotStraightDraw == true) {
         return (this.getCurrentHand().flushDraw) ? 3 : 4;
      }
      else if (this.getCurrentHand().openendedStraightDraw == true) {
         if (this.getCurrentHand().openendedStraightDrawHighCard == 14 ||
            this.getCurrentHand().openendedStraightDrawHighCard == 5) {
            return (this.getCurrentHand().flushDraw) ? 3 : 4;
         }
         return (this.getCurrentHand().flushDraw) ? 6 : 8;
      }
      return 0;
   } 

   // figure out how many hands are on the board, and the number of overcards
   public int[] checkPairs(ArrayList<Card> board) {
      
      int pairStats[] = new int[] {0, 0, 0};    //pairs on board, overCards to 1st pair, overCards to 2nd pair
      int tallies[] = new int[15];
      int handValue = this.getCurrentHand().hand.getValue();
      ArrayList<Integer> kickers = this.getCurrentHand().getKickers();

      for (Card c : board) {
         int val = c.getRank().getValue();
         if (tallies[val] == 1) {
            pairStats[0] += 1;
         }
         else {
            if (handValue == 2) {
               if (val > kickers.get(0)) {
                  pairStats[2] += 1;
                  pairStats[1] += 1;
               }
               else {
                  if (val > kickers.get(1) && val != kickers.get(0)) {
                     pairStats[1] += 1;
                  }
               }
            }
            else  {
               if (val > kickers.get(0)) {
                  pairStats[1] += 1;
               }
            }
         }
         tallies[val] += 1;
      }
      
      return pairStats;
   }
   
   public void changeProfile(Profile profile) {
      this.profile = profile;
      
      switch(this.profile) {
      case AGGRESSIVE:
         holeCardValues = new int[][] { 
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
         
         for (int i = 0; i < holeCardValues.length; i++) {
            for (int j = 0; j < holeCardValues[i].length; j++) {
               if (holeCardValues[i][j] > 1) {
                  holeCardValues[i][j] -= 1;
               }
            }
         }
         
         break;
      case NORMAL:
         holeCardValues = new int[][] { 
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
         break;
      case TIGHT:
         holeCardValues = new int[][] { 
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
         
         for (int i = 0; i < holeCardValues.length; i++) {
            for (int j = 0; j < holeCardValues[i].length; j++) {
               if (holeCardValues[i][j] < 8 && holeCardValues[i][j] > 0) {
                  holeCardValues[i][j] += 1;
               }
            }
         }
         
         break;
      }
   }
}
