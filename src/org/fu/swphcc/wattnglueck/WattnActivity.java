package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * Abstrakte Oberklasse fuer alle Activitys. Diese organisiert beispielsweise
 * das Anzeigen des Optionenmenues oder das Setzen der benutzerdefinierten Font.
 * @author Jan-Christopher
 *
 */
public abstract class WattnActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Typeface customFont = Typeface.createFromAsset(getAssets(), getString(R.string.setting_fontfilename));
		List<TextView> textViews = getTextViewsForFont();
		if (textViews != null) {
			for (TextView textView : textViews) {
				if (textView != null) textView.setTypeface(customFont);
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
	
}
