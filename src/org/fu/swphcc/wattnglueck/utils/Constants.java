package org.fu.swphcc.wattnglueck.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Diese Klasse stellt Konstanten für die App bereit die oft verwendet werden
 * 
 * @author Necros
 *
 */
public class Constants {
	
	//FormatString für die Dates in der Datenbank
	public static String DBDateFormatString = "yyyy-MM-dd";
	//FormatString für die Dates die angezeigt werden
	public static String ViewDateFormatString = "dd.MM.yyyy";
	
	//und das Ganze als SimpleDateFormat
	public static SimpleDateFormat DBDateFormat = new SimpleDateFormat(DBDateFormatString,Locale.GERMAN);
	public static SimpleDateFormat ViewDateFormat = new SimpleDateFormat(ViewDateFormatString,Locale.GERMAN);
	
	
	
}
