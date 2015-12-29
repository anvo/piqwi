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

public class Player implements Serializable
{
	private static final long serialVersionUID = -878825939768785799L;

	public final static Player Dummy = new Player("Dummy Player");
	
	private String name = null;
	
	public Player(String name) 
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
