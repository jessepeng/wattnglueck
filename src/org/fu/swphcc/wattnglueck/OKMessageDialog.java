package org.fu.swphcc.wattnglueck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public abstract class OKMessageDialog extends DialogFragment {

	private String message;

	public OKMessageDialog(String message) {
		this.setCancelable(false);
		this.message = message;
	}


	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok_dialog_ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				onOKAction();
				dialog.dismiss();
			}
		});

		Dialog dialog = builder.create();
		
		dialog.getLayoutInflater().setFactory(new Factory() {

			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase(
                        "TextView")) {
                    try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        ((TextView) view).setTextSize(20); 
                        
                        // set the text color
                        Typeface face = Typeface.createFromAsset(
                                getActivity().getAssets(),getString(R.string.setting_fontfilename));     
                        ((TextView) view).setTypeface(face);
                        ((TextView) view).setTextColor(Color.parseColor("#5e625b"));
                       
                        return view;
                    } catch (InflateException e) {
                        //Handle any inflation exception here
                    } catch (ClassNotFoundException e) {
                        //Handle any ClassNotFoundException here
                    }
                } else if (name.equalsIgnoreCase("Button")) {
                	try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        ((Button) view).setTextSize(20); 
                        
                        // set the text color
                        Typeface face = Typeface.createFromAsset(
                                getActivity().getAssets(),getString(R.string.setting_fontfilename));     
                        ((Button) view).setTypeface(face);
                        ((Button) view).setTextColor(Color.parseColor("#5e625b"));
                       
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

		return dialog;
	}

	protected abstract void onOKAction();

}
