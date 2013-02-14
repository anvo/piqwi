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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ValueTest extends TestCase 
{

	public void testUsage()
	{
		Value v = new Value(new Player("Peter"));
		v.getPoints().add(2);
		v.getPoints().add(4);
		
		assertEquals(6, v.getSum());		
	}
	
	public void testAddList()
	{
		Value v = new Value(Player.Dummy);
		List<Integer> l = new ArrayList<Integer>(3);
		l.add(2);
		l.add(12);
		l.add(4);
		v.getPoints().addAll(l);
		
		assertEquals(18, v.getSum());
	}
	
	public void testGetPlayer()
	{
		final Player p = new Player("Peter");
		Value v = new Value(p);
		assertEquals(p, v.getPlayer());
	}
}
