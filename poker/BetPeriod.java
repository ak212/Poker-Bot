package poker;

public enum BetPeriod {
   PREFLOP(0), FLOP(1), TURN(2), RIVER(3);

   private final int value;

   BetPeriod(final int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public static BetPeriod getBetPeriod(int p) {
      BetPeriod betPeriod = null;

      switch (p) {
      case 0:
         betPeriod = BetPeriod.PREFLOP;
         break;
      case 1:
         betPeriod = BetPeriod.FLOP;
         break;
      case 2:
         betPeriod = BetPeriod.TURN;
         break;
      case 3:
         betPeriod = BetPeriod.RIVER;
         break;
      }
      
      return betPeriod;
   }
}
