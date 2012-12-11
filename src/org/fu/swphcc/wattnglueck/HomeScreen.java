package org.fu.swphcc.wattnglueck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class HomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hiermit kann auf die Datenbank zugegriffen werden
		Database db = new Database(this);
		
		Date today = new Date();
		String todayString = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(today);
		setContentView(R.layout.activity_home_screen);
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		statusView.setText(showDate);
		TextView fontView = (TextView) findViewById(R.id.textFont);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "MankSans-Medium.ttf");
		fontView.setTypeface(customFont);
		TextView test = (TextView) findViewById(R.id.testausgabe); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_screen, menu);
		return true;
	}
	
	public void onClick(View v) {
		
	}

}
