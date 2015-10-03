
public enum Rank {
	ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), 
	TEN(10), JACK(11), QUEEN(12), KING(13);

	private int value;

	private Rank(int value) {
	      this.value = value;
	   }

	public int getValue() {
		return value;
	}

	public static Rank getRank(int r) {
		Rank rank = null;

		switch (r) {
		case 1:
			rank = Rank.ACE;
			break;
		case 2:
			rank = Rank.TWO;
			break;
		case 3:
			rank = Rank.THREE;
			break;
		case 4:
			rank = Rank.FOUR;
			break;
		case 5:
			rank = Rank.FIVE;
			break;
		case 6:
			rank = Rank.SIX;
			break;
		case 7:
			rank = Rank.SEVEN;
			break;
		case 8:
			rank = Rank.EIGHT;
			break;
		case 9:
			rank = Rank.NINE;
			break;
		case 10:
			rank = Rank.TEN;
			break;
		case 11:
			rank = Rank.JACK;
			break;
		case 12:
			rank = Rank.QUEEN;
			break;
		case 13:
			rank = Rank.KING;
			break;
		default:
			break;
		}

		return rank;
	}
}
