package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.RulesDispatch;
import coms362.cards.abstractcomp.RulesDispatchBase;
import coms362.cards.abstractcomp.Table;
import coms362.cards.events.inbound.CardEvent;
import coms362.cards.events.inbound.ConnectEvent;
import coms362.cards.events.inbound.DealEvent;
import coms362.cards.events.inbound.Event;
import coms362.cards.events.inbound.EventUnmarshallers;
import coms362.cards.events.inbound.GameRestartEvent;
import coms362.cards.events.inbound.InitGameEvent;
import coms362.cards.events.inbound.NewPartyEvent;
import coms362.cards.events.inbound.SetQuorumEvent;
import coms362.cards.fiftytwo.CreatePlayerCmd;
import coms362.cards.slapjack.SlapJackDealCommand;
import coms362.cards.fiftytwo.DropEventCmd;
import coms362.cards.fiftytwo.P52InitCmd;
import coms362.cards.fiftytwo.P52Move;
import coms362.cards.fiftytwo.PartyRole;
import coms362.cards.fiftytwo.SetQuorumCmd;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;
import coms362.cards.fiftytwo.EndPlayMove;

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
	
	public Move apply(DealEvent e, Table table, Player player){
		return new SlapJackDealCommand(table, player);
	}
	
	public Move apply(InitGameEvent e, Table table, Player player){
		return new SlapJackInitCmd(table.getPlayerMap(), "Slapjack Multiplayer", table);
	}
	
	public Move apply(NewPartyEvent e, Table table, Player player){
		if (e.getRole() == PartyRole.player){
			return new CreatePlayerCmd( e.getPosition(), e.getSocketId());
		}
		return new DropEventCmd();
	}
	
	public Move apply(SetQuorumEvent e, Table table, Player player){
		return new SetQuorumCmd(new SlapJackQuorum(2,2));
	}
	
	public Move apply(ConnectEvent e, Table table, Player player){
		Move rval = new DropEventCmd(); 
		System.out.println("Rules apply ConnectEvent "+e);
		if (! table.getQuorum().exceeds(table.getPlayers().size()+1)){
			if (e.getRole() == PartyRole.player){				
				rval =  new CreatePlayerCmd( e.getPosition(), e.getSocketId());
			}			
		}
		System.out.println("SlapJackRules connectHandler rval = "+rval);
		return rval;
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
		handlers.registerHandler(InitGameEvent.kId, (Class) InitGameEvent.class); 
		handlers.registerHandler(DealEvent.kId, (Class) DealEvent.class); 
		handlers.registerHandler(CardEvent.kId, (Class) CardEvent.class); 
		handlers.registerHandler(GameRestartEvent.kId, (Class) GameRestartEvent.class); 
		handlers.registerHandler(NewPartyEvent.kId, (Class) NewPartyEvent.class);
	}
}
