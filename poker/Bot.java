public class Bot {
   int playerPosition;
   HoleCards holeCards;
   int stack;
   boolean inHand;
   boolean dealerButton;
   boolean bigBlind;
   boolean smallBlind;

   // Unsuited: start with column
   // Suited: start with row
   int[][] holeCardValues = new int[][] {
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },	//0
      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },  //1
      { 0, 0, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 5 },  //2
      { 0, 0, 8, 6, 8, 8, 8, 8, 8, 8, 8, 8, 8, 7, 5 },  //3
      { 0, 0, 8, 8, 5, 8, 8, 8, 8, 8, 8, 8, 8, 6, 5 },  //4
      { 0, 0, 8, 8, 8, 4, 8, 8, 8, 8, 8, 8, 7, 6, 4 },  //5
      { 0, 0, 8, 8, 8, 8, 3, 8, 8, 8, 8, 8, 7, 6, 4 },  //6
      { 0, 0, 8, 8, 8, 8, 8, 2, 8, 8, 8, 7, 6, 5, 4 },  //7
      { 0, 0, 8, 8, 8, 8, 8, 8, 2, 8, 7, 7, 6, 5, 4 },  //8
      { 0, 0, 8, 8, 8, 8, 8, 8, 7, 2, 6, 6, 5, 4, 3 },  //9
      { 0, 0, 8, 8, 8, 8, 8, 7, 6, 6, 1, 5, 5, 4, 3 },  //10
      { 0, 0, 8, 8, 8, 7, 7, 6, 6, 5, 4, 1, 4, 3, 3 },  //J
      { 0, 0, 7, 7, 6, 6, 6, 6, 5, 4, 4, 4, 1, 3, 3 },  //Q
      { 0, 0, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 1, 2 },  //K
      { 0, 0, 5, 4, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1 }   //A	
   };
     // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10, J, Q, K, A

   public Bot(int position, int stack) {
      this.playerPosition = position;
      this.stack = stack;
   }

   public void bet(int bet) {
      // TODO need to handle case where bet exceeds stack, exception? shouldn't
      // be problem when we have a gui
      this.stack -= bet;

      System.out.println("Bot " + this.playerPosition + (bet == 0 ? " checks" : " bets " + bet));
   }

   public void blind(int blind) {
      this.stack -= blind;

      System.out.println("Bot " + this.playerPosition + (bigBlind ? " big blind" : " small blind"));

   }

   public int evalHoleCards(Holecards holecards) {
     
      int idx1 = 0, idx2 = 0, temp;

      idx1 = max(holecards.card1.rank, holecards.card2.rank);
      idx2 = min(holecards.card1.rank, holecards.card2.rank);
      
      if (holecards.card1.suit != holecards.card2.suit) {
	 idx1 = temp;
	 idx2 = idx1;
	 idx2 = temp;
      }

      return holeCardValues[idx1][idx2]
   }
}
