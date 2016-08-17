package com.special.ResideMenuDemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class AlertDialogFragment extends DialogFragment{
	@SuppressLint("NewApi")
	public Dialog onCreatDialog(Bundle savedInstanceState){
		return new AlertDialog.Builder(getActivity())
		.setTitle("小伙伴们，请登录再查看课表")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d("cancel_clicked","ok");
				dismiss();
			}
		}).create();
	}
}
