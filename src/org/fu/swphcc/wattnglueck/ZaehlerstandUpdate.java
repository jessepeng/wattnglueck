package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Preferences;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ZaehlerstandUpdate extends WattnActivity {

	private Zaehlerstand zaehlerstand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_update);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		NumberPicker num1 = (NumberPicker) findViewById(R.id.manuellNumberPicker1);
		NumberPicker num2 = (NumberPicker) findViewById(R.id.manuellNumberPicker2);
		NumberPicker num3 = (NumberPicker) findViewById(R.id.manuellNumberPicker3);
		NumberPicker num4 = (NumberPicker) findViewById(R.id.manuellNumberPicker4);
		NumberPicker num5 = (NumberPicker) findViewById(R.id.manuellNumberPicker5);
		num1.setMinValue(0);
		num1.setMaxValue(9);
		num2.setMinValue(0);
		num2.setMaxValue(9);
		num3.setMinValue(0);
		num3.setMaxValue(9);
		num4.setMinValue(0);
		num4.setMaxValue(9);
		num5.setMinValue(0);
		num5.setMaxValue(9);
		Database db = new Database(this);
		Preferences pref = new Preferences(this);

		Intent i = getIntent();
		zaehlerstand = new Zaehlerstand();
		zaehlerstand.setId(i.getIntExtra("id", -1));
		zaehlerstand.setDate((Date)i.getSerializableExtra("datum"));
		zaehlerstand.setZaehlerstand(i.getFloatExtra("zaehlerstand", 0));
		if (zaehlerstand != null) {
			String zaehlerstandString = zaehlerstand.getZaehlerstand().toString();
			int len = zaehlerstandString.length() - 2;
			if (len >= 5) {
				num1.setValue(Integer.parseInt(zaehlerstandString.substring(0, 1)));
			} else {
				num1.setValue(0);
			}
			num2.setMinValue(0);
			num2.setMaxValue(9);
			if (len >= 4) {
				num2.setValue(Integer.parseInt(zaehlerstandString.substring(len - 4, len - 3)));
			} else {
				num2.setValue(0);
			}
			num3.setMinValue(0);
			num3.setMaxValue(9);
			if (len >= 3) {
				num3.setValue(Integer.parseInt(zaehlerstandString.substring(len - 3, len - 2)));
			} else {
				num3.setValue(0);
			}
			num4.setMinValue(0);
			num4.setMaxValue(9);
			if (len >= 2) {
				num4.setValue(Integer.parseInt(zaehlerstandString.substring(len - 2, len - 1)));
			} else {
				num4.setValue(0);
			}
			num5.setMinValue(0);
			num5.setMaxValue(9);
			num5.setValue(Integer.parseInt(zaehlerstandString.substring(len - 1, len)));

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
		if (this.zaehlerstand != null) {
			if (this.zaehlerstand.getZaehlerstand() > zaehlerstand) {
				OKMessageDialog zaehlerstandNiedrig = new OKMessageDialog("Du hast einen Zählerstand eingegeben, der niedriger als dein letzter Zählerstand ist. Bitte gebe einen höheren Wert ein.") {

					@Override
					protected void onOKAction() {
						dismiss();
					}
				};
				zaehlerstandNiedrig.show(getFragmentManager(), "zaehlerstand_niedrig");
				return true;
			}
		}
		Database db = new Database(this);
		db.updateZaehlerstand(this.zaehlerstand);
		Intent returnIntent = new Intent();
		returnIntent.putExtra("zaehlerstand", this.zaehlerstand.getZaehlerstand());
		returnIntent.putExtra("id", this.zaehlerstand.getId());
		returnIntent.putExtra("datum", this.zaehlerstand.getDate());
		setResult(RESULT_OK,returnIntent); 
		Toast.makeText(this, zaehlerstand + " kWh hinzugefuegt.", Toast.LENGTH_SHORT).show();
		NavUtils.navigateUpFromSameTask(this);
		return true;
	}

}
