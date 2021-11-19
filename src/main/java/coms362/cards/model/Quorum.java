package coms362.cards.model;

/**
 * Contains information about participant count expectations.
 * 
 * For now, it only applies to players. Should we add support for non-playing participants (e.g.,
 * audience, pit boss ...) this will probably need some modification.
 * 
 * min is the minimum number of players required to start. max is the maximum number of players
 * supported.
 * 
 * The quorum is a key part of the game state. In some games, it may be initialized as soon as the
 * game has been selected. in others, it may be managed through a menu, or query parameters.
 * 
 * Presumably, if adding another participant (especially a player) would exceed the maximum, that
 * participant's connection would be refused.
 * 
 * @author Robert Ward
 */
public class Quorum {

    private int min = 0;
    private int max = 0;

    public Quorum(int minPlayers, int maxPlayers) {
        setMin(minPlayers);
        setMax(maxPlayers);
    }

    public Quorum(String minS, String maxS) {
        if (minS != null) {
            setMin(Integer.valueOf(minS));
        }
        if (maxS != null) {
            setMax(Integer.valueOf(maxS));
        }
        if (isSet() && getMin() == 0) {
            setMin(1);
        }
    }

    public boolean isSet() {
        return (getMin() > 0 || getMax() > 0);
    }

    public boolean meets(int count) {
        return count >= getMin() && count <= getMax();
    }

    public boolean exceeds(int count) {
        return count > getMax();
    }

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}
}
