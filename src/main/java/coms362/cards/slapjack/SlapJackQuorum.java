package coms362.cards.slapjack;

import coms362.cards.model.Quorum;

public class SlapJackQuorum extends Quorum
{
	public SlapJackQuorum(int minPlayers, int maxPlayers)
	{
		super(minPlayers, maxPlayers);
	}
	
	public SlapJackQuorum(String minS, String maxS)
	{
        super(minS, maxS);
    }
}
