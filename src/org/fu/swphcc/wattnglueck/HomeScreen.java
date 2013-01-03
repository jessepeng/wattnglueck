package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class HomeScreen extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
//		
//		//testdaten
//		setDummyValues();

		Date today = new Date();
		String todayString = Constants.ViewDateFormat.format(today);
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		statusView.setText(showDate);
		
		initViews();
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case R.id.textStatus:
			startActivity(new Intent(this, Status.class));
			break;
		case R.id.textZaehlerstand:
			startActivity(new Intent(this, ZaehlerstandChooseMethodDialog.class));
			break;
		}
		return true;
	}

	/**
	 * 
	 * es sollten vlt besser JUnit test verwendet werden
	 * 
	 */
	public void setDummyValues() {
		//testdaten setzen
		Database db = new Database(this);
		db.setDummyValues();
		Preferences p = new Preferences(this);
		p.setDummyValues();
	}


	@Override
	protected List<TextView> getTextViewsForFont() {
		return null;
	}
	
	@Override
	protected List<TextView> getButtonTextViews() {
		return Arrays.asList((TextView) findViewById(R.id.textStatus), (TextView) findViewById(R.id.textZaehlerstand));
	}

	@Override
	protected boolean showOptionsMenu() {
		return true;
	}
}
