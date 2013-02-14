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

import java.util.LinkedList;

import junit.framework.TestCase;

public class RoundTest extends TestCase 
{
	public void testUsage()
	{
		Round r = new Round();
		
		Value v = new Value(Player.Dummy);
		r.addValue(v);
		
		assertEquals(v, r.getValueFor(Player.Dummy));
	}
	
	public void testIsDone()
	{
		Round r = new Round();
		
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(Player.Dummy);
		players.add(new Player("Player2"));
		
		assertFalse(r.isDone(players));
		
		r.addValue(new Value(players.getFirst()));
		
		assertFalse(r.isDone(players));
		
		r.addValue(new Value(players.getLast()));
		
		assertTrue(r.isDone(players));
	}

}
