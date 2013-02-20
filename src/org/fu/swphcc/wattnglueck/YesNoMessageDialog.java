package org.fu.swphcc.wattnglueck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public abstract class YesNoMessageDialog extends DialogFragment {
	
	private String message;

	public YesNoMessageDialog(String message) {
		this.message = message;
		this.setCancelable(false);
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage(message);
		
		builder.setNegativeButton(R.string.yes_no_dialog_no, new DialogInterface.OnClickListener() {
			 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onNoAction();
			}
		});
		builder.setPositiveButton(R.string.yes_no_dialog_yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onYesAction();
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		TextView textView = (TextView) dialog.findViewById(android.R.id.message);
		
		Typeface customFont = Typeface.createFromAsset(builder.getContext().getAssets(), getString(R.string.setting_fontfilename));
		textView.setTypeface(customFont);
		Button b1 = (Button) dialog.findViewById(android.R.id.button1);
		Button b2 = (Button) dialog.findViewById(android.R.id.button2);
		Button b3 = (Button) dialog.findViewById(android.R.id.button3);
		b1.setTypeface(customFont);
		b2.setTypeface(customFont);
		b3.setTypeface(customFont);
		
		return dialog;
	}
	
	protected abstract void onYesAction();
	protected abstract void onNoAction();

}
