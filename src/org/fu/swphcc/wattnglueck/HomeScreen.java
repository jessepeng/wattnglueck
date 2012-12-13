package org.fu.swphcc.wattnglueck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeScreen extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		//testdaten
		setDummyValues();

		Date today = new Date();
		String todayString = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(today);
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		statusView.setText(showDate);
		statusView.setOnTouchListener(this);
		
		TextView zaehlerView = (TextView) findViewById(R.id.textZaehlerstand);
		zaehlerView.setOnTouchListener(this);
	}

	public void statusClick(View v) {
		System.out.println("Status click.");
		startActivity(new Intent(this, Status.class));
	}

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
	protected boolean showOptionsMenu() {
		return true;
	}
}
