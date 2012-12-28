package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
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
		
		TextView preisView = (TextView) findViewById(R.id.textStatusBetrag);
		preisView.setText(Integer.toString(Math.round(Zaehlerstand.getEstimatedBilling(this))).replace('.', ',') + " €");
		initViews();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
