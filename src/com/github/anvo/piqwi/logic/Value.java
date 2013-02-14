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
import java.util.LinkedList;
import java.util.List;

public class Value implements Serializable
{
	private static final long serialVersionUID = -5882015840215719668L;

	// Associated player
	private Player player = null;
	
	private List<Integer> points = new LinkedList<Integer>();
	
	public Value(Player player) 
	{
		this.player = player;
	}
	
	public Value(Player player, int init) 
	{
		this(player);
		this.points.add(init);
	}	

	public int getSum() 
	{
		int result = 0;
		for(Integer i: this.points)
			result += i;
		return result;
	}

	public List<Integer> getPoints()
	{
		return this.points;
	}

	public Player getPlayer() 
	{
		return this.player;
	}

}
