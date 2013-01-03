package org.fu.swphcc.wattnglueck.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Bietet Funktionen um die Eigenschaften der App zu speichen und zu laden
 *
 * TODO: Es ist zu überlegen ob es vlt. effizienter ist, die Preferences nur einmal zu laden und dann im Heap zu speichern
 * um unnötige Dateizugriffe zu minimieren.
 * 
 * @author Necros
 *
 */
public class Preferences {
	public static final String PREFS_NAME = "wattnpref";
	private Context ctx;
	private SharedPreferences settings;
	private float defaultKwhPreis = 24; 
	
	/**
	 * the fuckin' Constructor....
	 * @param ctx
	 */
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
	public Float getGrundpreis() {
		return settings.getFloat("grundpreis", 0);
	}
	
	/**
	 * speichert bzw ändert den KWh Preis
	 * @param kwh Preis pro KWh in Cent
	 */
	public void addGrundpreis(Float preis) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("grundpreis", preis);
		
		editor.commit();
	}
	
	/**
	 * speichert bzw aendert den monatlichen Abschlag
	 * @param abschlag monatlichen Abschlag in Euro
	 */
	public void addAbschlag(Float abschlag) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("abschlag", abschlag);
		
		editor.commit();
	}
	
	/**
	 * liefert den gespeicherten Abschlag
	 * @return
	 */
	public Float getAbschlag() {
		return settings.getFloat("abschlag", 0f);
	}
	
	/**
	 * setzt Dummywerte für die Preferences
	 */
	public void setDummyValues() {
		addBeginn("2013-01-01");
		addPreis(26.88f);
		addGrundpreis(5.60f);
		addAbschlag(101f);
	}
	
	/**
	 * Gibt an, ob die Einstellungen gesetzt sind oder nicht.
	 * @return
	 */
	public boolean isSet() {
		return ((getAbschlag() != null) && (getGrundpreis() != null) && (getBeginn() != null) && (getPreis() != null));
	}
	
	/**
	 * Loescht alle Einstellungen
	 */
	public void clearPreferences() {
		SharedPreferences.Editor editor = settings.edit();
		
		editor.clear();
		editor.commit();
	}
}
