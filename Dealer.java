import java.util.ArrayList;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   int pot;
   int currentBet;
   ArrayList<Integer> sidePots;

   public Dealer() {
      pot = 0;
      currentBet = 0;
      communityCards = new ArrayList<Card>();
      burnCards = new ArrayList<Card>();
   }

   public Card drawCard() {
      Card card = deckOfCards.deck.get(0);
      deckOfCards.deck.remove(0);
      return card;
   }

   public void flop() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
      communityCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void turn() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void river() {
      burnCards.add(drawCard());
      communityCards.add(drawCard());
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      for (Card card : communityCards) {
         System.out.println(card.shorten());
      }
   }

}
