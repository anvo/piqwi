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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.anvo.piqwi.R;
import com.github.anvo.piqwi.logic.Game;
import com.github.anvo.piqwi.logic.Player;
import com.github.anvo.piqwi.ui.fragments.InputFragment;
import com.github.anvo.piqwi.ui.fragments.PlayersFragment;
import com.github.anvo.piqwi.ui.fragments.ResultFragment;
import com.github.anvo.piqwi.ui.fragments.RoundsFragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends SherlockFragmentActivity {
	
	public final static int PLAYERS_TAB = 0;
	public final static int INPUT_TAB = 1;
	public final static int ROUNDS_TAB = 2;
	public final static int RESULT_TAB = 3;
	
	private final String GAME_SAVE_FILE = "GameSave";
	
	public final static String ACTION_GAME_RESTART = "com.github.anvo.piqwi.ACTION_GAME_RESTART";
	public final static String ACTION_PAGE_SELECTED = "com.github.anvo.piqwi.ACTION_PAGE_SELECTED";
	
	public final static String EXTRA_PAGE_POSITION = "com.github.anvo.piqwi.EXTRA_PAGE_POSITION";
	
	private Game game = new Game();
	

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				Intent pageSelected = new Intent(GameActivity.ACTION_PAGE_SELECTED);
				pageSelected.putExtra(GameActivity.EXTRA_PAGE_POSITION, position);
				LocalBroadcastManager.getInstance(GameActivity.this).sendBroadcastSync(pageSelected);
			}});
        
        this.loadGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getSupportMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    		case R.id.menu_restart:
    			Builder builder = new AlertDialog.Builder(this);
    			builder.setTitle("Neustart");
    			builder.setMessage("Das Spiel neu starten?");
    			builder.setIcon(android.R.drawable.ic_dialog_alert);
    			builder.setPositiveButton(android.R.string.yes, new OnClickListener(){
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						GameActivity.this.game.reset();
		    			Intent restartIntent = new Intent(GameActivity.ACTION_GAME_RESTART);
		    			LocalBroadcastManager.getInstance(GameActivity.this).sendBroadcast(restartIntent);
					}});
    			builder.setNegativeButton(android.R.string.no, null);
    			builder.show();    			
    		return true;
    		case R.id.menu_share:
    			Intent shareIntent = new Intent(Intent.ACTION_SEND);
    			shareIntent.setType("text/plain");
    			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PiQwi Ergebnis");
    			shareIntent.putExtra(Intent.EXTRA_TEXT, this.getShareText());
    			this.startActivity(Intent.createChooser(shareIntent, "Share"));
    		return true;
    		case R.id.menu_settings:
    			Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
    		return true;    		
    	}
        return false;
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	this.saveGame();
    }

    public Game getGame()
    {
    	return this.game;
    }
    
    protected void saveGame()
    {
    	try {
    		FileOutputStream fileOut = this.openFileOutput(GAME_SAVE_FILE, MODE_PRIVATE);
    		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
    		objectOut.writeObject(this.game);
    		objectOut.flush();
    		objectOut.close();
    		fileOut.flush();
    		fileOut.close();
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getLocalizedMessage(), e);
		}
    }
    
    protected void loadGame()
    {
    	try {
			FileInputStream fileIn = this.openFileInput(GAME_SAVE_FILE);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			this.game = (Game) objectIn.readObject();
			objectIn.close();
			fileIn.close();
    	} catch(FileNotFoundException f) {
    		//No problem! 
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getLocalizedMessage(), e);
		}
    }
    
    protected String getShareText()
    {
    	StringBuffer buffer = new StringBuffer();
    	
    	buffer.append("Das Ergebnis nach ");
    	buffer.append(this.game.getRounds().size());
    	buffer.append(" Runden:\n\n");
    	for(Player p: this.game.getResultList())
    	{
    		buffer.append(game.getResultFor(p));
    		buffer.append(" Punkt - ");
    		buffer.append(p.getName());
    		buffer.append("\n");
    	}
    	
    	buffer.append("\n PiQwi v" + this.getVersion());
    	
    	return buffer.toString();
    }

    protected String getVersion()
    {
    	try {
			return this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "0";
		}
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

    	private LinkedList<Fragment> fragments = new LinkedList<Fragment>();
    	
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments.add(new PlayersFragment());
            this.fragments.add(new InputFragment());            
            this.fragments.add(new RoundsFragment());
            this.fragments.add(new ResultFragment());
        }

        @Override
        public Fragment getItem(int i) {
        	return this.fragments.get(i);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PLAYERS_TAB: return getString(R.string.title_players).toUpperCase();
                case INPUT_TAB: return getString(R.string.title_input).toUpperCase();
                case ROUNDS_TAB: return getString(R.string.title_rounds).toUpperCase();
                case RESULT_TAB: return getString(R.string.title_result).toUpperCase();
            }
            return null;
        }
    }
}