package coms362.cards.slapjack;

import coms362.cards.model.Quorum;

public class SlapJackQuorum extends Quorum
{
	public SlapJackQuorum(int minPlayers, int maxPlayers)
	{
		super(2, 2);
	}
	
	public SlapJackQuorum(String minS, String maxS)
	{
        super(2, 2);
    }
}
