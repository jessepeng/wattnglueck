package org.fu.swphcc.wattnglueck;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class Ueber extends WattnActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ueber);
		
		initViews();
		
		((TextView) findViewById(R.id.textUeberUns)).setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return Arrays.asList(
				(TextView) findViewById(R.id.textUeberWattnGlueck),
				(TextView) findViewById(R.id.textUeberUns)
				);
	}

	@Override
	protected boolean showOptionsMenu() {
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return null;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		return false;
	}



}
