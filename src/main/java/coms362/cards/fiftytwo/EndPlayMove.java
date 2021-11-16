package coms362.cards.fiftytwo;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.SetGameTitleRemote;


public class EndPlayMove implements Move {
	
	private String title;

	public EndPlayMove(String title)
	{
		this.title = title;
	}
	
	public void apply(Table table) {
		table.setMatchOver(true);
	}

	public void apply(ViewFacade view) {	
		view.send(new SetGameTitleRemote(title));
	}
	
	@Override	
	public boolean isMatchEnd(){
		return true;
	}

}
