/*
Copyright (C) 2013 Andreas Volk

This file is part of PiQwi.

PiQwi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PiQwi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with PiQwi. If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.anvo.piqwi.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Game implements Serializable
{
	private static final long serialVersionUID = 7006103846658898527L;
	
	private List<Player> players = new LinkedList<Player>();
	private List<Round> rounds = new LinkedList<Round>();

	public List<Player> getPlayers() 
	{
		return this.players;
	}

	public List<Round> getRounds() 
	{
		return this.rounds;
	}

	public int getResultFor(Player p) {
		int result = 0;
		for(Round r: this.rounds)
			if(r.getValueFor(p) != null)
				result += r.getValueFor(p).getSum();
		return result;
	}

	/**
	 * 
	 * @return Player.Dummy if the list of players is empty
	 */
	public Player nextPlayer() 
	{
		if(this.players.isEmpty())
			return Player.Dummy;
		
		if(this.rounds.isEmpty())
			return this.players.get(0);
		
		Round last = this.getLastRound();
		for(Player p: this.players)
			if(last.getValueFor(p) == null)
				return p;
		
		return this.players.get(0);
	}
	
	public void reset()
	{
		this.rounds.clear();
	}
	
	public void addNextValue(Value v)
	{
		//First round
		if(this.getRounds().isEmpty())
			this.getRounds().add(new Round());
		
		Round r = this.getLastRound();
		
		//Check if round is full
		if(r.getValueFor(v.getPlayer()) != null)
		{
			this.getRounds().add(new Round());
			r = this.getLastRound();
		}
		
		r.addValue(v);			
	}
	
	public int getNumCurrentRound()
	{
		if(this.getRounds().size() == 0)
			return 1;
		if(this.getLastRound().isDone(this.getPlayers()))
			return this.getRounds().size() +1;
		return this.getRounds().size();
	}
	
	public List<Player> getResultList()
	{
		ArrayList<Player> result = new ArrayList<Player>(this.players);
		Collections.sort(result, new ResultComparator(this));
		return result;		
	}
	
	private Round getLastRound()
	{
		if(this.rounds.isEmpty())
			return null;
		return this.rounds.get(this.rounds.size() -1);
	}
	
	class ResultComparator implements Comparator<Player>
	{
		private Game game = null;

		public ResultComparator(Game game) {
			this.game = game;
		}

		@Override
		public int compare(Player lhs, Player rhs) {
			
			Integer resl = this.game.getResultFor(lhs);
			Integer resr = this.game.getResultFor(rhs);
			return resr.compareTo(resl);
		}
		
	}	

}
