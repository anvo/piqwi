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
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.ui.GameActivity;
import com.github.anvo.piqwi.ui.PlayerListAdapter;

public class PlayersFragment extends SherlockFragment {

	private Game game = null;
	private PlayerListAdapter playersAdapter = null;
	
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
    	
    	this.game = ((GameActivity)this.getActivity()).getGame();
    	
    	this.playersAdapter = new PlayerListAdapter(this.getActivity(), this.game.getPlayers());
    	ListView players = (ListView) this.getActivity().findViewById(R.id.list_players);
    	players.setAdapter(playersAdapter);
    	
    	this.registerForContextMenu(players);    	
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
							game.getPlayers().add(new Player(name));
							playersAdapter.notifyDataSetChanged();
						}
					}
				});
    			builder.setNegativeButton(android.R.string.cancel, null);
    			builder.create().show();
    			return true;
    	}
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	this.getActivity().getMenuInflater().inflate(R.menu.list_players, menu);
    }
    
    @Override
    public boolean onContextItemSelected (android.view.MenuItem item)
    {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId())
    	{
    		case R.id.menu_list_players_delete:
    			this.game.getPlayers().remove(info.position);
    			break;
    		case R.id.menu_list_players_up:
    			if(info.position > 0)
    				this.swapPlayers(info.position -1, info.position);
    			break;
    		case R.id.menu_list_players_down:
    			if(info.position < (this.game.getPlayers().size() -1))
    				this.swapPlayers(info.position +1, info.position);
    			break;   	
    		case R.id.menu_list_players_edit:
    			final int playerId = info.position;
    			Player player = this.game.getPlayers().get(playerId);
    			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
    			final View layout = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_player, null);
    			((EditText) layout.findViewById(R.id.dialog_player_name)).setText(player.getName()); 
    			builder.setView(layout);
    			builder.setTitle("Spieler: " + player.getName());
    			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText nameText = (EditText) layout.findViewById(R.id.dialog_player_name); 
						String name = nameText.getText().toString();
						if(!name.isEmpty())
						{
							game.getPlayers().get(playerId).setName(name);
							playersAdapter.notifyDataSetChanged();
						}
					}
				});
    			builder.setNegativeButton(android.R.string.cancel, null);
    			builder.create().show();    			
    			break;
    		default:
    			return super.onContextItemSelected(item);
    	}
    	this.playersAdapter.notifyDataSetChanged();
    	return true;
    }
    
    protected void swapPlayers(int newIndex, int oldIndex)
    {
    	Player tmp = this.game.getPlayers().get(newIndex);
    	this.game.getPlayers().set(newIndex, this.game.getPlayers().get(oldIndex));
    	this.game.getPlayers().set(oldIndex, tmp);
    }
}
