package org.fu.swphcc.wattnglueck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.TextSwitcher;

public class HomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		//testdaten
		setDummyValues();

		Date today = new Date();
		String todayString = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(today);
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		String showMoechteStatus = getString(R.string.home_moechte_status).replace("$date", todayString);
		
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		statusView.setText(showDate);

		TextView moechteStatusView = (TextView) findViewById(R.id.textMoechteStatus);
		moechteStatusView.setText(showMoechteStatus);
		
		TextView fontView = (TextView) findViewById(R.id.textFont);
		Typeface customFont = Typeface.createFromAsset(getAssets(), "MankSans-Medium.ttf");
		fontView.setTypeface(customFont);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_screen, menu);
		return true;
	}

	public void statusClick(View v) {
		startActivity(new Intent(this, Status.class));
	}
	
	public void switchClick(View v) {
		((TextSwitcher) v).showNext();
	}

	public void setDummyValues() {
		//testdaten setzen
		Database db = new Database(this);
		db.setDummyValues();
		Preferences p = new Preferences(this);
		p.setDummyValues();
	}
}
