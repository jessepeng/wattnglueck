package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Database;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Abstrakte Oberklasse fuer alle Activitys. Diese organisiert beispielsweise
 * das Anzeigen des Optionenmenues oder das Setzen der benutzerdefinierten Font.
 * @author Jan-Christopher
 *
 */
public abstract class WattnActivity extends Activity implements OnTouchListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Methode, die aufgerufen werden soll, um die TextViews und andere Views
	 * vorzubereiten (zum Beispiel Touch-Events oder Custom-Font hinzufuegen).
	 * Diese Methode sollte von jeder onCreate()-Methode als letzte
	 * Anweisung ausgefuert werden.
	 */
	protected void initViews() {
		Typeface customFont = Typeface.createFromAsset(getAssets(), getString(R.string.setting_fontfilename));
		List<TextView> textViews = getTextViewsForFont();
		if (textViews != null) {
			for (TextView textView : textViews) {
				if (textView != null) textView.setTypeface(customFont);
			}
		}
		
		List<TextView> buttonViews = getButtonTextViews();
		if (buttonViews != null) {
			for (TextView textView : buttonViews) {
				if (textView != null) textView.setOnTouchListener(this);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (showOptionsMenu()) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.options_menu, menu);
		}
		return true;
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
		case R.id.itemDevDummyValues:
			Database db = new Database(this);
			db.setDummyValues();
			Toast.makeText(this, "Dummy-Values eingefügt.", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.itemDevDeleteDatabase:
			db = new Database(this);
			db.clearDatabase();
			Toast.makeText(this, "Datenbank geleert.", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Wird aufgerufen, um die TextViews zu erhalten, die die benutzerdefinierte Font erhalten sollen
	 * @return
	 */
	protected abstract List<TextView> getTextViewsForFont();
	
	/**
	 * Legt fest, ob das Optionsmenue angezeigt werden soll.
	 * @return
	 */
	protected abstract boolean showOptionsMenu();
	
	/**
	 * Wird aufgerufen, um die TextViews zu erhalten, die als Buttons fungieren sollen.
	 * @return
	 */
	protected abstract List<TextView> getButtonTextViews();
	

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg1.getActionMasked() == MotionEvent.ACTION_DOWN) {
			Animation anim = new AlphaAnimation(0f, 1f);
			Animation out = new AlphaAnimation(1f, 0f);
			out.setDuration(0);
			anim.setDuration(150);
			out.setFillAfter(true);
			anim.setFillAfter(true);
			arg0.startAnimation(out);
			arg0.setBackgroundResource(R.drawable.button_background_pressed);
			arg0.startAnimation(anim);
			return true;
		} else if (arg1.getActionMasked() == MotionEvent.ACTION_UP) {
			arg0.setBackgroundResource(R.drawable.button_background);
			return onClick(arg0, arg1);
		}
		return true;
		
	}
	
	public abstract boolean onClick(View arg0, MotionEvent arg1);
	
}
