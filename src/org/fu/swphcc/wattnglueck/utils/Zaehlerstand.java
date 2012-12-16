package org.fu.swphcc.wattnglueck.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

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

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static Float getEstimatedCoverage(Context ctx) {
		Preferences p = new Preferences(ctx);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
		Database db = new Database(ctx);
		//da es leider in SQLite keine Date Typen gibt müssen wir das Sortieren und heraussuchen hier machen
		List<Zaehlerstand> zlist = db.getAll();
		Collections.sort(zlist);
		Date vbeginn = null;
		Date vende = null;
		
		try {
			vbeginn = sdf.parse(p.getBeginn());
			vende = vbeginn;
			vende.setYear(vbeginn.getYear()+1);
		} catch (ParseException e) {	}
		
		Zaehlerstand start = null;

		for(Zaehlerstand z : zlist) {
			if(z.getDate().compareTo(vbeginn)>=0) {
				start= z;
				break;
			}
		}

		Zaehlerstand ende = null;
		
		if(start!=null) {
			ListIterator<Zaehlerstand> li = zlist.listIterator();
			while(li.hasPrevious()) {
				Zaehlerstand z=li.previous();
				if(z.getDate().compareTo(vende)<=0) {
					ende = z;
					break;
				}
			}
			if(ende!=null) {
				Float delta = ende.Zaehlerstand - start.Zaehlerstand;
				long zeit = ende.date.getTime() - start.date.getTime();
				float tage = zeit / 86400000f;
				Float estimation = delta / tage * 365f;
				return estimation;
			}
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
