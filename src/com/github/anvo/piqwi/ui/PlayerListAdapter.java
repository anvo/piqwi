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

import com.github.anvo.piqwi.logic.Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerListAdapter extends BaseAdapter {

	private List<Player> list = null;
	private Context context = null;
	
	public PlayerListAdapter(Context c, List<Player> list)
	{
		this.list = list;
		this.context = c;
	}
	
	@Override
	public int getCount() 
	{
		return this.list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		if(this.list.size() >= position)
			return 0;
		return this.list.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}		
		return this.populateView(position, convertView);
	}
	
	protected View populateView(int position, View convertView)
	{
		Player player = (Player)this.getItem(position);
		TextView text1 = (TextView)convertView.findViewById(android.R.id.text1);
		
		text1.setText(player.getName());
		
		return convertView;
	}

}
