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

import java.util.List;

import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.logic.Round;
import com.github.anvo.piqwi.logic.Value;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RoundListAdapter extends BaseAdapter {

	private Context context = null;
	private TableRow referenceRow = null;

	public RoundListAdapter(Context c, TableRow r)
	{
		this.context = c;
		this.referenceRow = r;
	}
	@Override
	public int getCount() 
	{
		return GameActivity.getGame().getRounds().size();
	}

	@Override
	public Object getItem(int position) 
	{
		return GameActivity.getGame().getRounds().get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		if(position <= this.getCount())
			return this.getItem(position).hashCode();
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			TableLayout table  = (TableLayout) inflater.inflate(R.layout.fragment_rounds_body_item, parent, false);
			CopyWidthTableRow row = (CopyWidthTableRow) table.getChildAt(0);
			row.setSource(this.referenceRow);
			convertView = table;
		}				
		return this.populateView(position, (TableLayout) convertView);
	}
	
	protected View populateView(final int position, TableLayout convertView)
	{
		final List<Player> players = GameActivity.getGame().getPlayers();
		Round round = GameActivity.getGame().getRounds().get(position);
		TableRow row = (TableRow) convertView.getChildAt(0);
		
		TextView roundView = (TextView) row.getVirtualChildAt(0);
		roundView.setText(Integer.toString(position + 1));
		
		//Update current elements
    	for(int i=0; i < players.size(); i++)
    	{
    		final Player p = players.get(i);
    		Value value = round.getValueFor(p);
    		TextView v = (TextView) row.getVirtualChildAt(i + 1);//First element is not used
    		if(v == null)
    		{
    			v = new TextView(this.context, null, R.attr.tableRoundsFooterRef);
    			row.addView(v);
    		}
    		if(value == null)
    			v.setText("");
    		else
    			v.setText(Integer.toString(value.getSum()));
    		
    		v.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Intent openEdit = new Intent(context, EditActivity.class);
					openEdit.putExtra(EditActivity.EXTRA_ROUND, position);
					openEdit.putExtra(EditActivity.EXTRA_PLAYER, players.indexOf(p));
					context.startActivity(openEdit);
					return true;
				}});    		
    	}
    	//Delete remaining elements
    	for(int i = players.size(); i < row.getVirtualChildCount()-1; i++)
    		row.removeViewAt(i+1); 		

    	return convertView;
	}

}
