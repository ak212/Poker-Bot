package poker.model.player;

import poker.Main;
import poker.model.cards.HoleCards;
import poker.model.hand.HandStrength;

public class Player {
   private int id;
   private int position;
   private int preFlopPosition;
   private HoleCards holeCards;
   private HandStrength currentHand;
   private int stack;
   int potCommitment;
   private int totalBet;
   private boolean playerActed;
   private boolean inHand;
   private boolean allIn;
   private boolean dealerButton;
   private boolean bigBlind;
   private boolean smallBlind;
   private boolean calledSB;
   public PlayerStats stats;
   Main mainApp;

   public Player(int id, int stack) {
      this.setId(id);
      this.setStack(stack);
      this.stats = new PlayerStats();
   }

   public void bet(int bet) {
      if (bet > this.getStack()) {
         bet = this.getStack();
      }

      this.setStack(this.getStack() - bet);
      this.potCommitment += bet;
      this.setTotalBet(this.getTotalBet() + bet);
      this.setPlayerActed(true);

      if (this.getStack() == 0) {
         this.setAllIn(true);
      }

      if (this instanceof Bot) {
         System.out.println("Bot " + this.getId() + (bet == 0 ? " checks" : " bets " + bet));
         mainApp.updateConsole("Bot " + this.getId() + (bet == 0 ? " checks" : " bets " + bet));
      }
      else {
         System.out.println("Player " + this.getId() + (bet == 0 ? " checks" : " bets " + bet));
         mainApp.updateConsole("Player " + this.getId() + (bet == 0 ? " checks" : " bets " + bet));
      }
   }

   public void call(int call) {
      this.setStack(this.getStack() - call);
      this.potCommitment += call;
      this.setTotalBet(this.getTotalBet() + call);
      this.setPlayerActed(true);

      if (this instanceof Bot) {
         System.out.println("Bot " + this.getId() + (call == 0 ? " checks " : (" calls " + call)));
         mainApp.updateConsole("Bot " + this.getId() + (call == 0 ? " checks " : (" calls " + call)));
      }
      else {
         System.out.println("Player " + this.getId() + (call == 0 ? " checks " : (" calls " + call)));
         mainApp.updateConsole("Player " + this.getId() + (call == 0 ? " checks " : (" calls " + call)));
      }
   }

   public void blind(int blind) {
      this.setStack(this.getStack() - blind);
      this.potCommitment += blind;
      this.setTotalBet(this.getTotalBet() + blind);

      if (this instanceof Bot) {
         System.out.println("Bot " + this.getId() + (isBigBlind() ? " big blind" : " small blind"));
         mainApp.updateBetAmountOne(Integer.toString(blind));
      }
      else {
         System.out.println("Player " + this.getId() + (isBigBlind() ? " big blind" : " small blind"));
         mainApp.updateBetAmountZero(Integer.toString(blind));
      }
   }

   public void nextHand() {
      this.potCommitment = 0;
      this.setPreFlopPosition(0);
      this.setPosition(0);
      this.setBigBlind(false);
      this.setDealerButton(false);
      this.setSmallBlind(false);
      this.setCalledSB(false);
      this.setAllIn(false);
   }

   public boolean isAllIn() {
      return allIn;
   }

   public void setAllIn(boolean allIn) {
      this.allIn = allIn;
   }

   public void printHoleCards() {
      System.out.println(this.holeCards.getCard1().shorten() + this.holeCards.getCard2().shorten());
      // mainApp.updateConsole(this.holeCards.getCard1().shorten() + this.holeCards.getCard2().shorten());
   }

   public HandStrength getCurrentHand() {
      return currentHand;
   }

   public void setCurrentHand(HandStrength currentHand) {
      this.currentHand = currentHand;
   }

   public int getTotalBet() {
      return totalBet;
   }

   public void setTotalBet(int totalBet) {
      this.totalBet = totalBet;
   }

   public boolean isCalledSB() {
      return calledSB;
   }

   public void setCalledSB(boolean calledSB) {
      this.calledSB = calledSB;
   }

   public boolean isSmallBlind() {
      return smallBlind;
   }

   public void setSmallBlind(boolean smallBlind) {
      this.smallBlind = smallBlind;
   }

   public boolean isBigBlind() {
      return bigBlind;
   }

   public void setBigBlind(boolean bigBlind) {
      this.bigBlind = bigBlind;
   }

   public boolean isInHand() {
      return inHand;
   }

   public void setInHand(boolean inHand) {
      this.inHand = inHand;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getStack() {
      return stack;
   }

   public void setStack(int stack) {
      this.stack = stack;
   }

   public boolean isDealerButton() {
      return dealerButton;
   }

   public void setDealerButton(boolean dealerButton) {
      this.dealerButton = dealerButton;
   }

   public int getPosition() {
      return position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public int getPreFlopPosition() {
      return preFlopPosition;
   }

   public void setPreFlopPosition(int preFlopPosition) {
      this.preFlopPosition = preFlopPosition;
   }

   public boolean isPlayerActed() {
      return playerActed;
   }

   public void setPlayerActed(boolean playerActed) {
      this.playerActed = playerActed;
   }

   public HoleCards getHoleCards() {
      return holeCards;
   }

   public void setHoleCards(HoleCards holeCards) {
      this.holeCards = holeCards;
   }

   public void setMainApp(Main mainApp) {
      this.mainApp = mainApp;
   }
}
