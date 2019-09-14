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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.view.ActionMode;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.logic.Player;

public class PlayerListActionMode implements ActionMode.Callback
{
	private Game game = null;
	private PlayerListAdapter playersAdapter = null;
	private Activity activity = null;
	private ListView list;
	
	private int position; //Index of player to edit
	
	private boolean isActive = true;
	
	private ActionMode mode = null;
	
	public PlayerListActionMode(Activity a, Game g, PlayerListAdapter p, int position, ListView list)
	{
		this.game = g;
		this.playersAdapter = p;
		this.activity = a;
		this.position = position;
		this.list = list;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) 
	{
		mode.getMenuInflater().inflate(R.menu.list_players, menu);
		// Workaround for Android Issue 159527
		// https://code.google.com/p/android/issues/detail?id=159527
		// Method onPrepareActionMode is no longer called after onCreateActionMode
		// in Android > 5.0 and appcompat > v22.1
		// For now, we will call it by hand
		this.onPrepareActionMode(mode, menu);

		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) 
	{
		mode.setTitle(game.getPlayers().get(position).getName());
		this.mode = mode;
		this.playersAdapter.setSelectionMode(true);
		this.list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.list.setItemChecked(this.position, true);	
		return true;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
	{
		switch(item.getItemId())
		{
			case R.id.menu_list_players_delete:
			{
    			game.getPlayers().remove(position);
    			mode.finish();
    			Intent playerRemoveIntent = new Intent(LocalEvents.ACTION_PLAYER_REMOVE);
    			LocalBroadcastManager.getInstance(this.activity).sendBroadcast(playerRemoveIntent);
    			break;
			}
    		case R.id.menu_list_players_up:
    		{
    			int next = position - 1;
    			if(next < 0)
    				next = this.game.getPlayers().size() -1;
   				swapPlayers(next, position);
   				this.updatePosition(next);
    			break;
    		}
			case R.id.menu_list_players_down:
			{
				int next = position + 1;
    			if(next > (game.getPlayers().size() -1))
    				next = 0;
    			swapPlayers(next, position);
    			this.updatePosition(next);
    			break;
			}
			case R.id.menu_list_players_edit:
			{
    			final int playerId = position;
    			Player player = game.getPlayers().get(playerId);
    			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    			final View layout = activity.getLayoutInflater().inflate(R.layout.dialog_player, null);
    			((EditText) layout.findViewById(R.id.dialog_player_name)).setText(player.getName()); 
    			builder.setView(layout);
    			builder.setTitle(activity.getString(R.string.dialog_players_edit_title, player.getName()));
    			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText nameText = (EditText) layout.findViewById(R.id.dialog_player_name); 
						String name = nameText.getText().toString();
						if(!name.isEmpty())
						{
							game.getPlayers().get(playerId).setName(name);
			    			Intent playerEditIntent = new Intent(LocalEvents.ACTION_PLAYER_EDIT);
			    			LocalBroadcastManager.getInstance(activity).sendBroadcast(playerEditIntent);  
						}
					}
				});
    			builder.setNegativeButton(android.R.string.cancel, null);
    			builder.create().show();    	
    			mode.finish();
    			break;		
			}
		}		
		playersAdapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) 
	{
		this.isActive = false;		
		this.playersAdapter.setSelectionMode(false);
		this.list.setItemChecked(this.position, false);
		this.list.setChoiceMode(ListView.CHOICE_MODE_NONE);
		
		//If the list is set to CHOICE_MODE_NONE, some entries remain checked. Looks
		// like a bug in the ListView. To avoid the bug, all entries are reset by hand.
		for(int i=0; i < this.list.getChildCount(); i++)
			if(this.list.getChildAt(i) instanceof Checkable)
				((Checkable)this.list.getChildAt(i)).setChecked(false);
		
	}
	
	public boolean isActive()
	{
		return this.isActive;
	}
	
	/**
	 * Update the current element the class is working on
	 */
	public void updatePosition(int position)
	{
		this.position = position;
		this.mode.invalidate();
		this.list.setItemChecked(this.position, true);
	}
	
	public void finish()
	{
		this.mode.finish();
	}
	
    protected void swapPlayers(int newIndex, int oldIndex)
    {
    	Player tmp = this.game.getPlayers().get(newIndex);
    	this.game.getPlayers().set(newIndex, this.game.getPlayers().get(oldIndex));
    	this.game.getPlayers().set(oldIndex, tmp);
		Intent playerEditIntent = new Intent(LocalEvents.ACTION_PLAYER_EDIT);
		LocalBroadcastManager.getInstance(this.activity).sendBroadcast(playerEditIntent);        	
    }	

}
