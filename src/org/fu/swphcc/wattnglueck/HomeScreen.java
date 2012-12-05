package org.fu.swphcc.wattnglueck;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class HomeScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Date today = new Date();
		String todayString = new SimpleDateFormat("dd.MM.yyyy").format(today);
		setContentView(R.layout.activity_home_screen);
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		statusView.setText(showDate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_screen, menu);
		return true;
	}

}
