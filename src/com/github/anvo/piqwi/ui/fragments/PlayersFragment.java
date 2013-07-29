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

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.ui.GameActivity;
import com.github.anvo.piqwi.ui.LocalEvents;
import com.github.anvo.piqwi.ui.PlayerListActionMode;
import com.github.anvo.piqwi.ui.PlayerListAdapter;

public class PlayersFragment extends Fragment {

	private PlayerListAdapter playersAdapter = null;
	private PlayerListActionMode action = null;
	private BroadcastReceiver broadcastReceiver = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_players, container, false);
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	this.playersAdapter = new PlayerListAdapter(this.getActivity());
    	final ListView players = (ListView) this.getActivity().findViewById(R.id.list_players);
    	players.setAdapter(playersAdapter);
    	players.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				action = new PlayerListActionMode(getActivity(), GameActivity.getGame(), playersAdapter, position,players);
				((ActionBarActivity)getActivity()).startSupportActionMode(action);
				return true;
			}});	
    	players.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if(action != null && action.isActive())
					action.updatePosition(position);
			}});
    	
    	this.broadcastReceiver   = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(GameActivity.ACTION_PAGE_SELECTED.equals(intent.getAction()))
					if(action != null && action.isActive())
						action.finish();
				if(LocalEvents.ACTION_PLAYER_ADD.equals(intent.getAction()) ||
					LocalEvents.ACTION_PLAYER_EDIT.equals(intent.getAction()) ||
					LocalEvents.ACTION_PLAYER_REMOVE.equals(intent.getAction()))
						playersAdapter.notifyDataSetChanged();
			}};
		IntentFilter eventFilter = new IntentFilter();
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_ADD);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_EDIT);
		eventFilter.addAction(LocalEvents.ACTION_PLAYER_REMOVE);
		eventFilter.addAction(GameActivity.ACTION_PAGE_SELECTED);
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, eventFilter);
    	    	
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }    
    
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
    	inflater.inflate(R.menu.fragment_players, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch(item.getItemId())
    	{
    		case R.id.menu_players_addplayer:
    			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
    			final View layout = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_player, null);
    			builder.setView(layout);
    			builder.setTitle("Neuer Spieler");
    			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText nameText = (EditText) layout.findViewById(R.id.dialog_player_name); 
						String name = nameText.getText().toString();
						if(!name.isEmpty())
						{
							GameActivity.getGame().getPlayers().add(new Player(name));
							Intent playerAddIntent = new Intent(LocalEvents.ACTION_PLAYER_ADD);
			    			LocalBroadcastManager.getInstance(PlayersFragment.this.getActivity()).sendBroadcast(playerAddIntent);
						}
					}
				});
    			builder.setNegativeButton(android.R.string.cancel, null);
    			builder.create().show();
    			return true;
    	}
        return false;
    }
}
