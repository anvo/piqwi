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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Value;
import com.github.anvo.piqwi.ui.GameActivity;
import com.github.anvo.piqwi.ui.LocalEvents;

public class InputFragment extends SherlockFragment {

	protected Value value = null;
	private BroadcastReceiver broadcastReceiver = null;
		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	if(this.value == null)
    		this.resetInputValues();
    	
        //Number callbacks    	
    	this.registerNumberButtonListener(2, R.id.input_button2);
    	this.registerNumberButtonListener(3, R.id.input_button3);
    	this.registerNumberButtonListener(4, R.id.input_button4);
    	this.registerNumberButtonListener(5, R.id.input_button5);
    	this.registerNumberButtonListener(12, R.id.input_button12);
    	
    	//Clear button
    	Button buttonC = (Button) this.getActivity().findViewById(R.id.input_buttonc);
    	buttonC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonCClick();
			}
		});
    	
    	//Save button
    	Button buttonOk = (Button) this.getActivity().findViewById(R.id.input_buttonok);
    	buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonOKClick();
			}
		});    	
    	    	
    	//Receive local events
    	this.broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
					InputFragment.this.resetInputValues();
		}};
		IntentFilter eventFilter = new IntentFilter();
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_ADD);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_EDIT);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_REMOVE);
		eventFilter.addAction(LocalEvents.ACTION_GAME_NEW);
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, eventFilter);
    	
    	this.redraw();
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }
	
    protected void redraw()
    {
    	TextView currentround = (TextView) this.getActivity().findViewById(R.id.input_currentround);
    	currentround.setText("Runde " + GameActivity.getGame().getNumCurrentRound());
    	
    	TextView currentplayer = (TextView) this.getActivity().findViewById(R.id.input_currentplayer);
    	currentplayer.setText(this.value.getPlayer().getName());
    	
    	TextView sum = (TextView) this.getActivity().findViewById(R.id.input_sum);
    	sum.setText(Integer.toString(this.value.getSum()));
    	
    	TextView sumdetail = (TextView) this.getActivity().findViewById(R.id.input_sumdetail);
    	sumdetail.setText(this.getSumDetailText());
    }
    
    protected String getSumDetailText()
    {    	
    	if(this.value.getPoints().isEmpty())
    		return "0";
    	return TextUtils.join(" + ", this.value.getPoints());
    }
    
    protected void registerNumberButtonListener(final int num, int btn)
    {
    	Button button = (Button) this.getActivity().findViewById(btn);
    	button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonNumberClick(num);
			}});      	
    }
    
    protected void onButtonNumberClick(int num)
    {
    	value.getPoints().add(num);
		redraw();
    }
    
    protected void onButtonCClick()
    {
    	value.getPoints().clear();
    	this.redraw();
    }
    
    protected void onButtonOKClick()
    { 
    	if(GameActivity.getGame().getPlayers().size() > 0)
    	{
	    	GameActivity.getGame().addNextValue(value);
	    	this.resetInputValues();
			LocalBroadcastManager.getInstance(this.getActivity()).sendBroadcast(new Intent(LocalEvents.ACTION_RESULT_ADD));
    	}
    }
    
    protected void resetInputValues()
    {
    	this.value = new Value(GameActivity.getGame().nextPlayer());
    	if(this.isVisible())
    		this.redraw();
    }
}
