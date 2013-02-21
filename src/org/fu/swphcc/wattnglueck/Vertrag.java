package org.fu.swphcc.wattnglueck;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Preferences;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.app.AlertDialog.Builder;
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
	float superAbschlag = 0f;
	float superZaehlerstand = 0f;
	boolean superInit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertrag);

		Preferences pref = new Preferences(this);
		superDate = pref.getBeginn();
		superGrundgebuehr = pref.getGrundpreis();
		superKilowattstunde = pref.getPreis();
		superAbschlag = pref.getAbschlag();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    superInit = extras.getBoolean("init", false);
		}
		
		Database db = new Database(this);
		if (!superInit) {
			List<Zaehlerstand> zaehlerstaende = db.getByRange(superDate, superDate);
			if (zaehlerstaende != null) {
				superZaehlerstand = zaehlerstaende.get(0).getZaehlerstand();
			}
		}
		
		WattnFragment vertragBeginn = VertragBeginn.newInstance(R.layout.view_vertrag_beginn, superDate, this);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.vertrag_fragment, vertragBeginn).commit();
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		if (currentFragment != null) return currentFragment.getTextViewsForFont();
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
	
	public void saveAndFinish() {
		Preferences pref = new Preferences(this);
		pref.addAbschlag(superAbschlag);
		pref.addBeginn(superDate);
		pref.addGrundpreis(superGrundgebuehr);
		pref.addPreis(superKilowattstunde);
		Database db = new Database(this);
		if (db.getByRange(superDate, superDate) == null) {
			db.addZaehlerstand(superZaehlerstand, superDate);
		} else {
			db.updateZaehlerstand(superDate, superZaehlerstand);
		}
		finish();
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
				if (date != null) {
					setDate = Constants.DBDateFormat.parse(date);
				} else {
					setDate = new Date();
				}
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
			WattnFragment gebuehrenFragment = Gebuehren.newInstance(R.layout.view_gebuehren, parentActivity.superGrundgebuehr, parentActivity.superKilowattstunde, parentActivity);

	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.vertrag_fragment, gebuehrenFragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        ft.addToBackStack(null);
	        ft.commit();
			
			return true;
		}
		
		@Override
		public void onPause() {
			super.onPause();
			DatePicker date = (DatePicker) getActivity().findViewById(R.id.dateVertragBeginn);
			year = date.getYear();
			month = date.getMonth();
			day = date.getDayOfMonth();
			String dateString = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth();
			try {
				parentActivity.superDate = Constants.DBDateFormat.format(Constants.DBDateFormat.parse(dateString));
			} catch (ParseException e) {
				parentActivity.superDate = dateString;
			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			return v;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			DatePicker date = (DatePicker) getActivity().findViewById(R.id.dateVertragBeginn);
			date.updateDate(year, month, day);
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}

		@Override
		public List<TextView> getTextViewsForFont() {
			return Arrays.asList(
					(TextView) getActivity().findViewById(R.id.textVertragBeginn),
					(TextView) getActivity().findViewById(R.id.textVertragBeginnWeiter)
					);
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
			WattnFragment abschlagFragment = Abschlag.newInstance(R.layout.view_abschlag, parentActivity.superAbschlag, parentActivity);

	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.vertrag_fragment, abschlagFragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        ft.addToBackStack(null);
	        ft.commit();
			
			return true;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			NumberPicker grundgebuehr1 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund1);
			NumberPicker grundgebuehr2 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund2);
			NumberPicker grundgebuehr3 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund3);
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
			NumberPicker kilowatt1 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH1);
			NumberPicker kilowatt2 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH2);
			NumberPicker kilowatt3 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH3);
			NumberPicker kilowatt4 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH4);
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
			
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}
		
		@Override
		public void onPause() {
			super.onPause();
			NumberPicker grundgebuehr1 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund1);
			NumberPicker grundgebuehr2 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund2);
			NumberPicker grundgebuehr3 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenGrund3);
			float grundgebuehrNeu = grundgebuehr1.getValue() + (grundgebuehr2.getValue() * 0.1f) + (grundgebuehr3.getValue() * 0.01f);
			
			NumberPicker kilowatt1 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH1);
			NumberPicker kilowatt2 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH2);
			NumberPicker kilowatt3 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH3);
			NumberPicker kilowatt4 = (NumberPicker) getActivity().findViewById(R.id.gebuehrenKWH4);
			float kilowattNeu = (kilowatt1.getValue() * 10f) + kilowatt2.getValue() + (kilowatt3.getValue() * 0.1f) + (kilowatt4.getValue() * 0.01f);
			
			parentActivity.superGrundgebuehr = grundgebuehrNeu;
			grundgebuehr = grundgebuehrNeu;
			parentActivity.superKilowattstunde = kilowattNeu;
			kilowattstunde = kilowattNeu;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			return v;
		}

		@Override
		public List<TextView> getTextViewsForFont() {
			return Arrays.asList(
					(TextView) getActivity().findViewById(R.id.textGebuehrenBeginn),
					(TextView) getActivity().findViewById(R.id.textGebuehrenKomma1),
					(TextView) getActivity().findViewById(R.id.textGebuehrenKomma2),
					(TextView) getActivity().findViewById(R.id.textGebuehrenEuro1),
					(TextView) getActivity().findViewById(R.id.textGebuehrenEuro2),
					(TextView) getActivity().findViewById(R.id.textGebuehrenKilowatt),
					(TextView) getActivity().findViewById(R.id.textGebuehrenWeiter)
					);
		}
	}
	
	public static class Abschlag extends WattnFragment  {
		
		Float abschlag;
		int layoutID;
		Vertrag parentActivity;

		public static Abschlag newInstance(int layoutID, float abschlag, Vertrag parent) {
			Abschlag f = new Abschlag();
			
			f.abschlag = abschlag;
			f.layoutID = layoutID;
			f.parentActivity = parent;
			return f;
		}

		@Override
		public TextView getWeiterButton() {
			View v = getView();
			return (v != null ? (TextView) v.findViewById(R.id.textAbschlagWeiter) : null);
		}

		@Override
		public boolean onClick(View arg0, MotionEvent arg1) {
			onPause();
			if (this.abschlag == 0) {
				OKMessageDialog negativeAbschlagDialog = new OKMessageDialog("Bitte gib einen monatlichen Abschlag ein.") {
					
					@Override
					protected void onOKAction() {
						dismiss();
					}

					@Override
					protected void additionalBuilderOperations(Builder builder) {
					}
				};
				negativeAbschlagDialog.show(getFragmentManager(), "negative_abschlag");
			} else {
				WattnFragment zaehlerstandBeginnFragment = ZaehlerstandBeginn.newInstance(R.layout.view_zaehlerstand_beginn, parentActivity.superZaehlerstand, parentActivity);
	
		        FragmentTransaction ft = getFragmentManager().beginTransaction();
		        ft.replace(R.id.vertrag_fragment, zaehlerstandBeginnFragment);
		        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		        ft.addToBackStack(null);
		        ft.commit();
			}
			return true;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			return v;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			NumberPicker abschlag1 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum1);
			NumberPicker abschlag2 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum2);
			NumberPicker abschlag3 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum3);
			String abschlagString = abschlag.toString();
			abschlag1.setMinValue(0);
			abschlag1.setMaxValue(9);
			abschlag2.setMinValue(0);
			abschlag2.setMaxValue(9);
			abschlag3.setMinValue(0);
			abschlag3.setMaxValue(9);
			if (abschlagString.length() >= 5) {
				abschlag1.setValue(Integer.parseInt(abschlagString.substring(0,1)));
				abschlag2.setValue(Integer.parseInt(abschlagString.substring(1,2)));
				abschlag3.setValue(Integer.parseInt(abschlagString.substring(2,3)));
			} else if (abschlagString.length() >= 4) {
				abschlag1.setValue(0);
				abschlag2.setValue(Integer.parseInt(abschlagString.substring(0,1)));
				abschlag3.setValue(Integer.parseInt(abschlagString.substring(1,2)));
			} else if (abschlagString.length() >= 3) {
				abschlag1.setValue(0);
				abschlag2.setValue(0);
				abschlag3.setValue(Integer.parseInt(abschlagString.substring(0,1)));
			}
			
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}
		
		@Override
		public void onPause() {
			super.onPause();
			NumberPicker abschlag1 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum1);
			NumberPicker abschlag2 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum2);
			NumberPicker abschlag3 = (NumberPicker) getActivity().findViewById(R.id.abschlagNum3);
			
			float abschlag = abschlag1.getValue() * 100f + abschlag2.getValue() * 10f + abschlag3.getValue();
			
			parentActivity.superAbschlag = abschlag;
			this.abschlag = abschlag;
		}

		@Override
		public List<TextView> getTextViewsForFont() {
			return Arrays.asList(
					(TextView) getActivity().findViewById(R.id.textAbschlagBeginn),
					(TextView) getActivity().findViewById(R.id.textAbschlagEuro1),
					(TextView) getActivity().findViewById(R.id.textAbschlagWeiter)
					);
		}
	}

	public static class ZaehlerstandBeginn extends WattnFragment  {
		
		Float zaehlerstand;
		int layoutID;
		Vertrag parentActivity;

		public static ZaehlerstandBeginn newInstance(int layoutID, float zaehlerstand, Vertrag parent) {
			ZaehlerstandBeginn f = new ZaehlerstandBeginn();
			
			f.zaehlerstand = zaehlerstand;
			f.layoutID = layoutID;
			f.parentActivity = parent;
			return f;
		}

		@Override
		public TextView getWeiterButton() {
			View v = getView();
			return (v != null ? (TextView) v.findViewById(R.id.textZahlerBeginnWeiter) : null);
		}

		@Override
		public boolean onClick(View arg0, MotionEvent arg1) {
			NumberPicker zaehler1 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum1);
			NumberPicker zaehler2 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum2);
			NumberPicker zaehler3 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum3);
			NumberPicker zaehler4 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum4);
			NumberPicker zaehler5 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum5);
			
			zaehlerstand = zaehler1.getValue() * 10000f + zaehler2.getValue() * 1000f + zaehler3.getValue() * 100f + zaehler4.getValue() * 10f + zaehler5.getValue();
			parentActivity.superZaehlerstand = zaehlerstand;
			if (parentActivity.superInit) {
				OKMessageDialog stromzaehler = new OKMessageDialog("Bitte gehe nun zu deinem Stromzähler.") {
					
					@Override
					protected void onOKAction() {
						dismiss();
						parentActivity.saveAndFinish();
					}

					@Override
					protected void additionalBuilderOperations(Builder builder) {
					}
				};
				stromzaehler.show(getFragmentManager(), "zaehler");
			}
			return true;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			View v = inflater.inflate(layoutID, container, false);
			return v;
		}
		
		@Override
		public void onStart() {
			super.onStart();
			
			NumberPicker zaehler1 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum1);
			NumberPicker zaehler2 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum2);
			NumberPicker zaehler3 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum3);
			NumberPicker zaehler4 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum4);
			NumberPicker zaehler5 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum5);
			String zaehlerstandString = zaehlerstand.toString();
			int len = zaehlerstandString.length() - 2;
			zaehler1.setMinValue(0);
			zaehler1.setMaxValue(9);
			if (len >= 5) {
				zaehler1.setValue(Integer.parseInt(zaehlerstandString.substring(0, 1)));
			} else {
				zaehler1.setValue(0);
			}
			zaehler2.setMinValue(0);
			zaehler2.setMaxValue(9);
			if (len >= 4) {
				zaehler2.setValue(Integer.parseInt(zaehlerstandString.substring(len - 4, len - 3)));
			} else {
				zaehler2.setValue(0);
			}
			zaehler3.setMinValue(0);
			zaehler3.setMaxValue(9);
			if (len >= 3) {
				zaehler3.setValue(Integer.parseInt(zaehlerstandString.substring(len - 3, len - 2)));
			} else {
				zaehler3.setValue(0);
			}
			zaehler4.setMinValue(0);
			zaehler4.setMaxValue(9);
			if (len >= 2) {
				zaehler4.setValue(Integer.parseInt(zaehlerstandString.substring(len - 2, len - 1)));
			} else {
				zaehler4.setValue(0);
			}
			zaehler5.setMinValue(0);
			zaehler5.setMaxValue(9);
			zaehler5.setValue(Integer.parseInt(zaehlerstandString.substring(len - 1, len)));
			
			parentActivity.currentFragment = this;
	        parentActivity.initViews();
		}
		
		@Override
		public void onPause() {
			super.onPause();
			NumberPicker zaehler1 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum1);
			NumberPicker zaehler2 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum2);
			NumberPicker zaehler3 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum3);
			NumberPicker zaehler4 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum4);
			NumberPicker zaehler5 = (NumberPicker) getActivity().findViewById(R.id.zaehlerNum5);
			
			zaehlerstand = zaehler1.getValue() * 10000f + zaehler2.getValue() * 1000f + zaehler3.getValue() * 100f + zaehler4.getValue() * 10f + zaehler5.getValue();
			parentActivity.superZaehlerstand = zaehlerstand;
		}

		@Override
		public List<TextView> getTextViewsForFont() {
			return Arrays.asList(
					(TextView) getActivity().findViewById(R.id.textZaehlerstandBeginnAnfang),
					(TextView) getActivity().findViewById(R.id.textManuellZaehlerEnde),
					(TextView) getActivity().findViewById(R.id.textZahlerBeginnWeiter)
					);
		}
	}
	
}
