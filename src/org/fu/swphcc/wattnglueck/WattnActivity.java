package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
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
	 * Setzt die Schriftart richtig im Titel.
	 */
	protected void onResume() {
		super.onResume();

		try {
			final int titleId =  Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView title = (TextView) getWindow().findViewById(titleId);
			title.setTextSize(24);
			title.setTextColor(Color.parseColor("#5e625b"));
			Typeface customFont = Typeface.createFromAsset(getAssets(), getString(R.string.setting_fontfilename));
			title.setTypeface(customFont);
		} catch(Exception e) {}
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
			
			getLayoutInflater().setFactory(new Factory() {
		            public View onCreateView(String name, Context context,
		                    AttributeSet attrs) {
		            	System.out.println(name);
		                if (name.equalsIgnoreCase(
		                        "TextView")) {
		                    try {
		                        LayoutInflater li = LayoutInflater.from(context);
		                        final View view = li.createView(name, null, attrs);
		                        ((TextView) view).setTextSize(20); 
                                
                                // set the text color
                                Typeface face = Typeface.createFromAsset(
                                        getAssets(),getString(R.string.setting_fontfilename));     
                                ((TextView) view).setTypeface(face);
                                ((TextView) view).setTextColor(Color.parseColor("#5e625b"));
		                       
		                        return view;
		                    } catch (InflateException e) {
		                        //Handle any inflation exception here
		                    } catch (ClassNotFoundException e) {
		                        //Handle any ClassNotFoundException here
		                    }
		                }
		                return null;
		            }
		        });
			
			getMenuInflater().inflate(R.menu.options_menu, menu);
		}
		return super.onCreateOptionsMenu(menu);
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
		case R.id.itemVertrag:
			startActivity(new Intent(this, Vertrag.class));
			return true;
//		case R.id.itemDevDummyValues:
//			Database db = new Database(this);
//			Preferences pref = new Preferences(this);
//			pref.setDummyValues();
//			db.setDummyValues();
//			Toast.makeText(this, "Dummy-Values eingefügt.", Toast.LENGTH_SHORT).show();
//			return true;
		case R.id.itemDevDeleteDatabase:
			Database db = new Database(this);
			Preferences pref = new Preferences(this);
			db = new Database(this);
			pref = new Preferences(this);
			db.clearDatabase();
			pref.clearPreferences();
			OKMessageDialog prefsDeleted = new OKMessageDialog("Datenbank und Einstellungen wurden gelöscht.") {
				
				@Override
				protected void onOKAction() {
					dismiss();
					finish();
				}
			};
			prefsDeleted.show(getFragmentManager(), "prefs_deleted");
			return true;
		case R.id.itemZaehlerstaende:
			startActivity(new Intent(this,ZaehlerstandActivity.class));
			return true;
			
		case R.id.itemAbout:
			startActivity(new Intent(this,Ueber.class));
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
