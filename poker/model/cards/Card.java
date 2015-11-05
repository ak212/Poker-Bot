package poker.model.cards;
public class Card {
   private Suit suit;
   private Rank rank;

   public Card(Suit suit, Rank rank) {
      this.setSuit(suit);
      this.setRank(rank);
   }

   public String shorten() {
      String card = "";

      switch (this.getRank()) {
      case ACE:
         card = "A";
         break;
      case TWO:
         card = "2";
         break;
      case THREE:
         card = "3";
         break;
      case FOUR:
         card = "4";
         break;
      case FIVE:
         card = "5";
         break;
      case SIX:
         card = "6";
         break;
      case SEVEN:
         card = "7";
         break;
      case EIGHT:
         card = "8";
         break;
      case NINE:
         card = "9";
         break;
      case TEN:
         card = "10";
         break;
      case JACK:
         card = "J";
         break;
      case QUEEN:
         card = "Q";
         break;
      case KING:
         card = "K";
         break;
      default:
         break;
      }

      switch (this.getSuit()) {
      case SPADES:
         card += "s";
         break;
      case HEARTS:
         card += "h";
         break;
      case CLUBS:
         card += "c";
         break;
      case DIAMONDS:
         card += "d";
         break;
      default:
         break;
      }

      return card;
   }

   public Rank getRank() {
      return rank;
   }

   public void setRank(Rank rank) {
      this.rank = rank;
   }

   public Suit getSuit() {
      return suit;
   }

   public void setSuit(Suit suit) {
      this.suit = suit;
   }
}
