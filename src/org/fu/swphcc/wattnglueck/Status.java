package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.DialogInterface;
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
		TextView preisView = (TextView) findViewById(R.id.textStatusBetrag);
		Float exactBetrag = Zaehlerstand.getEstimatedBilling(this);
		if (exactBetrag != null) {
			Integer betrag = Math.round(exactBetrag);
			if (betrag < 0) {
				betrag = -betrag;
				View me = findViewById(android.R.id.content);
				me.setBackgroundResource(R.drawable.red_gradient);
				TextView statusAnfang = (TextView) findViewById(R.id.textStatusZahlungAnfang);
				statusAnfang.setText(getString(R.string.status_nachzahlung_start));
				TextView statusEnde = (TextView) findViewById(R.id.textStatusZahlungEnde);
				statusEnde.setText(getString(R.string.status_nachzahlung_ende));
			} else {
				View me = findViewById(android.R.id.content);
				me.setBackgroundResource(R.drawable.green_gradient);
				TextView statusAnfang = (TextView) findViewById(R.id.textStatusZahlungAnfang);
				statusAnfang.setText(getString(R.string.status_rueckzahlung_start));
				TextView statusEnde = (TextView) findViewById(R.id.textStatusZahlungEnde);
				statusEnde.setText(getString(R.string.status_rueckzahlung_ende));
			}
			preisView.setText(Integer.toString(betrag) + " €");
			initViews();
		} else {
			// Kein Ergebnis von Berechnung (null)
			YesNoMessageDialog dialog = new YesNoMessageDialog(getString(R.string.status_dialog)) {
				
				@Override
				protected void onYesAction() {
					NavUtils.navigateUpFromSameTask(this.getActivity());
				}
				
				@Override
				protected void onNoAction() {
					NavUtils.navigateUpFromSameTask(this.getActivity());
				}
				
				@Override
				public void onCancel(DialogInterface dialog) {
					NavUtils.navigateUpFromSameTask(this.getActivity());
				}
			};
			dialog.show(getFragmentManager(), "status_dialog");
		}
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return null;
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
		return true;
	}

}
