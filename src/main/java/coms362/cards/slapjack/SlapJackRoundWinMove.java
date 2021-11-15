package coms362.cards.slapjack;

import java.util.Collection;
import java.util.Iterator;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.AddToPileBottomRemote;
import coms362.cards.events.remote.HideCardRemote;
import coms362.cards.events.remote.RemoveFromPileRemote;
import coms362.cards.events.remote.ShowPlayerScore;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SlapJackRoundWinMove implements Move
{
	private Player p;
	private Pile fromPile;
	private Pile toPile;

	SlapJackRoundWinMove(Player p, Pile fromPile, Pile toPile){
		this.p = p;
		this.fromPile = fromPile;
		this.toPile = toPile;
	}

	@Override
	public void apply(Table table)
	{
		Collection<Card> tmp = fromPile.getCards();
		Iterator<Card> iter = tmp.iterator();
		Card c;
		while (iter.hasNext()) {
			c = iter.next();
			//uses a quality ternary operator
			table.addToPile(p.getPlayerNum() == 1 ? SlapJackRules.PLAYER_ONE_PILE : SlapJackRules.PLAYER_TWO_PILE, c);
			table.removeFromPile(SlapJackRules.DISCARD_PILE,c);
			//would be best to update score here
		}
		
	}

	@Override
	public void apply(ViewFacade view)
	{
		Collection<Card> tmp = fromPile.getCards();
		Iterator<Card> iter = tmp.iterator();
		Card c;
		while (iter.hasNext()) {
			c = iter.next();
			//hides the card and adds it to the bottom of the pile of the round winner
			view.send(new HideCardRemote(c));
			view.send(new RemoveFromPileRemote(fromPile, c));
			view.send(new AddToPileBottomRemote(toPile, c));
			view.send(new HideCardRemote(c));
		}
		view.send(new ShowPlayerScore(p, null));
	}
}
