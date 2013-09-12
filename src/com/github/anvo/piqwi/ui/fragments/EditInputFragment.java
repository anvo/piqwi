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
package com.github.anvo.piqwi.ui.fragments;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Value;
import com.github.anvo.piqwi.ui.LocalEvents;

/**
 * Edit an existing entry instead of creating a new one
 */
public class EditInputFragment extends InputFragment 
{
	private int roundNumber = 0;
	
	public void setValue(Value value)
	{
		this.value = value;
		this.redraw();
	}
	
	public void setRound(int round)
	{
		this.roundNumber = round;
	}
	
	@Override
	protected void redraw()
	{
		super.redraw();
		
		TextView currentround = (TextView) this.getActivity().findViewById(R.id.input_currentround);
    	currentround.setText(this.getString(R.string.intput_currentround, this.roundNumber));
	}
	
	@Override
	protected void onButtonOKClick()
	{
		this.getActivity().finish();
		LocalBroadcastManager.getInstance(this.getActivity()).sendBroadcast(new Intent(LocalEvents.ACTION_RESULT_EDIT));
	}

}
