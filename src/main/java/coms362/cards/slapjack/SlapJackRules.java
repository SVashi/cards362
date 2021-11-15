package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.RulesDispatch;
import coms362.cards.abstractcomp.RulesDispatchBase;
import coms362.cards.abstractcomp.Table;
import coms362.cards.events.inbound.Event;
import coms362.cards.events.inbound.InitGameEvent;
import coms362.cards.fiftytwo.P52InitCmd;

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
		// TODO Auto-generated method stub
		return null;
	}

	public Move apply(InitGameEvent e, Table table, Player player){
		return new SlapJackInitCmd(table.getPlayerMap(), "SlapJack", table);
	}
	
	private void registerEvents()
	{
		
	}
}
