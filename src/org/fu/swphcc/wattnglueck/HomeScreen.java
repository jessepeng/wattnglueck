package org.fu.swphcc.wattnglueck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class HomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hiermit kann auf die Datenbank zugegriffen werden
		Database db = new Database(this);
		
		//test
		SQLiteDatabase writeDB = db.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date" , "test");
		cv.put("value", "123456");
		writeDB.insert("data", null, cv);
		
		Cursor c = writeDB.query(true, "data", null, null, null, null, null, null, null, null);
		
		Date today = new Date();
		String todayString = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(today);
		setContentView(R.layout.activity_home_screen);
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		statusView.setText(showDate);
		
		TextView test = (TextView) findViewById(R.id.testausgabe); 
		
		if(c.getCount()>0) {
			c.moveToNext();
			test.setText(c.getString(1)+ " " + c.getDouble(2));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_screen, menu);
		return true;
	}

}
