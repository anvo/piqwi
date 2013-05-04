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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.ui.GameActivity;
import com.github.anvo.piqwi.ui.ResultListAdapter;

public class ResultFragment extends SherlockListFragment
{
    private Game game = null;
    private ResultListAdapter resultAdapter = null;
	private BroadcastReceiver broadcastReceiver = null;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }	
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	this.game  = ((GameActivity)this.getActivity()).getGame();
    	
    	ListView results = (ListView) this.getActivity().findViewById(android.R.id.list);
    	this.resultAdapter = new ResultListAdapter(this.getActivity(), this.game);
    	results.setAdapter(this.resultAdapter);
    	
    	//Receive game restart intent
    	this.broadcastReceiver  = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(GameActivity.ACTION_GAME_RESTART.equals(intent.getAction()))
					ResultFragment.this.resultAdapter.notifyDataSetChanged();
			}};
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter(GameActivity.ACTION_GAME_RESTART));
    	
    }
    
    public void onStop ()
    {
    	super.onStop();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }
}