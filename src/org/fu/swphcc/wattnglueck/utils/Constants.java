package org.fu.swphcc.wattnglueck.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Diese Klasse stellt Konstanten f�r die App bereit die oft verwendet werden
 * 
 * @author Necros
 *
 */
public class Constants {
	
	//FormatString f�r die Dates in der Datenbank
	public static String DBDateFormatString = "yyyy-MM-dd";
	//FormatString f�r die Dates die angezeigt werden
	public static String ViewDateFormatString = "dd.MM.yyyy";
	
	//und das Ganze als SimpleDateFormat
	public static SimpleDateFormat DBDateFormat = new SimpleDateFormat(DBDateFormatString,Locale.GERMAN);
	public static SimpleDateFormat ViewDateFormat = new SimpleDateFormat(ViewDateFormatString,Locale.GERMAN);
	
	
	
}
