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
import coms362.cards.fiftytwo.DealCommand;
import coms362.cards.fiftytwo.DropEventCmd;
import coms362.cards.fiftytwo.P52InitCmd;
import coms362.cards.fiftytwo.P52Move;
import coms362.cards.fiftytwo.PartyRole;
import coms362.cards.fiftytwo.SetQuorumCmd;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SlapJackRules extends RulesDispatchBase implements Rules, RulesDispatch 
{
	public static final String PLAYER_ONE_PILE = "p1Pile";
    public static final String PLAYER_TWO_PILE = "p2Pile";
    public static final String DISCARD_PILE = "discardPile";
    
    SlapJackRules()
    {
    	registerEvents();
    }
    
	@Override
	public Move eval(Event nextE, Table table, Player player)
	{
		return nextE.dispatch(this, table, player);
	}
	
	public Move apply(CardEvent e, Table table, Player player){	
		Pile fromPile = null;
		if (player.getPlayerNum() == 1)
		{
			fromPile = table.getPile(PLAYER_ONE_PILE);
		}
		if (player.getPlayerNum() == 2)
		{
			fromPile = table.getPile(PLAYER_TWO_PILE);
		}
		Pile toPile = table.getPile(DISCARD_PILE);
		Card c = fromPile.getCard(e.getId());
		if (c == null) {
			return new DropEventCmd();
		}
		return new P52Move(c, player, fromPile, toPile);		
	}
	
	public Move apply(DealEvent e, Table table, Player player){
		return new DealCommand(table, player);
	}
	
	public Move apply(InitGameEvent e, Table table, Player player){
		return new P52InitCmd(table.getPlayerMap(), "Slapjack Multiplayer", table);
	}
	
	public Move apply(NewPartyEvent e, Table table, Player player){
		if (e.getRole() == PartyRole.player){
			return new CreatePlayerCmd( e.getPosition(), e.getSocketId());
		}
		return new DropEventCmd();
	}
	
	public Move apply(SetQuorumEvent e, Table table, Player player){
		return new SetQuorumCmd(e.getQuorum());
	}
	
	public Move apply(ConnectEvent e, Table table, Player player){
		Move rval = new DropEventCmd(); 
		System.out.println("Rules apply ConnectEvent "+e);
		if (! table.getQuorum().exceeds(table.getPlayers().size()+1)){
			if (e.getRole() == PartyRole.player){				
				rval =  new CreatePlayerCmd( e.getPosition(), e.getSocketId());
			}			
		}
		System.out.println("PickupRules connectHandler rval = "+rval);
		return rval;
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
