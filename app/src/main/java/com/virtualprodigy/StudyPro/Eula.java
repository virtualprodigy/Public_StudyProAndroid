package com.virtualprodigy.studypro;


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
import android.webkit.WebView;

public class Eula extends Activity {

	private boolean exit;
	PackageInfo packVersion = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayEula();
	}

	private void displayEula() {
		packVersion = getPackageVersion();
		final String eulaKey_Title =getString(R.string.eula) + getString(R.string.app_name) + getString(R.string.version) + packVersion.versionName;

		final SharedPreferences saveEula = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean isEulaAccepted = saveEula.getBoolean(eulaKey_Title, false);

		final Intent startApp = new Intent(this, StudyProActivity.class);
		if (isEulaAccepted == true) {
			startActivity(startApp);
		}else {
			
			WebView webView = new WebView(this);
			webView.loadUrl("file:///android_asset/study_pro_eula.html");

			AlertDialog.Builder eulaAlert = new AlertDialog.Builder(this)
					.setTitle(eulaKey_Title)
					.setView(webView)
					.setPositiveButton(R.string.accept, new Dialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface,int i) {

							SharedPreferences.Editor editor = saveEula.edit();
							editor.putBoolean(eulaKey_Title, true);
							editor.commit();
							dialogInterface.dismiss();
							startActivity(new Intent(startApp));

						}
					})
					.setNegativeButton(R.string.disagree,
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
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

		return;
	}

}

