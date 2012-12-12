package org.fu.swphcc.wattnglueck;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Bietet Funktionen um die Eigenschaften der App zu speichen und zu laden
 * 
 * @author Necros
 *
 */
public class Preferences {
	public static final String PREFS_NAME = "wattnpref";
	private Context ctx;
	private SharedPreferences settings;
	private float defaultKwhPreis = 12; 
	
	
	public Preferences(Context ctx) {
		this.ctx=ctx;
		settings = ctx.getSharedPreferences(PREFS_NAME, 0);	
	}
	
	/**
	 * fügt einen aktualisierten Kilowattstundenpreis in die Preferences ein
	 * @param kwhPreis der Preis für eine Kilowattstunde in Cent
	 */
	public void addPreis(Float kwhPreis) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("kwhPreis", kwhPreis);
		
		editor.commit();
	}
	
	public  Float getPreis() {
		return settings.getFloat("kwhPreis", defaultKwhPreis);
	}
	
}
