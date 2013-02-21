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
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ZaehlerstandKamera extends WattnActivity {
	
	float zaehlerstand;
	Zaehlerstand alterZaehlerstand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_kamera);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent startIntent = getIntent();
		String z√§hlerstand = startIntent.getExtras().getString("value");
		
		((TextView)findViewById(R.id.textKameraZaehlerstand)).setText(z√§hlerstand);
		
		try {
			zaehlerstand = Float.valueOf(z√§hlerstand);
		} catch (NumberFormatException e) {
			zaehlerstand = 0;
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				Database db = new Database(getBaseContext());
				Preferences pref = new Preferences(getBaseContext());
				List<Zaehlerstand> zaehlerList = db.getByRange(pref.getBeginn(), Constants.DBDateFormat.format(new Date()));
				if (zaehlerList != null) {
					alterZaehlerstand = zaehlerList.get(zaehlerList.size() - 1);
				}
			}
			
		}).start();
		
		initViews();
		
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return Arrays.asList(
				(TextView) findViewById(R.id.textKameraZaehlerstand),
				(TextView) findViewById(R.id.textKameraEingeben),
				(TextView) findViewById(R.id.textKameraJa),
				(TextView) findViewById(R.id.textKameraNeinMache),
				(TextView) findViewById(R.id.textKameraNeinEingeben),
				(TextView) findViewById(R.id.textKameraNeuesFoto),
				(TextView) findViewById(R.id.textKameraZaehlerRichtig)
				);
	}

	@Override
	protected boolean showOptionsMenu() {
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return Arrays.asList(
				(TextView) findViewById(R.id.textKameraJa),
				(TextView) findViewById(R.id.textKameraEingeben),
				(TextView) findViewById(R.id.textKameraNeuesFoto)
				);
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case R.id.textKameraJa:
			if (alterZaehlerstand != null) {
				if (alterZaehlerstand.getZaehlerstand() > zaehlerstand) {
					OKMessageDialog zaehlerstandNiedrig = new OKMessageDialog("Es wurde ein Z‰hlerstand erkannt, der niedriger als dein letzter Z‰hlerstand ist. Bitte gib einen hˆheren Wert ein.") {

						@Override
						protected void onOKAction() {
							dismiss();
							startActivity(new Intent(getBaseContext(), ZaehlerstandManuell.class));
							finish();
						}

						@Override
						protected void additionalBuilderOperations(
								Builder builder) {
						}
					};
					zaehlerstandNiedrig.show(getFragmentManager(), "zaehlerstand_niedrig");
					return true;
				}
			}
			Database db = new Database(this);
			db.addZaehlerstand(zaehlerstand);
			Toast.makeText(this, zaehlerstand + " kWh hinzugefuegt.", Toast.LENGTH_SHORT).show();
			NavUtils.navigateUpFromSameTask(this);
			break;
		case R.id.textKameraNeuesFoto:
			startActivity(new Intent(this, Kamera.class));
			this.finish();
			break;
		case R.id.textKameraEingeben:
			startActivity(new Intent(this, ZaehlerstandManuell.class));
			this.finish();
			break;
		}
		return true;
	}
}
