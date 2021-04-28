package engine;

import model.cards.Card;
import model.cards.minions.Minion;

public interface GameListener {
	public void onGameOver();

	public void onEndTurn(Card drawCard);

	public void onWarlockCard2(Card c);

	public void onMinionDeath3(Minion m);

}
