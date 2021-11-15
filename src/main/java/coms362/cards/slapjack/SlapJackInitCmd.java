package coms362.cards.slapjack;

import java.util.Map;
import java.util.Random;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
import coms362.cards.events.remote.CreatePileRemote;
import coms362.cards.events.remote.SetBottomPlayerTextRemote;
import coms362.cards.events.remote.SetGameTitleRemote;
import coms362.cards.events.remote.SetupTable;
import coms362.cards.fiftytwo.DealButton;
import coms362.cards.fiftytwo.P52Rules;
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
		Pile playerOnePile = new Pile(SlapJackRules.PLAYER_ONE_PILE, new Location(500,359));
		Random random = table.getRandom();
        try {
            for (String suit : Card.suits) {
                for (int i = 1; i <= 13; i++) {
                    Card card = new Card();
                    card.setSuit(suit);
                    card.setRank(i);
                    card.setX(random.nextInt(200) + 100);
                    card.setY(random.nextInt(200) + 100);
                    card.setRotate(random.nextInt(360));
                    card.setFaceUp(random.nextBoolean());
                    playerOnePile.addCard(card);
                }
            }
            table.addPile(playerOnePile);
            table.addPile(new Pile(P52Rules.DISCARD_PILE, new Location(500,359)));
        } catch (Exception e) {
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
			view.send(new SetBottomPlayerTextRemote(role, p));
		}

		/**Create two piles for two players of slapjack**/
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.PLAYER_ONE_PILE)));
		view.send(new CreatePileRemote(table.getPile(SlapJackRules.PLAYER_TWO_PILE)));
		view.send(new CreatePileRemote(table.getPile(P52Rules.DISCARD_PILE)));
		DealButton dealButton = new DealButton("DEAL", new Location(0, 0));
		view.register(dealButton); //so we can find it later. 
		
	}

}
