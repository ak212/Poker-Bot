package poker;

public enum Action {
   CHECKCALL(0), BET(1), FOLD(2);

   private final int value;

   Action(final int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
