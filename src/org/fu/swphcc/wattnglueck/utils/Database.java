package org.fu.swphcc.wattnglueck.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "wattndata";
	private static final String DATABASE_TABLE = "data";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, value REAL);";
	private Context ctx;

	public Database(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx=ctx;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Fügt einen Zählerstand zur Datenbank, als Datum wird das aktuelle verwendet
	 * 
	 * @param stand der Zählerstand in KWh
	 */
	public void addZaehlerstand(Float stand) {

		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		Date d = new Date();
		cv.put("date", Constants.DBDateFormat.format(d));
		cv.put("value", stand);
		writeDB.insert(DATABASE_TABLE, null, cv);
	}
	
	/**
	 * Ändert einen sich bereits in der Datenbank befindlichen Zählerstand
	 * @param date
	 * @param stand
	 */
	public void updateZaehlerstand(Date date, Float stand) {
		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date", Constants.DBDateFormat.format(date));
		cv.put("value", stand);
		writeDB.update(DATABASE_TABLE, cv, "date = ?", new String[] {Constants.DBDateFormat.format(date)});
	}

	/**
	 * Ändert einen sich bereits in der Datenbank befindlichen Zählerstand
	 * @param date
	 * @param stand
	 */
	public void updateZaehlerstand(String date, Float stand) {
		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("value", stand);
		writeDB.update(DATABASE_TABLE, cv, "date = ?", new String[] {date});
	}

	
	/**
	 * Holt alle Zählerstände aus der Datenbank und gibt sie Sortiert nach dem Datum zurück
	 * 
	 * @return Liste aller Zählerstände
	 */
	public List<Zaehlerstand> getAll() {
		SQLiteDatabase readDB = getReadableDatabase();
		Cursor c = readDB.query(true, DATABASE_TABLE, null, null, null, null, null, "date", null, null);
		
		if(c.getCount()>0) {
			List<Zaehlerstand> zlist = new LinkedList<Zaehlerstand>();
			while(c.moveToNext()) {
				Zaehlerstand z = new Zaehlerstand();
				z.setZaehlerstand(c.getFloat(2));
				try {
					z.setDate(Constants.DBDateFormat.parse(c.getString(1)));
				} catch (ParseException e) { 
					// vlt. Fehlerausgabe
				}
				zlist.add(z);
			}
			return zlist;
		} else 
			return null;
	}
	
	/**
	 * Holt einen Datensatz, der per Id definiert ist, aus der Datenbank und gibt ihn zurück
	 * 
	 * @param id die Id des Datensatz
	 * @return der Datensatz verpackt in ein Zählerstand Object
	 */
	public Zaehlerstand getById(Integer id) {
		SQLiteDatabase readDB = getReadableDatabase();
		String[]  args= new String[1];
		args[0]=id.toString();
		Cursor c = readDB.query(true, DATABASE_TABLE, null, "id = ?", args, null, null, null, null, null);

		if(c.getCount() > 0) {
			Zaehlerstand z = new Zaehlerstand();
			z.setZaehlerstand(c.getFloat(2));
			try {
				z.setDate(Constants.DBDateFormat.parse(c.getString(1)));
			} catch (ParseException e) { 
				// vlt. testausgabe
			}
			return z;
		} else 
			return null;
	}
	
	/**
	 * Gibt alle Zählerstände zurück die in einem Zeitraum liegen
	 * 
	 * @param von Datum im Format Constants.DBDateFormatString
	 * @param bis Datum im Format Constants.DBDateFormatString
	 * @return Liste der Zählerstände
	 * 
	 */
	public List<Zaehlerstand> getByRange(String von, String bis) {
		SQLiteDatabase readDB = getReadableDatabase();
		String[]  args= new String[2];
		args[0]=von;	
		args[1]=bis;
		
		Cursor c = readDB.query(true, DATABASE_TABLE, null, "date >= ? AND date <= ?", args, null, null, null, null, null);
		
		if(c.getCount() >= 1) {
			List<Zaehlerstand> zlist = new LinkedList<Zaehlerstand>();
			while(c.moveToNext()) {
				Zaehlerstand z = new Zaehlerstand();
				z.setZaehlerstand(c.getFloat(2));
				try {
					z.setDate(Constants.DBDateFormat.parse(c.getString(1)));
				} catch (ParseException e) { 
					// vlt. Fehlerausgabe
				}
				zlist.add(z);
			}
			return zlist;
		} else 
			return null;

	}
	
	/**
	 * Füllt die DB mit zwei Dummy Einträgen
	 */
	public void setDummyValues() {
		
		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put("date", "2012-01-01");
		cv.put("value", 71010f);
		writeDB.insert(DATABASE_TABLE, null, cv);
		cv.put("date", "2012-06-01");
		cv.put("value", 72010f);
		writeDB.insert(DATABASE_TABLE, null, cv);
		cv.put("date", "2013-01-01");
		cv.put("value", 74010f);
		writeDB.insert(DATABASE_TABLE, null, cv);
		cv.put("date", "2013-01-03");
		cv.put("value", 74019f);
		writeDB.insert(DATABASE_TABLE, null, cv);
		
	}
	
	/**
	 * Leert die Datenbank
	 */
	public void clearDatabase() {
		SQLiteDatabase writeDB = getWritableDatabase();
		writeDB.delete(DATABASE_TABLE, "1=1", null);
	}
}
