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

import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.logic.Player;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter{

	private Context context = null;
	private Game game = null;
	
	public ResultListAdapter(Context c, Game g)
	{
		this.context = c;
		this.game = g;
	}	
	
	@Override
	public int getCount() {
		return this.game.getResultList().size();
	}

	@Override
	public Object getItem(int position) {
		return this.game.getResultList().get(position);
	}

	@Override
	public long getItemId(int position) {
		if(this.game.getResultList().size() >= position)
			return 0;
		return this.game.getResultList().get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		}	
		
		Player player = (Player)this.getItem(position);
		
		TextView text1 = (TextView)convertView.findViewById(android.R.id.text1);
		text1.setText(Integer.toString(this.game.getResultFor(player)) + " Punkte");
		
		TextView text2 = (TextView)convertView.findViewById(android.R.id.text2);
		
		text2.setText(player.getName());
				
		
		return convertView;
	}
}
