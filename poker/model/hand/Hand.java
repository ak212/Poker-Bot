package poker.model.hand;

public enum Hand {
   StraightFlush(8),
   FourOfAKind(7),
   FullHouse(6),
   Flush(5),
   Straight(4),
   ThreeOfAKind(3),
   TwoPair(2),
   OnePair(1),
   HighCard(0),
   NoHand(-1);
   
   private final int value;
   
   Hand(final int value) {
      this.value = value;
   }
   
   public int getValue() {
      return this.value;
   }
}
