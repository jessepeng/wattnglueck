package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Preferences;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.app.AlertDialog.Builder;
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
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				Preferences pref = new Preferences(getBaseContext());
				if (!pref.isSet()) {
					OKMessageDialog vertragDialog = new OKMessageDialog("Bitte halte deinen Stromvertrag bereit.") {

						@Override
						protected void onOKAction() {
							Intent vertragIntent = new Intent(getBaseContext(), Vertrag.class);
							vertragIntent.putExtra("init", true);
							startActivity(vertragIntent);
							dismiss();
						}

						@Override
						protected void additionalBuilderOperations(
								Builder builder) {
						}
					};
					vertragDialog.show(getFragmentManager(), "vertrag");
				}

				initViews();
			}
			
		}).run();

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
	
	@Override
	public void onResume() {
		super.onResume();
		
		Preferences pref = new Preferences(getBaseContext());
		
		Date today = new Date();
		String todayString = Constants.ViewDateFormat.format(today);
		String showDate = getString(R.string.home_status).replace("$date", todayString);
		
		Database db = new Database(getBaseContext());
		List<Zaehlerstand> zaehlerList = db.getByRange(pref.getBeginn(), Constants.DBDateFormat.format(new Date()));
		boolean zaehlerVorhanden = false;
		if (zaehlerList != null) {
			Zaehlerstand zaehlerstand = zaehlerList.get(zaehlerList.size() - 1);
			if (zaehlerstand != null) {
				showDate = getString(R.string.home_status).replace("$date", Constants.ViewDateFormat.format(zaehlerstand.getDate()));
				zaehlerVorhanden = true;
			}
		}
		
		TextView statusView = (TextView) findViewById(R.id.textStatus); 
		statusView.setText(showDate);
		
		if (!zaehlerVorhanden) {
			statusView.setVisibility(View.INVISIBLE);
			findViewById(R.id.textStatusAnfang).setVisibility(View.INVISIBLE);
			findViewById(R.id.textStatusEnde).setVisibility(View.INVISIBLE);
		}
		
		
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

		return Arrays.asList(
				(TextView) findViewById(R.id.textStatus), 
				(TextView) findViewById(R.id.textZaehlerstand),
				(TextView) findViewById(R.id.textZaehler),
				(TextView) findViewById(R.id.textZaehlerstand),
				(TextView) findViewById(R.id.textZaehlerEnde),
				(TextView) findViewById(R.id.textStatusAnfang),
				(TextView) findViewById(R.id.textStatus),
				(TextView) findViewById(R.id.textStatusEnde)
				);		
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
