package coms362.cards.slapjack;

import java.util.ArrayList;
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
//		ArrayList<Card> tmp = new ArrayList<>(fromPile.getCards());
//		Card c;
//		for(int i =0; i<tmp.size(); i++) {
//			c =tmp.get(i);
//			//uses a quality ternary operator
//			table.addToPile(p.getPlayerNum() == 1 ? SlapJackRules.PLAYER_ONE_PILE : SlapJackRules.PLAYER_TWO_PILE, c);
//			table.removeFromPile(SlapJackRules.DISCARD_PILE,c);
//			p.addToScore(1);
//		}
	}

	@Override
	public void apply(ViewFacade view)
	{
		ArrayList<Card> tmp = new ArrayList<>(toPile.getCards());
		Card c;
		for(int i =0; i<tmp.size(); i++) {
			c = tmp.get(i);
			//hides the card and adds it to the bottom of the pile of the round winner
			view.send(new HideCardRemote(c));
			view.send(new RemoveFromPileRemote(fromPile, c));
			view.send(new AddToPileBottomRemote(toPile, c));
			view.send(new HideCardRemote(c));
		}
		view.send(new ShowPlayerScore(p, null));
	}
}
