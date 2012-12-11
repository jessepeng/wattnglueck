package org.fu.swphcc.wattnglueck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "wattndata";
	private static final String DATABASE_TABLE = "data";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, value REAL);";

	public Database(Context ctx) {
		 super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
