package com.github.anvo.piqwi.ui;

public interface LocalEvents {

	public final static String ACTION_GAME_NEW 		= LocalEvents.class.getName() + ".ACTION_GAME_NEW";

	public final static String ACTION_PLAYER_ADD 	= LocalEvents.class.getName() + ".ACTION_PLAYER_ADD";		
	public final static String ACTION_PLAYER_REMOVE = LocalEvents.class.getName() + ".ACTION_PLAYER_REMOVE";
	public final static String ACTION_PLAYER_EDIT 	= LocalEvents.class.getName() + ".ACTION_PLAYER_EDIT";
	
	public final static String ACTION_RESULT_ADD 	= LocalEvents.class.getName() + ".ACTION_RESULT_ADD";
	public final static String ACTION_RESULT_EDIT 	= LocalEvents.class.getName() + ".ACTION_RESULT_EDIT";
	
}
