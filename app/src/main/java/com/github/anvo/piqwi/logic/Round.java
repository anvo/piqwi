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
import java.util.HashMap;
import java.util.List;

public class Round implements Serializable
{
	private static final long serialVersionUID = -2203185212615351155L;
	
	private HashMap<Player, Value> values = new HashMap<Player, Value>();
	
	public void addValue(Value v) 
	{
		this.values.put(v.getPlayer(), v);
	}

	public Value getValueFor(Player p) 
	{
		return this.values.get(p);
	}
	
	public boolean isDone(List<Player> players)
	{
		return this.values.keySet().size() >= players.size();
	}

}
