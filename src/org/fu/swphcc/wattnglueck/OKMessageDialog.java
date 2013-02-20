package org.fu.swphcc.wattnglueck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public abstract class OKMessageDialog extends DialogFragment {

	private String message;

	public OKMessageDialog(String message) {
		this.setCancelable(false);
		this.message = message;
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		TextView textView = (TextView) getActivity().findViewById(android.R.id.message);

		Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.setting_fontfilename));
		textView.setTypeface(customFont);
		Button b1 = (Button) getActivity().findViewById(android.R.id.button1);
		Button b2 = (Button) getActivity().findViewById(android.R.id.button2);
		Button b3 = (Button) getActivity().findViewById(android.R.id.button3);
		if(b1!=null)
			b1.setTypeface(customFont);
		if(b2!=null)
			b2.setTypeface(customFont);
		if(b3!=null)
			b3.setTypeface(customFont);
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

		return dialog;
	}

	protected abstract void onOKAction();

}
