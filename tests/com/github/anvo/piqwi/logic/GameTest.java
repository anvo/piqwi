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

import junit.framework.TestCase;

public class GameTest extends TestCase 
{
	
	public void testUsage()
	{
		//Init
		Game g = new Game();
		
		final Player p1 = new Player("Player One");
		final Player p2 = new Player("Player Two");
		
		g.getPlayers().add(p1);
		g.getPlayers().add(p2);
		
		//Round 1
		Round r1 = new Round();
		
		Value vr1p1 = new Value(p1);
		vr1p1.getPoints().add(2);
		r1.addValue(vr1p1);
		
		Value vr1p2 = new Value(p2);
		vr1p2.getPoints().add(4);
		vr1p2.getPoints().add(2);
		r1.addValue(vr1p2);
		
		g.getRounds().add(r1);
		
		//Round 2
		Round r2 = new Round();
		
		Value vr2p1 = new Value(p1);
		vr2p1.getPoints().add(12);
		r2.addValue(vr2p1);
		
		Value vr2p2 = new Value(p2);
		vr2p2.getPoints().add(0);
		r2.addValue(vr2p2);
		
		g.getRounds().add(r2);		
		
		//Result
		assertEquals(14, g.getResultFor(p1));
		assertEquals(6, g.getResultFor(p2));
	}
	
	public void testNextPlayer()
	{
		final Player p1 = new Player("Player One");
		final Player p2 = new Player("Player Two");
		final Player p3 = new Player("Player Three");
		
		Game g = new Game();
		g.getPlayers().add(p1);
		g.getPlayers().add(p2);
		g.getPlayers().add(p3);
		
		assertEquals(p1, g.nextPlayer());
		
		Round r1 = new Round();
		g.getRounds().add(r1);
		
		assertEquals(p1, g.nextPlayer());
		
		Value vp1 = new Value(p1);
		r1.addValue(vp1);
		
		assertEquals(p2, g.nextPlayer());
		
		Value vp2 = new Value(p2);
		r1.addValue(vp2);
		
		assertEquals(p3, g.nextPlayer());
		
		Value vp3 = new Value(p3);
		r1.addValue(vp3);
		
		assertEquals(p1, g.nextPlayer());
	}
	
	public void testReset()
	{
		//Init
		Game g = new Game();
		
		final Player p1 = new Player("Player One");
		final Player p2 = new Player("Player Two");
		
		g.getPlayers().add(p1);
		g.getPlayers().add(p2);
		
		Round r1 = new Round();
		g.getRounds().add(r1);
		
		Value vp1 = new Value(p1);
		vp1.getPoints().add(2);
		r1.addValue(vp1);
		
		Value vp2 = new Value(p2);
		vp2.getPoints().add(12);
		r1.addValue(vp2);
		
		
		assertEquals(2, g.getPlayers().size());
		assertEquals(1, g.getRounds().size());
		assertEquals(2, g.getResultFor(p1));
		assertEquals(12, g.getResultFor(p2));
		
		//Reset game
		g.reset();
		
		assertEquals(2, g.getPlayers().size());
		assertEquals(0, g.getRounds().size());
		assertEquals(0, g.getResultFor(p1));
		assertEquals(0, g.getResultFor(p2));		
						
	}
	
	public void testAddNextValue()
	{
		//Init
		Game g = new Game();
		
		final Player p1 = new Player("Player One");
		final Player p2 = new Player("Player Two");
		
		g.getPlayers().add(p1);
		g.getPlayers().add(p2);
		
		Value p1v1 = new Value(p1,4);
		Value p2v1 = new Value(p2,5);
		Value p1v2 = new Value(p1,4);
		Value p2v2 = new Value(p2,5);
		
		g.addNextValue(p1v1);
		
		assertEquals(1, g.getRounds().size());
		assertEquals(4, g.getResultFor(p1));
		assertEquals(0, g.getResultFor(p2));
		
		g.addNextValue(p2v1);
		
		assertEquals(1, g.getRounds().size());
		assertEquals(4, g.getResultFor(p1));
		assertEquals(5, g.getResultFor(p2));
		
		g.addNextValue(p1v2);
		
		assertEquals(2, g.getRounds().size());
		assertEquals(8, g.getResultFor(p1));
		assertEquals(5, g.getResultFor(p2));
		
		g.addNextValue(p2v2);
		
		assertEquals(2, g.getRounds().size());
		assertEquals(8, g.getResultFor(p1));
		assertEquals(10, g.getResultFor(p2));			
		
	}
	
	public void testGetNumCurrentRound()
	{
		Game g = new Game();
		g.getPlayers().add(new Player("Player1"));
		g.getPlayers().add(new Player("Player2"));
		
		assertEquals(1, g.getNumCurrentRound());
		
		g.addNextValue(new Value(g.getPlayers().get(0)));
		
		assertEquals(1, g.getNumCurrentRound());
		
		g.addNextValue(new Value(g.getPlayers().get(1)));
		
		assertEquals(2, g.getNumCurrentRound());
	}
}
