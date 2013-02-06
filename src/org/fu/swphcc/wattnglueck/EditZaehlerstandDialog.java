package org.fu.swphcc.wattnglueck;

import java.util.Date;

import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class EditZaehlerstandDialog extends Activity {
	Zaehlerstand z;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit_zaehlerstand);
		Intent i = getIntent();
		z=new Zaehlerstand();
		z.setId(i.getIntExtra("id", -1));
		z.setDate((Date)i.getSerializableExtra("datum"));
		z.setZaehlerstand(i.getFloatExtra("zaehlerstand", 0));
	}

	public void onClickOK(View v) {

			EditText et = (EditText) findViewById(R.id.zaehlerstand);
			z.setZaehlerstand(Float.parseFloat(et.getText().toString()));
			Database db = new Database(this);
			db.updateZaehlerstand(z);
			Intent returnIntent = new Intent();
			returnIntent.putExtra("zaehlerstand", z.getZaehlerstand());
			returnIntent.putExtra("id", z.getId());
			returnIntent.putExtra("datum", z.getDate());
			setResult(RESULT_OK,returnIntent);     
			this.finish();
	}
	
	public void onClickCancel(View v) {
		this.finish();
	}
}
