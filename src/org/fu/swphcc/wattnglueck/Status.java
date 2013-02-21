package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Status extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Float exactBetrag = Zaehlerstand.getEstimatedBilling(this);
		if (exactBetrag != null) {
			Integer betrag = Math.round(exactBetrag);
			String betragText;

			if (betrag < 0) {
				betrag = -betrag;
				View me = findViewById(R.id.textStatusWrap);
				me.setBackgroundResource(R.drawable.red_background);
				betragText = getString(R.string.status_nachzahlung_start);

			} else {
				View me = findViewById(R.id.textStatusWrap);
				me.setBackgroundResource(R.drawable.green_background);
				betragText = getString(R.string.status_rueckzahlung_start);

			}
			TextView statusAnfang = (TextView) findViewById(R.id.textStatusZahlungAnfang);
			betragText = betragText.replace("$betrag", betrag.toString());
			statusAnfang.setText(betragText);
			
			initViews();
		} else {
			// Kein Ergebnis von Berechnung (null)
			NavUtils.navigateUpFromSameTask(this);
		}
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return Arrays.asList(
				(TextView) findViewById(R.id.textStatusDetails),
				(TextView) findViewById(R.id.textStatusDetailsAnfang),
				(TextView) findViewById(R.id.textStatusDetailsEnde),
				(TextView) findViewById(R.id.textStatusZahlungAnfang)
				);
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return Arrays.asList((TextView) findViewById(R.id.textStatusDetails));
	}

	@Override
	protected boolean showOptionsMenu() {
		return true;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case R.id.textStatusDetails:
			startActivity(new Intent(this, Details.class));
			return true;
		}
		return false;
	}

}
