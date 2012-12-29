package org.fu.swphcc.wattnglueck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public abstract class YesNoMessageDialog extends DialogFragment {
	
	private String message;

	public YesNoMessageDialog(String message) {
		this.message = message;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false);
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
		return builder.create();
	}
	
	protected abstract void onYesAction();
	protected abstract void onNoAction();

}
