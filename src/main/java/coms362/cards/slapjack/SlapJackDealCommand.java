package coms362.cards.slapjack;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.CreateCardRemote;
import coms362.cards.events.remote.HideButtonRemote;
import coms362.cards.events.remote.UpdateCardRemote;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.fiftytwo.P52Rules;
import coms362.cards.model.Card;
import coms362.cards.model.Pile;

public class SlapJackDealCommand implements Move
{
	  private Table table;

	    public SlapJackDealCommand(Table table, Player player) {
	        this.table = table;
	    }

	    public void apply(Table table) {
	        // TODO Auto-generated method stub

	    }

	    public void apply(ViewFacade views) {

	        try {
	            String remoteId = views.getRemoteId(DealButton.kSelector);
	            views.send(new HideButtonRemote(remoteId));
	            Pile local = table.getPile(""+1);
	            if (local == null) {
	                return;
	            }
	            for (Card c : local.getCards()) {
	                String outVal = "";
	                views.send(new CreateCardRemote(c));
	                views.send(new UpdateCardRemote(c));
	                System.out.println(outVal);
	            }
	            local = table.getPile(""+2);
	            if (local == null) {
	                return;
	            }
	            for (Card c : local.getCards()) {
	                String outVal = "";
	                views.send(new CreateCardRemote(c));
	                views.send(new UpdateCardRemote(c));
	                System.out.println(outVal);
	            }
	            local = table.getPile(""+3);
	            if (local == null) {
	                return;
	            }
	            for (Card c : local.getCards()) {
	                String outVal = "";
	                views.send(new CreateCardRemote(c));
	                views.send(new UpdateCardRemote(c));
	                System.out.println(outVal);
	            }
	            local = table.getPile(""+4);
	            if (local == null) {
	                return;
	            }
	            for (Card c : local.getCards()) {
	                String outVal = "";
	                views.send(new CreateCardRemote(c));
	                views.send(new UpdateCardRemote(c));
	                System.out.println(outVal);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
