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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.logic.Round;
import com.github.anvo.piqwi.logic.Value;
import com.github.anvo.piqwi.ui.GameActivity;

public class RoundsFragment extends SherlockFragment {
	
	private TableLayout table = null;
	private Game game = null;
	private BroadcastReceiver broadcastReceiver = null;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rounds, container, false);
        this.table = (TableLayout) v.findViewById(R.id.rounds_table);
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	this.game = ((GameActivity)this.getActivity()).getGame();
    	this.updateTable();
    	
    	//Receive game restart intent
    	this.broadcastReceiver   = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(GameActivity.ACTION_GAME_RESTART.equals(intent.getAction()))
					RoundsFragment.this.updateTable();
			}};
    	LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter(GameActivity.ACTION_GAME_RESTART));
    	    	
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(this.broadcastReceiver);
    }
    
    public void update()
    {    	
    	if(this.table != null)
    		this.updateTable();
    }
    
    private void updateTable()
    {
    	this.updateTableHeader();
    	
    	for(int i=0; i < this.game.getRounds().size(); i++)
    	{
    		TableRow row = (TableRow) this.table.getChildAt(i +1); //First row is header
    		if(row == null || this.table.getChildCount() <= 2+i) //Header + Footer
    		{
    			row = new TableRow(this.getActivity());
    			TableRow footer = (TableRow) this.table.getChildAt(this.table.getChildCount() -1);
    			this.table.removeView(footer);
    			this.table.addView(row);
    			this.table.addView(footer);
    		}
    		this.updateTableRow(row, this.game.getRounds().get(i),i);
    	}
    	//Delete remaining rows
    	final int rowsVisible = this.table.getChildCount() - 2; //Exclude header, footer
    	if(rowsVisible > this.game.getRounds().size())
    		this.table.removeViews(this.game.getRounds().size() +1, rowsVisible - this.game.getRounds().size());
    	
    	this.updateTableFooter();
    }
    
    private void updateTableFooter() {
    	TableRow footer = (TableRow) this.table.findViewById(R.id.rounds_footer);
    	
    	//Update current elements
    	for(int i=0; i < this.game.getPlayers().size(); i++)
    	{
    		Player p = this.game.getPlayers().get(i);
    		TextView v = (TextView) footer.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.getActivity(), null, R.attr.tableRoundsFooterRef);
    			footer.addView(v);
    		}
    		v.setText(Integer.toString(this.game.getResultFor(p)));
    	}
    	//Delete remaining elements
    	final int playersVisible =  footer.getVirtualChildCount()-1;
    	if(playersVisible > this.game.getPlayers().size())
    		footer.removeViews(playersVisible + 1, playersVisible - playersVisible);
	}

	private void updateTableHeader()
    {
    	TableRow header = (TableRow) this.table.findViewById(R.id.rounds_header);
    	
    	//Update current elements
    	for(int i=0; i < this.game.getPlayers().size(); i++)
    	{
    		Player p = this.game.getPlayers().get(i);
    		TextView v = (TextView) header.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.getActivity(),null, R.attr.tableRoundsHeaderRef);
    			header.addView(v);
    		}
    		v.setText(p.getName());
    	}
    	//Delete remaining elements
    	for(int i=this.game.getPlayers().size(); i < header.getVirtualChildCount()-1; i++)
    		header.removeViewAt(i+1); 
    }
	
	private void updateTableRow(TableRow row, Round round, int index)
	{
		TextView numRound = (TextView) row.getVirtualChildAt(0);
		if(numRound == null)
		{
			numRound = new TextView(this.getActivity(),null, R.attr.tableRoundsRowFirstRef);
			row.addView(numRound);
		}
		numRound.setText(Integer.toString(index + 1));

    	//Update current elements
    	for(int i=0; i < this.game.getPlayers().size(); i++)
    	{
    		Player p = this.game.getPlayers().get(i);
    		Value value = round.getValueFor(p);
    		TextView v = (TextView) row.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.getActivity(), null, R.attr.tableRoundsRowRef);
    			row.addView(v);
    		}
    		if(value != null)
    			v.setText(Integer.toString(value.getSum()));
    		else
    			v.setText("");
    	}
    	//Delete remaining elements
    	final int playersVisible =  row.getVirtualChildCount()-1;
    	if(playersVisible > this.game.getPlayers().size())
    		row.removeViews(playersVisible + 1, playersVisible - playersVisible);    	
	}

}
