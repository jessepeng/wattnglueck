package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.DialogInterface;
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
			String meldungText;
			if (betrag < 0) {
				betrag = -betrag;
				View me = findViewById(android.R.id.content);
				me.setBackgroundResource(R.drawable.red_gradient);
				betragText = getString(R.string.status_nachzahlung_start);
				meldungText = getString(R.string.status_nachzahlung_meldung);
			} else {
				View me = findViewById(android.R.id.content);
				me.setBackgroundResource(R.drawable.green_gradient);
				betragText = getString(R.string.status_rueckzahlung_start);
				meldungText = getString(R.string.status_rueckzahlung_meldung);
			}
			TextView statusAnfang = (TextView) findViewById(R.id.textStatusZahlungAnfang);
			betragText = betragText.replace("$betrag", betrag.toString());
			statusAnfang.setText(betragText);
			((TextView) findViewById(R.id.textStatusZahlungAnfang)).setText(meldungText);
			
			initViews();
		} else {
			// Kein Ergebnis von Berechnung (null)
			YesNoMessageDialog dialog = new YesNoMessageDialog(getString(R.string.status_dialog)) {

				@Override
				protected void onYesAction() {
					startActivity(new Intent(getActivity(), ZaehlerstandChooseMethodDialog.class));
					this.dismiss();
				}

				@Override
				protected void onNoAction() {
					NavUtils.navigateUpFromSameTask(getActivity());
				}

				@Override
				public void onCancel(DialogInterface dialog) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
			};
			dialog.show(getFragmentManager(), "status_dialog");
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
