package poker;

public enum Suit {
   SPADES(0), HEARTS(1), CLUBS(2), DIAMONDS(3);

   private int value;

   private Suit(int value) {
      this.value = value;
   }

   public int getValue() {
      return value;
   }

   public static Suit getSuit(int s) {
      Suit suit = null;

      switch (s) {
      case 0:
         suit = Suit.SPADES;
         break;
      case 1:
         suit = Suit.HEARTS;
         break;
      case 2:
         suit = Suit.CLUBS;
         break;
      case 3:
         suit = Suit.DIAMONDS;
         break;
      default:
         break;
      }
      
      return suit;
   }
}
