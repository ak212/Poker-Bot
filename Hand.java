
public class Hand {
   Card card1;
   Card card2;

   public Hand(Card card1, Card card2) {
      this.card1 = card1;
      this.card2 = card2;
   }

   public void printHand() {
      System.out.println(this.card1.rank + " of " + this.card1.suit);
      System.out.println(this.card2.rank + " of " + this.card2.suit);
   }
}
