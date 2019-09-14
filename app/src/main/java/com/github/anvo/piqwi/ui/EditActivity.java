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

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Round;
import com.github.anvo.piqwi.logic.Value;
import com.github.anvo.piqwi.ui.fragments.EditInputFragment;

public class EditActivity extends AppCompatActivity
{
	public static final String EXTRA_ROUND = EditActivity.class.getName() + ".ROUND";
	public static final String EXTRA_PLAYER = EditActivity.class.getName() + ".PLAYER";
	
	private static final String TAG = EditActivity.class.getName();
	private static final int INVALID = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_edit);
        this.setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        int playerIndex = this.getIntent().getIntExtra(EXTRA_PLAYER, INVALID);
        if(playerIndex == INVALID)
        {
        	Log.e(TAG, "No EXTRA_PLAYER given");
        	this.finish();
        	return;
        }
        
        int roundIndex = this.getIntent().getIntExtra(EXTRA_ROUND, INVALID);
        if(roundIndex == INVALID)
        {
        	Log.e(TAG, "No EXTRA_ROUND given");
        	this.finish();
        	return;
        }        
        
        Round round = GameActivity.getGame().getRounds().get(roundIndex);
        Value value = round.getValueFor(GameActivity.getGame().getPlayers().get(playerIndex));
        if(value == null)
        {
        	Log.e(TAG, "No value found");
        	this.finish();
        	return;
        }
        EditInputFragment input = (EditInputFragment) this.getSupportFragmentManager().findFragmentById(R.id.activity_edit_input);
        input.setValue(value);
        input.setRound(roundIndex + 1);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }    

}
