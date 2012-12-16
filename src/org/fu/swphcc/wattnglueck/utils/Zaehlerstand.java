package org.fu.swphcc.wattnglueck.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;

/**
 * Bietet die Möglichkeit Zählerstände zu speichern und 
 * zusätzlich diverse statische Methoden um Zählerstände zu verarbeiten
 * 
 * @author Necros
 *
 */
public class Zaehlerstand implements Comparable{

	private Float Zaehlerstand;
	private Date date;

	/**
	 * Berechnet den Erwarteten Verbrauch in KWh
	 * Die Funktion benutzt im Moment deprecated Funktions, was in diesem Fall aber nicht weiter schlimm ist
	 * 
	 * @param ctx der Context der App bzw der DB
	 * @return die Erwarteten KWh als Float
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static Float getEstimatedCoverage(Context ctx) {
		Preferences p = new Preferences(ctx);
		Database db = new Database(ctx);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(Constants.DBDateFormat.parse(p.getBeginn()));

			c.add(Calendar.DATE, 1);

			//Zählerstände holen
			List<Zaehlerstand> zlist = db.getByRange(p.getBeginn(),Constants.DBDateFormat.format(c.getTime()));

			if(zlist.size()>0) {

				Zaehlerstand start = zlist.get(0);
				Zaehlerstand ende = zlist.get(zlist.size()-1);

				if(ende!=null) {
					Float delta = ende.Zaehlerstand - start.Zaehlerstand;
					long zeit = ende.date.getTime() - start.date.getTime();
					float tage = zeit / 86400000f;
					Float estimation = delta / tage * 365f;
					return estimation;
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Float getZaehlerstand() {
		return Zaehlerstand;
	}
	public void setZaehlerstand(Float zaehlerstand) {
		Zaehlerstand = zaehlerstand;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(Object o) {
		//o sollte immer instanceof Zahlerstand sein...
		if(o instanceof Zaehlerstand) {
			Zaehlerstand z = (Zaehlerstand) o;
			return this.date.compareTo(z.date);
		} else return 1;


	}
}
