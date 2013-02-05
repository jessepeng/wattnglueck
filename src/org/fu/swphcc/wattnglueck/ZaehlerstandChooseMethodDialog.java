package org.fu.swphcc.wattnglueck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ZaehlerstandChooseMethodDialog extends Activity implements OnClickListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_zaehlerstand);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButtonKamera:
			Toast.makeText(getApplicationContext(), "Tippe auf den Bildschirm, um ein Foto zu machen.", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, Kamera.class));
			this.finish();
			break;
		case R.id.imageButtonText:
			startActivity(new Intent(this, ZaehlerstandManuell.class));
			this.finish();
			break;
		}
	}
	
}
