package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.RulesDispatch;
import coms362.cards.abstractcomp.RulesDispatchBase;
import coms362.cards.abstractcomp.Table;
import coms362.cards.events.inbound.CardEvent;
import coms362.cards.events.inbound.Event;
import coms362.cards.events.inbound.InitGameEvent;
import coms362.cards.events.inbound.EventUnmarshallers;
import coms362.cards.fiftytwo.DropEventCmd;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SlapJackRules extends RulesDispatchBase implements Rules, RulesDispatch 
{
	public static final String PLAYER_ONE_PILE = "p1Pile";
    public static final String PLAYER_TWO_PILE = "p2Pile";
    public static final String DISCARD_PILE = "discardPile";
	private int turn = 1;
    
    SlapJackRules()
    {
    	registerEvents();
    }
    
	@Override
	public Move eval(Event nextE, Table table, Player player)
	{
		return nextE.dispatch(this, table, player);
	}

	public Move apply(InitGameEvent e, Table table, Player player)
	{
		return new SlapJackInitCmd(table.getPlayerMap(), "SlapJack", table);
	}

	public Move apply(CardEvent e, Table table, Player player)
	{	
		Pile p1Pile = table.getPile(PLAYER_ONE_PILE);
		Pile p2Pile = table.getPile(PLAYER_TWO_PILE);
		Pile toPile = table.getPile(DISCARD_PILE);
		Card c;
		//always check if they chose from the center pile and if so check the rank of the card
		c = toPile.getCard(e.getId());
		if(c != null && c.getRank() == 11){
			//if the rank is 11 it is a jack so that player will win the round
			return new SlapJackRoundWinMove(player, toPile , player.getPlayerNum() == 1 ? p1Pile : p2Pile);
		}
		//just does a flipflop between player 1 and 2 as there is only 2 players
		// does this as nothing else is implemented
		if(turn == 1 && player.getPlayerNum() == 1){
			//yes this is repeated code for each player just a quick mockup to see how things worked
			//this is when it is player 1's turn and they are the person who selected a card currently
			c = p1Pile.getCard(e.getId());
			if (c == null) {
				//this checks if they selected from their deck
				return new DropEventCmd();
			}
			//flip turn after it is determined that they selected from their pile
			turn = 2;
			return new SlapJackMove(c, player, p1Pile, toPile);
		}else if(turn == 2 && player.getPlayerNum() == 2){
			//this is when it is player 2's turn and they are the person who selected a card currently
			c = p2Pile.getCard(e.getId());
			if (c == null) {
				return new DropEventCmd();
			}
			turn = 1;
			return new SlapJackMove(c, player, p2Pile, toPile);
		}else{
			//if it is not their turn or they selected from the wrong deck then 
			return new DropEventCmd();
		}		
	}
	
	private void registerEvents()
	{
		EventUnmarshallers handlers = EventUnmarshallers.getInstance();
		handlers.registerHandler(CardEvent.kId, (Class) CardEvent.class); 
	}
}
