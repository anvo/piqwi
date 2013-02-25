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
package com.github.anvo.piqwi.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;

public class CopyWidthTableRow extends TableRow 
{
	private TableRow source = null;
	
	public CopyWidthTableRow(Context context) 
	{
		super(context);
	}

	public CopyWidthTableRow(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
	}
	
	public void setSource(TableRow s)
	{
		this.source = s;
	}

	//@Override
	protected int[] getColumnsWidths(int widthMeasureSpec) 
	{
		int[] retval = new int[this.getChildCount()];
		for(int i=0; i < this.getChildCount(); i++)
			retval[i] = this.source.getChildAt(i).getWidth();
		return retval;
	}
}
