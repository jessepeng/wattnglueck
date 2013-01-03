package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Database;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ZaehlerstandManuell extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_manuell);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		NumberPicker num1 = (NumberPicker) findViewById(R.id.manuellNumberPicker1);
		NumberPicker num2 = (NumberPicker) findViewById(R.id.manuellNumberPicker2);
		NumberPicker num3 = (NumberPicker) findViewById(R.id.manuellNumberPicker3);
		NumberPicker num4 = (NumberPicker) findViewById(R.id.manuellNumberPicker4);
		NumberPicker num5 = (NumberPicker) findViewById(R.id.manuellNumberPicker5);
		for (NumberPicker num : Arrays.asList(num1, num2, num3, num4, num5)) {
			num.setMinValue(0);
			num.setMaxValue(9);
		}
		
		initViews();
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return null;
	}

	@Override
	protected boolean showOptionsMenu() {
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return Arrays.asList((TextView) findViewById(R.id.textManuellWeiter));
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		Float zaehlerstand = 0f;
		NumberPicker num1 = (NumberPicker) findViewById(R.id.manuellNumberPicker1);
		NumberPicker num2 = (NumberPicker) findViewById(R.id.manuellNumberPicker2);
		NumberPicker num3 = (NumberPicker) findViewById(R.id.manuellNumberPicker3);
		NumberPicker num4 = (NumberPicker) findViewById(R.id.manuellNumberPicker4);
		NumberPicker num5 = (NumberPicker) findViewById(R.id.manuellNumberPicker5);
		zaehlerstand += 10000f * num1.getValue();
		zaehlerstand += 1000f * num2.getValue(); 
		zaehlerstand += 100f * num3.getValue(); 
		zaehlerstand += 10f * num4.getValue(); 
		zaehlerstand += 1f * num5.getValue(); 
		Database db = new Database(this);
		db.addZaehlerstand(zaehlerstand);
		Toast.makeText(this, zaehlerstand + " kWh hinzugefuegt.", Toast.LENGTH_SHORT).show();
		NavUtils.navigateUpFromSameTask(this);
		return true;
	}

}
