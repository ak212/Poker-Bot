import java.util.ArrayList;

public class Dealer {
   DeckOfCards deckOfCards;
   ArrayList<Card> communityCards;
   ArrayList<Card> burnCards;
   int smallBlind;
   int bigBlind;
   int pot;
   int currentBet;
   ArrayList<Integer> sidePots;

   public Dealer() {
      pot = 0;
      currentBet = 0;
      communityCards = new ArrayList<Card>();
      burnCards = new ArrayList<Card>();
      deckOfCards = new DeckOfCards();
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

   public void newHand() {
      burnCards.clear();
      communityCards.clear();
      pot = 0;
      currentBet = 0;
      sidePots.clear();
      deckOfCards = new DeckOfCards();
   }

   public void printCommunityCards() {
      System.out.println("On the board:");
      for (Card card : communityCards) {
         System.out.println(card.shorten());
      }
   }

}
