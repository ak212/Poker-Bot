public class Hand {
   Card card1;
   Card card2;

   // TODO fix this so we can have 2, 5, 6, 7 card hands

   public Hand(Card card1, Card card2) {
      this.card1 = card1;
      this.card2 = card2;
   }

   public void printHand() {
      System.out.println(this.card1.shorten() + this.card2.shorten());
   }
}
