package coms362.cards.slapjack;

import coms362.cards.abstractcomp.GameFactory;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Rules;
import coms362.cards.abstractcomp.Table;
import coms362.cards.abstractcomp.View;
import coms362.cards.abstractcomp.ViewFactory;
import coms362.cards.fiftytwo.P52Player;
import coms362.cards.fiftytwo.P52PlayerView;
import coms362.cards.fiftytwo.PartyRole;
import coms362.cards.model.PlayerFactory;
import coms362.cards.model.TableBaseSlapJack;
import coms362.cards.streams.RemoteTableGateway;

public class SlapJackGameFactory implements GameFactory, PlayerFactory, ViewFactory
{
	@Override
	public Rules createRules()
	{
		return new SlapJackRules();
	}

	@Override
	public Table createTable()
	{
		return new TableBaseSlapJack(this);
	}

	@Override
	public View createView(PartyRole role, Integer num, String socketId, RemoteTableGateway gw )
	{
		return new P52PlayerView(num, socketId, gw);
	}

	@Override
	public Player createPlayer( Integer position, String socketId)
	{
		return new P52Player(position, socketId);
	}

	@Override
	public PlayerFactory createPlayerFactory()
	{
		return this;
	}
}
