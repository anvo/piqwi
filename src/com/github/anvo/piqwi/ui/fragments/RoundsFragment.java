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

import java.util.List;

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
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.ui.CopyWidthTableRow;
import com.github.anvo.piqwi.ui.GameActivity;
import com.github.anvo.piqwi.ui.LocalEvents;
import com.github.anvo.piqwi.ui.RoundListAdapter;

public class RoundsFragment extends SherlockFragment {

	private BroadcastReceiver broadcastReceiver = null;
	private ListView list = null;
	private TableRow header = null;
	private CopyWidthTableRow footer = null;
	private RoundListAdapter listAdapter = null;
		
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rounds, container, false);
        this.list = (ListView) v.findViewById(R.id.rounds_table_body);
        this.header = (TableRow) v.findViewById(R.id.rounds_table_header_row);
        this.footer = (CopyWidthTableRow) v.findViewById(R.id.rounds_table_footer_row);
        this.footer.setSource(this.header);
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	this.setRetainInstance(true);

    	this.listAdapter = new RoundListAdapter(this.getActivity(), this.header);
        list.setAdapter(this.listAdapter);    	

    	this.update();  
    	
    	//Receive local events
    	this.broadcastReceiver   = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				update();
			}};
		IntentFilter filter =  new IntentFilter();
		filter.addAction(LocalEvents.ACTION_GAME_NEW);
		filter.addAction(LocalEvents.ACTION_PLAYER_ADD);
		filter.addAction(LocalEvents.ACTION_PLAYER_EDIT);
		filter.addAction(LocalEvents.ACTION_PLAYER_REMOVE);
		filter.addAction(LocalEvents.ACTION_RESULT_ADD);
		filter.addAction(LocalEvents.ACTION_RESULT_EDIT);
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, filter);
    }
       
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }

    public void update()
    {
		this.updateHeader();
		this.listAdapter.notifyDataSetChanged();
		this.updateFooter();
    }
    
    protected void updateHeader()
    {
    	List<Player> players = GameActivity.getGame().getPlayers();
    	
    	//Update current elements
    	for(int i=0; i < players.size(); i++)
    	{
    		Player p = players.get(i);
    		TextView v = (TextView) this.header.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.getActivity(),null, R.attr.tableRoundsHeaderRef);
    			this.header.addView(v);
    		}
    		v.setText(p.getName());
    	}
    	//Delete remaining elements
    	for(int i= players.size(); i < this.header.getVirtualChildCount()-1; i++)
    		this.header.removeViewAt(i+1); 
    }
    
    protected void updateFooter()
    {
    	List<Player> players = GameActivity.getGame().getPlayers();
    	
    	//Update current elements
    	for(int i=0; i < players.size(); i++)
    	{
    		Player p = players.get(i);
    		TextView v = (TextView) this.footer.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.getActivity(),null, R.attr.tableRoundsFooterRef);
    			this.footer.addView(v);
    		}
    		v.setText(Integer.toString(GameActivity.getGame().getResultFor(p)));
    	}
    	//Delete remaining elements
    	for(int i = players.size(); i < this.footer.getVirtualChildCount()-1; i++)
    		this.footer.removeViewAt(i+1);
    }
}
