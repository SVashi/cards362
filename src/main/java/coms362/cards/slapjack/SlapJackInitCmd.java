package coms362.cards.slapjack;

import java.util.Map;
import java.util.Random;

import coms362.cards.abstractcomp.Move;
import coms362.cards.abstractcomp.Player;
import coms362.cards.abstractcomp.Table;
import coms362.cards.app.ViewFacade;
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
		Pile discardPile = new Pile(SlapJackRules.DISCARD_PILE, new Location(500,359));
		table.addPile(discardPile);
		Pile playerOnePile = new Pile(SlapJackRules.PLAYER_ONE_PILE, new Location(500,159));
		Pile playerTwoPile = new Pile(SlapJackRules.PLAYER_TWO_PILE, new Location(500,559));
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
                    card.setX(random.nextInt(200) + 100);
                    card.setY(random.nextInt(200) + 100);
                    card.setRotate(random.nextInt(360));
                    card.setFaceUp(random.nextBoolean());
                    if (even % 2 == 0)
                    {
                    	playerOnePile.addCard(card);
                    }
                    else
                    {
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
	public void apply(ViewFacade views)
	{
		// TODO Auto-generated method stub
		
	}

}
