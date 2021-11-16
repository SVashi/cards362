package coms362.cards.slapjack;

import java.util.Map;
import java.util.Random;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.CreateButtonRemote;
import coms362.cards.events.remote.CreatePileRemote;
import coms362.cards.events.remote.SetBottomPlayerTextRemote;
import coms362.cards.events.remote.SetGameTitleRemote;
import coms362.cards.events.remote.SetupTable;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.model.Card;
import coms362.cards.model.Location;
import coms362.cards.model.Pile;


public class SlapJackInitCmd implements Move
{
    
    private Table table;
	private Map<Integer, Player> players;
	private String title;
	
	public SlapJackInitCmd(Map<Integer, Player> players, String game, Table table)
	{
		this.players = players;
		this.title = game;
		this.table = table;
	}
	
	@Override
	public void apply(Table table)
	{
		Pile discardPile = new Pile(SlapJackRules.DISCARD_PILE, new Location(300,300));
		discardPile.setFaceUp(true);
		table.addPile(discardPile);
		Pile playerOnePile = new Pile(SlapJackRules.PLAYER_ONE_PILE, new Location(300,150));
		Pile playerTwoPile = new Pile(SlapJackRules.PLAYER_TWO_PILE, new Location(300,450));
		Random random = table.getRandom();
		
        try
        {
        	int even = 0;
            for (String suit : Card.suits)
            {
                for (int i = 1; i <= 13; i++)
                {
                    Card card = new Card();
                    card.setSuit(suit);
                    card.setRank(i);
                    card.setRotate(0);
                    card.setFaceUp(false);
                    if (even % 2 == 0)
                    {
                    	card.setX(300);
                        card.setY(150);
                    	playerOnePile.addCard(card);
                    }
                    else
                    {
                    	card.setX(300);
                        card.setY(450);
                    	playerTwoPile.addCard(card);
                    }
                    System.out.println(even);
                    even++;
                }
            }
            table.addPile(playerOnePile);
            table.addPile(playerTwoPile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

	@Override
	public void apply(ViewFacade view)
	{
		/**Set up table and title**/
		view.send(new SetupTable());
		view.send(new SetGameTitleRemote(title));

		/**Not sure if dealer is necessary in our implementation**/
		for (Player p : players.values()){
			String role = (p.getPlayerNum() == 1) ? "Dealer" : "Player "+p.getPlayerNum();
			p.addToScore(26);
			view.send(new SetBottomPlayerTextRemote(role, p));
		}

		/**Create two piles for two players of slapjack**/
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.PLAYER_ONE_PILE)));
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.PLAYER_TWO_PILE)));
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.DISCARD_PILE)));
		DealButton dealButton = new DealButton("DEAL", new Location(0, 0));
		view.register(dealButton); //so we can find it later. 
		view.send(new CreateButtonRemote(dealButton));
	}

}
