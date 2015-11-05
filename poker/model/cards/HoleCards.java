package poker.model.cards;
public class HoleCards {
   private Card card1;
   private Card card2;

   // TODO fix this so we can have 2, 5, 6, 7 card hands

   public HoleCards(Card card1, Card card2) {
      this.setCard1(card1);
      this.setCard2(card2);
   }

   public void printHoleCards() {
      System.out.println(this.getCard1().shorten() + this.getCard2().shorten());
   }

   public Card getCard1() {
      return card1;
   }

   public void setCard1(Card card1) {
      this.card1 = card1;
   }

   public Card getCard2() {
      return card2;
   }

   public void setCard2(Card card2) {
      this.card2 = card2;
   }
}
