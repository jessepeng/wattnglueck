package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Vertrag extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertrag);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean showOptionsMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
