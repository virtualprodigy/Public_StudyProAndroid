package com.CramSlam.VirtualProdigy;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Eula extends Activity {

	private boolean exit;
	private String EULA_PRE = "EULA ";
	PackageInfo packVersion = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent startApp = new Intent(this,
				com.CramSlam.VirtualProdigy.CramSlam.class);

		packVersion = getPackageVersion();
		final String eulaKey = " " + packVersion.versionCode + "."
				+ packVersion.versionName + ": " + EULA_PRE;

		final SharedPreferences saveEula = PreferenceManager
				.getDefaultSharedPreferences(this);
		Boolean EulaAccepted = saveEula.getBoolean(eulaKey, false);

		if (EulaAccepted == true) {
			startActivity(new Intent(this,
					com.CramSlam.VirtualProdigy.CramSlam.class));
		}

		if (EulaAccepted == false) {
			String appTitle = this.getString(R.string.app_name) + " Version "
					+ eulaKey;
			String eulaStatement = this.getString(R.string.eula);// getApplicationContext()
																	// has
																	// issues
																	// using
																	// this
																	// fixes
																	// window
																	// manager
																	// error and
																	// string
																	// error
			AlertDialog.Builder eulaAlert = new AlertDialog.Builder(this)
					.setTitle(appTitle)
					.setMessage(eulaStatement)
					.setPositiveButton("Accept", new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface,
								int i) {

							SharedPreferences.Editor editor = saveEula.edit();
							editor.putBoolean(eulaKey, true);
							editor.commit();
							dialogInterface.dismiss();
							startActivity(new Intent(startApp));

						}
					})
					.setNegativeButton("Disagree",
							new Dialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									finish();
								}

							}).setCancelable(false);
			eulaAlert.create().show();

		}
	}

	private PackageInfo getPackageVersion() {
		PackageInfo pack = null;
		try {
			pack = getApplicationContext().getPackageManager().getPackageInfo(
					getApplicationContext().getPackageName(),
					PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {

		}
		return pack;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

		return;
	}

}

