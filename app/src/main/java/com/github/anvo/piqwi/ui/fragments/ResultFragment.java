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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.ui.LocalEvents;
import com.github.anvo.piqwi.ui.ResultListAdapter;

public class ResultFragment extends ListFragment
{
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
    	
    	ListView results = (ListView) this.getActivity().findViewById(android.R.id.list);
    	this.resultAdapter = new ResultListAdapter(this.getActivity());
    	results.setAdapter(this.resultAdapter);
    	
    	//Receive game restart intent
    	this.broadcastReceiver  = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
					ResultFragment.this.resultAdapter.notifyDataSetChanged();
			}};
		IntentFilter eventFilter = new IntentFilter();
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_ADD);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_EDIT);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_REMOVE);
		eventFilter.addAction(LocalEvents.ACTION_GAME_NEW);
		eventFilter.addAction(LocalEvents.ACTION_RESULT_ADD);
		eventFilter.addAction(LocalEvents.ACTION_RESULT_EDIT);
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, eventFilter);
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }
}
