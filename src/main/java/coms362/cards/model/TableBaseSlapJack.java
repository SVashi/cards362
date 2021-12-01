package coms362.cards.model;

import coms362.cards.abstractcomp.Player;

public class TableBaseSlapJack extends TableBase{

    public TableBaseSlapJack(PlayerFactory pFactory) {
        super(pFactory);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Player checkPlayerWin() {
		boolean[] playersOut = new boolean[getPlayers().size()];
		int numPlayersOut = 0;
		for(int i = 0; i < getPlayers().size(); i++) {
			if(getPlayer(i+1).getScore() == 0) {
				playersOut[i] = true;
				numPlayersOut++;
			} else {
				playersOut[i] = false;
			}
		}
		if(numPlayersOut == getPlayers().size() -1) {
			for(int i = 0; i < playersOut.length; i++) {
				if(!playersOut[i]) {
					return getPlayer(i+1);
				}
			}
		} 
		return null;
		
	}
    
}
