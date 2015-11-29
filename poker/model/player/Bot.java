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
   private int opponentStackSize;
   private Profile profile;
   private double opponentOverallAggression;
   private double opponentShortTermAggression;

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

   // values somewhat correspond to Action
   // 0 = CheckCall, 1 = Bet/CheckCall, 2 = CheckCall/Fold

   // only used with a Hand of one pair
   int[][] checkPairValuesRaiseOnePair = new int[][] {    
         { 1, 1, 0, 2, 2, 2 },  // 0   number of pairs on board
         { 0, 0, 0, 2,-1,-1 },  // 1   -1 indicates invalid
   };
        // 0, 1, 2, 3, 4, 5   number of overcards to hand

   // only used with a Hand of one pair
   int[][] checkPairValuesNoRaiseOnePair = new int[][] {    
         { 1, 1, 1, 0, 0, 0 },  // 0   number of pairs on board
         { 1, 0, 0, 0,-1,-1 },  // 1   -1 indicates invalid
   };
        // 0, 1, 2, 3, 4, 5   number of overcards to hand

   // only used with a Hand of two pair
   int[][] checkPairValuesRaiseTwoPair = new int[][] {    
         { 1, 1, 1, 1 },  // 0      number of pairs on the board
         { 1, 1, 0, 2 },  // 1      -1 indicates invalid
         { 0, 0,-1,-1 }   // 2    
   };
        // 0, 1, 2, 3     number of overcards to hand

   // only used with a Hand of two pair
   int[][] checkPairValuesNoRaiseTwoPair = new int[][] {    
         { 1, 1, 1, 1 },  // 0      number of pairs on board
         { 1, 1, 1, 0 },  // 1      -1 indicates invalid
         { 1, 0,-1,-1 }   // 2    
   };
        // 0, 1, 2, 3     number of overcards to hand

   public Bot(int position, int stack) {
      super(position, stack);
      this.opponentStackSize = 0;
      this.profile = Profile.NORMAL;
      this.opponentOverallAggression = 0.0;
      this.opponentShortTermAggression = 0.0;
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
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 5.5 / this.holeCardsValue, bbAmount)));
            }
            else {
               this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
            }
         }
         else if (this.isSmallBlind()) {
            if (this.holeCardsValue <= 2) {
               this.setBotTurn(new Turn(Action.BET, randomizeBet(currentBet * 4 / this.holeCardsValue, bbAmount)));
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
      boolean isDealer = this.isDealerButton();

      int betAmount = totalBet - this.getTotalBet();
      int potSize = dealer.getPot();
      int numRemainingCards = dealer.getDeck().size();
      int minBet = dealer.getBigBlindAmount();
      int pairData[] = new int[1];

      int cardsThatWillMakeHand = (this.getCurrentHand().flushDraw == true) ? 9 : 0;
      cardsThatWillMakeHand += getStraightCards();

      System.out.println("Cards needed to make hand: " + cardsThatWillMakeHand);

      double odds = cardsThatWillMakeHand / (numRemainingCards - cardsThatWillMakeHand), divisor = 0, numBigBlinds = 0;
      double basebet = 0;
      double betPercentageOfPot = betAmount / (double)(potSize - betAmount);
      double overallThresh = 0.0, shortTermThresh = 0.0, percentageThresh = 0.0;
      double raiseIfOdds = 0.0, raiseIfNoOdds = 0.0;

      Random rand = new Random();

      System.out.println("bet percentage of Pot: " + betPercentageOfPot);
      System.out.println("raise: " + raise + ", betAmount: " + betAmount + ", currentBet: " + currentBet);

      if (raise) {
         System.out.println("Raise");
         if (this.getCurrentHand().hand.getValue() > 3) {
            if (!isDealer) {
               if (this.opponentOverallAggression < 1 || this.opponentShortTermAggression < 1.5) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (betPercentageOfPot < 0.6) ? 1.75 : 1.25;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else {
               if (this.opponentOverallAggression < 0.5 || this.opponentShortTermAggression < 0.8) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (betPercentageOfPot < 0.5) ? 1.25 : 1.05;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
         }
         else {
            if (this.getCurrentHand().hand.getValue() == 3) {
               if(!isDealer) {
                  if (this.opponentOverallAggression < 0.75 || this.opponentShortTermAggression < 1 || odds == 0.0) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount)); 
                  }
                  else {
                     divisor = (odds > 0.0) ? 1.75 : 1.5;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3 / divisor, minBet))); 
                  }
               }
               else {
                  if (this.opponentOverallAggression < 0.8 || this.opponentShortTermAggression < 1.2) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                  }
                  else {
                     divisor = (odds > 0) ? 1.5 : 1.8;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3 / divisor, minBet))); 
                  }
               }
            }
            else {
               pairData = checkPairs(this.getCurrentHand().currentBoard);
               if (this.getCurrentHand().hand.getValue() == 2) {
                  switch (checkPairValuesRaiseTwoPair[pairData[0]][pairData[1]]) {
                  case 0:
                     overallThresh    = (!isDealer) ? 0.55 : 0.45;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.70 : 0.60;
                     percentageThresh = (!isDealer) ? 0.80 : 0.90;

                     if ((this.opponentOverallAggression > overallThresh || this.opponentShortTermAggression > shortTermThresh)) {
                        if (betPercentageOfPot < odds || (odds == 0.0 && betPercentageOfPot < percentageThresh)) {
                           this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                        }
                        else {
                           this.setBotTurn(new Turn(Action.FOLD, 0));
                        }   
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                     
                  case 1:
                     overallThresh    = (!isDealer) ? 0.75 : 0.65;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.65 : 0.60;
                     raiseIfOdds      = (!isDealer) ? 1.20 : 1.10;   
                     raiseIfNoOdds    = (!isDealer) ? 1.60 : 1.50;   

                     if ((this.opponentOverallAggression < overallThresh || this.opponentShortTermAggression < shortTermThresh)) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        divisor = (odds > 0.0) ? raiseIfOdds : raiseIfNoOdds;
                        this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3.5 / divisor, minBet))); 
                     }                   
                     break;
                  case 2:
                     if (betPercentageOfPot < odds) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                          
                  default:
                     break;
                  }
               }
               else if (this.getCurrentHand().hand.getValue() == 1) {
                  switch (checkPairValuesRaiseOnePair[pairData[0]][pairData[1]]) {
                  case 0:
                     overallThresh    = (!isDealer) ? 0.75 : 0.65;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.60 : 0.55;
                     percentageThresh = (!isDealer) ? 0.60 : 0.750;

                     if (this.opponentOverallAggression > overallThresh || this.opponentShortTermAggression > shortTermThresh) {
                        if (betPercentageOfPot < odds || (odds == 0.0 && betPercentageOfPot < percentageThresh)) {
                           this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                        }
                        else {
                           this.setBotTurn(new Turn(Action.FOLD, 0));
                        }   
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                     
                  case 1:
                     overallThresh    = (!isDealer) ? 0.55 : 0.45;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.75 : 0.60;
                     raiseIfOdds      = (!isDealer) ? 1.20 : 1.05;   
                     raiseIfNoOdds    = (!isDealer) ? 1.50 : 1.40;   

                     if (this.opponentOverallAggression < overallThresh || this.opponentShortTermAggression < shortTermThresh) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        divisor = (odds > 0.0) ? raiseIfOdds : raiseIfNoOdds;
                        this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3 / divisor, minBet))); 
                     }                   
                     break;
                  case 2:
                     if (betPercentageOfPot < odds) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                          
                  default:
                     break;
                  }
               }
               else {
                  if (betPercentageOfPot < odds) {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                  }
                  else {
                     this.setBotTurn(new Turn(Action.FOLD, 0));
                  }
               }
            }
         }
      }
      else {   // no raise
         if (this.getCurrentHand().hand.getValue() > 3) {
            if (!isDealer) {
               if (this.opponentOverallAggression > 1.25 || this.opponentShortTermAggression > 1.5) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (this.getCurrentHand().hand.getValue() > 4) ? 1.9 : 1.5;
                  basebet = (potSize / minBet < 3) ? 2 * minBet: potSize / 3;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               }
            }
            else {
               if (this.opponentOverallAggression > 2 || this.opponentShortTermAggression > 3) {
                  this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
               }
               else {
                  divisor = (this.getCurrentHand().hand.getValue() > 4) ? 1.15 : 1.00;
                  basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 1.9;
                  this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * this.getCurrentHand().hand.getValue() / divisor, minBet)));
               } 
            }
         }
         else {
            if (this.getCurrentHand().hand.getValue() == 3) {

               if(!isDealer) {
                  if (this.opponentOverallAggression < 0.70 || this.opponentShortTermAggression < 0.6) {
                     divisor = (odds > 0.0) ? 1.3 : 1.6;
                     basebet = (potSize / minBet < 3) ? 2 * minBet: potSize / 2.4;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.7 / divisor, minBet))); 
                  }
                  else {
                     divisor = (odds > 0.0) ? 1.5 : 1.75;
                     basebet = (potSize / minBet < 3) ? 2 * minBet: potSize / 3;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 3 / divisor, minBet)));  
                  }
               }
               else {
                  if (this.opponentOverallAggression < 0.50 || this.opponentShortTermAggression < 0.60) {
                     divisor = (odds > 0.0) ? 1.1 : 1.4;
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 2.6;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 3 / divisor, minBet))); 
                  }
                  else {
                     divisor = (odds > 0.0) ? 1.4 : 1.7;
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 3;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 2.8 / divisor, minBet))); 
                  }
               }
            }
            else {
               pairData = checkPairs(this.getCurrentHand().currentBoard);
               if (this.getCurrentHand().hand.getValue() == 2) {
                  switch (checkPairValuesNoRaiseTwoPair[pairData[0]][pairData[1]]) {
                  case 0:
                     overallThresh    = (!isDealer) ? 0.55 : 0.45;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.70 : 0.60;
                     percentageThresh = (!isDealer) ? 0.80 : 0.90;

                     if ((this.opponentOverallAggression > overallThresh || this.opponentShortTermAggression > shortTermThresh)) {
                        if (betPercentageOfPot < odds || (odds == 0.0 && betPercentageOfPot < percentageThresh)) {
                           this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                        }
                        else {
                           this.setBotTurn(new Turn(Action.FOLD, 0));
                        }   
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                     
                  case 1:
                     overallThresh    = (!isDealer) ? 0.75 : 0.65;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.65 : 0.60;
                     raiseIfOdds      = (!isDealer) ? 1.20 : 1.10;   
                     raiseIfNoOdds    = (!isDealer) ? 1.60 : 1.50;   

                     if ((this.opponentOverallAggression < overallThresh || this.opponentShortTermAggression < shortTermThresh)) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        divisor = (odds > 0.0) ? raiseIfOdds : raiseIfNoOdds;
                        basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 4;
                        this.setBotTurn(new Turn(Action.BET, randomizeBet(betAmount * 3.5 / divisor, minBet))); 
                     }                   
                     break;                          
                  default:
                     break;
                  }
               }
               else if (this.getCurrentHand().hand.getValue() == 1) {
                  switch (checkPairValuesNoRaiseOnePair[pairData[0]][pairData[1]]) {
                  case 0:
                     overallThresh    = (!isDealer) ? 0.75 : 0.65;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.60 : 0.55;
                     percentageThresh = (!isDealer) ? 0.60 : 0.750;

                     if (this.opponentOverallAggression > overallThresh || this.opponentShortTermAggression > shortTermThresh) {
                        if (betPercentageOfPot < odds || (odds == 0.0 && betPercentageOfPot < percentageThresh)) {
                           this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                        }
                        else {
                           this.setBotTurn(new Turn(Action.FOLD, 0));
                        }   
                     }
                     else {
                        this.setBotTurn(new Turn(Action.FOLD, 0));
                     }   
                     break;                     
                  case 1:
                     overallThresh    = (!isDealer) ? 0.55 : 0.45;     //under the gun, on the button
                     shortTermThresh  = (!isDealer) ? 0.75 : 0.60;
                     raiseIfOdds      = (!isDealer) ? 1.20 : 1.05;   
                     raiseIfNoOdds    = (!isDealer) ? 1.50 : 1.40;   

                     if (this.opponentOverallAggression < overallThresh || this.opponentShortTermAggression < shortTermThresh) {
                        this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));  
                     }
                     else {
                        divisor = (odds > 0.0) ? raiseIfOdds : raiseIfNoOdds;
                        basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 4;
                        this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet * 3 / divisor, minBet))); 
                     }                   
                     break;                         
                  default:
                     break;
                  }
               }
               else {
                  if (pairData[0] == 0 && isDealer) {
                     basebet = (potSize / minBet < 3) ? 2 * minBet : potSize / 3;
                     this.setBotTurn(new Turn(Action.BET, randomizeBet(basebet, minBet))); 
                  }
                  else {
                     this.setBotTurn(new Turn(Action.CHECKCALL, betAmount));
                  }
               }
            }  
         }
      }
   }

   public Turn getBotTurn() {
      return botTurn;
   }

   public void setBotTurn(Turn botTurn) {
      this.botTurn = botTurn;
   }

   public void getOpponentData(ArrayList<Player> players) {
      double aggression = 0.0;     
      int decisions = 0;    
      
      for (Player player : players) {
         if (player instanceof Bot == false) {
            this.opponentStackSize = player.getStack();
            if (player.stats.overallDecisions > 0) {
               this.opponentOverallAggression = player.stats.overallAggression / player.stats.overallDecisions;
            }
            
            for (int i = 0; i < 10; i++) {
               aggression += player.stats.shortTermAggression[i];
               decisions += player.stats.shortTermDecisions[i];
            }
            this.opponentShortTermAggression = aggression / decisions;
         }
      }

      System.out.println("Oppo. stack = " + this.opponentStackSize + ", overall agg. = " + this.opponentOverallAggression + 
         ", short term agg.  = " + this.opponentShortTermAggression);
   }

   
   public int randomizeBet(double bet, int minBet) {
	  Random rand = new Random();
	  int max = (int)(bet * 1.5);
	  int min = (int)(bet * 0.5);
	  
	  int randBet = rand.nextInt((max - min) + 1) + min;
     int newBet = (int)(Math.ceil(randBet / (double)minBet) * minBet);

     return (newBet > this.getStack()) ? this.getStack() : newBet;
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
      
      int pairStats[] = new int[] {0, 0};    //pairs on board, overCards to 1st pair, overCards to 2nd pair
      int tallies[] = new int[15];
      int handValue = this.getCurrentHand().hand.getValue();
      ArrayList<Integer> kickers = this.getCurrentHand().getKickers();

      for (Card c : board) {
         int val = c.getRank().getValue();
         if (tallies[val] == 1 || (handValue == 0 && val == kickers.get(0))) {
            pairStats[0] += 1;
         }
         else {
            if (handValue == 2 && (val > kickers.get(0) || (val > kickers.get(1) && val != kickers.get(0)))) {  
               pairStats[1] += 1;
            }   
            else if (handValue < 2 && val > kickers.get(0)) {
               pairStats[1] += 1;
            }
         }
         tallies[val] += 1;
      }
      System.out.print("Pair Stats: ");
      if (handValue == 0) {
         System.out.print("High Card is on Board: ");
      }
      else {
         System.out.print("Number of pairs on Board: ");
      }
      System.out.println("" + pairStats[0] + ", overcards to current Hand: " + pairStats[1]);     
      
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
