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
