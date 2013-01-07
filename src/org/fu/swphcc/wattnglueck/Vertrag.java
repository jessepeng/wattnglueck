package org.fu.swphcc.wattnglueck;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Preferences;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Vertrag extends WattnActivity {
	
	ViewFlipper flipVertrag;
	WattnFragment currentFragment;
	String superDate;
	float superGrundgebuehr = 0.00f;
	float superKilowattstunde = 0.00f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertrag);

		Preferences pref = new Preferences(this);
		superDate = pref.getBeginn();
		superGrundgebuehr = pref.getGrundpreis();
		superKilowattstunde = pref.getPreis();
		
		WattnFragment vertragBeginn = VertragBeginn.newInstance(R.layout.view_vertrag_beginn, superDate, this);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.vertrag_fragment, vertragBeginn).commit();
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
		if (currentFragment != null) return Arrays.asList(currentFragment.getWeiterButton());
		return null;
		}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		if (currentFragment != null) return currentFragment.onClick(arg0, arg1);
		return false;
	}
	
	public static class VertragBeginn extends WattnFragment {
		
		int day;
		int month;
		int year;
		int layoutID;
		Vertrag parentActivity;

		public static VertragBeginn newInstance(int layoutID, String date, Vertrag parent) {
			VertragBeginn f = new VertragBeginn();

			Date setDate = null;
			
			try {
				setDate = Constants.DBDateFormat.parse(date);
			} catch (ParseException e) {
				setDate = new Date();
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(setDate);
			f.day = cal.get(Calendar.DAY_OF_MONTH);
			f.month = cal.get(Calendar.MONTH);
			f.year = cal.get(Calendar.YEAR);
			f.layoutID = layoutID;
			f.parentActivity = parent;
			return f;
		}

		@Override
		public TextView getWeiterButton() {
			return (TextView) getActivity().findViewById(R.id.textVertragBeginnWeiter);
		}

		@Override
		public boolean onClick(View arg0, MotionEvent arg1) {
			DatePicker date = (DatePicker) getActivity().findViewById(R.id.dateVertragBeginn);
			String dateString = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth();
			try {
				parentActivity.superDate = Constants.DBDateFormat.format(Constants.DBDateFormat.parse(dateString));
			} catch (ParseException e) {
				parentActivity.superDate = dateString;
			}
			WattnFragment gebuehrenFragment = Gebuehren.newInstance(R.layout.view_gebuehren, parentActivity.superGrundgebuehr, parentActivity.superKilowattstunde, parentActivity);

	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.vertrag_fragment, gebuehrenFragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        ft.addToBackStack(null);
	        ft.commit();
			
			return true;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			DatePicker date = (DatePicker) v.findViewById(R.id.dateVertragBeginn);
			date.updateDate(year, month, day);
			return v;
		}
		
		
		@Override
		public void onActivityCreated (Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}
		
	}
	
	public static class Gebuehren extends WattnFragment  {
		
		Float grundgebuehr;
		Float kilowattstunde;
		int layoutID;
		Vertrag parentActivity;

		public static Gebuehren newInstance(int layoutID, float grundgebuehr, float kilowattstunde, Vertrag parent) {
			Gebuehren f = new Gebuehren();
			
			f.grundgebuehr = grundgebuehr;
			f.kilowattstunde = kilowattstunde;
			f.layoutID = layoutID;
			f.parentActivity = parent;
			return f;
		}

		@Override
		public TextView getWeiterButton() {
			View v = getView();
			return (v != null ? (TextView) v.findViewById(R.id.textGebuehrenWeiter) : null);
		}

		@Override
		public boolean onClick(View arg0, MotionEvent arg1) {
			return true;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			NumberPicker grundgebuehr1 = (NumberPicker) v.findViewById(R.id.gebuehrenGrund1);
			NumberPicker grundgebuehr2 = (NumberPicker) v.findViewById(R.id.gebuehrenGrund2);
			NumberPicker grundgebuehr3 = (NumberPicker) v.findViewById(R.id.gebuehrenGrund3);
			String grundgebuehrString = grundgebuehr.toString();
			grundgebuehr1.setMinValue(0);
			grundgebuehr1.setMaxValue(9);
			grundgebuehr1.setValue(Integer.parseInt(grundgebuehrString.substring(0,1)));
			grundgebuehr2.setMinValue(0);
			grundgebuehr2.setMaxValue(9);
			grundgebuehr2.setValue(Integer.parseInt(grundgebuehrString.substring(2,3)));
			grundgebuehr3.setMinValue(0);
			grundgebuehr3.setMaxValue(9);
			if (grundgebuehrString.length() >= 4) {
				grundgebuehr3.setValue(Integer.parseInt(grundgebuehrString.substring(3,4)));
			} else {
				grundgebuehr3.setValue(0);
			}
			NumberPicker kilowatt1 = (NumberPicker) v.findViewById(R.id.gebuehrenKWH1);
			NumberPicker kilowatt2 = (NumberPicker) v.findViewById(R.id.gebuehrenKWH2);
			NumberPicker kilowatt3 = (NumberPicker) v.findViewById(R.id.gebuehrenKWH3);
			NumberPicker kilowatt4 = (NumberPicker) v.findViewById(R.id.gebuehrenKWH4);
			String kilowattStundeVorKomma = kilowattstunde.toString().substring(0, kilowattstunde.toString().indexOf("."));
			String kilowattStundeNachKomma = kilowattstunde.toString().substring(kilowattstunde.toString().indexOf(".") + 1, kilowattstunde.toString().length());
			kilowatt1.setMinValue(0);
			kilowatt1.setMaxValue(9);
			if (kilowattStundeVorKomma.length() >= 2) {
				kilowatt1.setValue(Integer.parseInt(kilowattStundeVorKomma.substring(0,1)));
			} else {
				kilowatt1.setValue(0);
			}
			kilowatt2.setMinValue(0);
			kilowatt2.setMaxValue(9);
			kilowatt2.setValue(Integer.parseInt(kilowattStundeVorKomma.substring(1,2)));
			kilowatt3.setMinValue(0);
			kilowatt3.setMaxValue(9);
			kilowatt3.setValue(Integer.parseInt(kilowattStundeNachKomma.substring(0,1)));
			kilowatt4.setMinValue(0);
			kilowatt4.setMaxValue(9);
			if (kilowattStundeNachKomma.length() >= 2) {
				kilowatt4.setValue(Integer.parseInt(kilowattStundeNachKomma.substring(1,2)));
			} else {
				kilowatt4.setValue(0);
			}
			return v;
		}
		
		@Override
		public void onActivityCreated (Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}
	}

}
