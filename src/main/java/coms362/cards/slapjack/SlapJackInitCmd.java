package coms362.cards.slapjack;

import java.util.Map;
import java.util.Random;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
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
	public void apply(ViewFacade views)
	{
		// TODO Auto-generated method stub
		
	}

}
