package org.fu.swphcc.wattnglueck.utils;

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
	
	/**
	 * liefert den Momentan gesetzten Kwh-Preis
	 * @return kwh/h in Cent
	 */
	public Float getPreis() {
		return settings.getFloat("kwhPreis", defaultKwhPreis);
	}
	
	/**
	 * Setzt den Beginn des Abrechnungszeitraums
	 * Überprüfung ob der Parameter im richtigen Format ist fehlt noch
	 * @param date Datum im Format "dd.MM.YYY"
	 */
	public void addBeginn(String date) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("beginn", date);
		
		editor.commit();
	}
	
	/**
	 * gibt das Startdatum des Abrechnungszeitraums zurück
	 * @return Datum als String im Format "dd.MM.YYY"
	 */
	public String getBeginn() {	
		return settings.getString("beginn", null);
	}
	
	/**
	 * liefert die gesetzten KWH des Vertrags
	 * @return kwh
	 */
	public Integer getKwh() {
		return settings.getInt("kwh", 0);
	}
	
	public void addKwh(Integer kwh) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("kwh", kwh);
		
		editor.commit();
	}
	
	/**
	 * setzt Dummywerte für die Preferences
	 */
	public void setDummyValues() {
		addBeginn("01.01.2011");
		addPreis(45f);
		addKwh(1600);
	}
}
