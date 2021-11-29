package coms362.cards.slapjack;

import java.util.ArrayList;
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
import coms362.cards.events.remote.ShowPlayerScore;
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
		ArrayList<Pile> playerPiles = new ArrayList<>();
		int[] x = {300, 300, 150, 450};
		int[] y = {150, 450, 300, 300};
		for (int i = 1; i <= table.getPlayers().size(); i++)
		{
			playerPiles.add(new Pile(""+i, new Location(x[i-1],y[i-1])));
		}
		Random random = table.getRandom();
		int counter = 0;
		try
        {
            for (String suit : Card.suits)
            {
            	boolean[] avail = new boolean[13];
            	for (int i = 0; i < 13; i++)
            		avail[i] = true;
                for (int i = 1; i <= 13; i++)
                {
                    Card card = new Card();
                    card.setSuit(suit);
                    
                    int rand = random.nextInt(13);
                    while (avail[rand] == false)
                    	rand = random.nextInt(13);
                    
                    card.setRank(rand+1);
                    card.setRotate(0);
                    card.setFaceUp(false);
                    avail[rand] = false;
                    card.setX(x[counter % table.getPlayers().size()]);
                    card.setY(y[counter % table.getPlayers().size()]);
                    playerPiles.get(counter % table.getPlayers().size()).addCard(card);
                    counter++;
                }
            }
            for (int i = 0; i < table.getPlayers().size(); i++)
            {
            	table.addPile(playerPiles.get(i));
            }
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
			p.addToScore(table.getPile(""+p.getPlayerNum()).getCards().size());
			view.send(new SetBottomPlayerTextRemote(role, p));
			view.send(new ShowPlayerScore(p, null));
		}

		/**Create two piles for two players of slapjack**/
		for (int i = 1; i <= players.size(); i++)
        {
			view.send(new CreatePileRemote(table.getPile(""+i)));
        }
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.DISCARD_PILE)));
		DealButton dealButton = new DealButton("DEAL", new Location(0, 0));
		view.register(dealButton); //so we can find it later. 
		view.send(new CreateButtonRemote(dealButton));
	}

}
