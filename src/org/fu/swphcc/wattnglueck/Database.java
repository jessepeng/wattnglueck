package org.fu.swphcc.wattnglueck;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

	public void addZaehlerstand(Float stand) {

		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
		Date d = new Date();
		cv.put("date", date.format(d));
		cv.put("value", stand);
		writeDB.insert(DATABASE_NAME, null, cv);
	}

	public List<Zaehlerstand> getAll() {
		SQLiteDatabase readDB = getReadableDatabase();
		Cursor c = readDB.query(true, "data", null, null, null, null, null, null, null, null);

		if(c.getCount()>0) {
			List<Zaehlerstand> zlist = new LinkedList<Zaehlerstand>();
			while(c.moveToNext()) {
				SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
				Zaehlerstand z = new Zaehlerstand();
				z.setZaehlerstand(c.getFloat(2));
				try {
					z.setDate(date.parse(c.getString(1)));
				} catch (ParseException e) { 
					// vlt. testausgabe
				}
				zlist.add(z);
			}
			return zlist;
		} else 
			return null;
	}
	
	public Zaehlerstand getById(Integer id) {
		SQLiteDatabase readDB = getReadableDatabase();
		String[]  args= new String[1];
		args[0]=id.toString();
		Cursor c = readDB.query(true, "data", null, "id = ?", args, null, null, null, null, null);

		if(c.getCount()>0) {
			SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
			Zaehlerstand z = new Zaehlerstand();
			z.setZaehlerstand(c.getFloat(2));
			try {
				z.setDate(date.parse(c.getString(1)));
			} catch (ParseException e) { 
				// vlt. testausgabe
			}
			return z;
		} else 
			return null;
	}
	
	public void setDummyValues() {
		
		SQLiteDatabase writeDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
		Date d = new Date();
		cv.put("date", "01.01.2011");
		cv.put("value", 123000f);
		writeDB.insert(DATABASE_NAME, null, cv);
		cv.put("date", date.format(d));
		cv.put("value", 123000f);
		writeDB.insert(DATABASE_NAME, null, cv);
		
	}
	
}
