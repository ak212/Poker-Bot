
public class Player {
	Hand hand;
	int stack;

	public Player(int stack) {
		this.stack = stack;
	}

	public void bet(int bet) {
		this.stack -= bet;
	}
}
