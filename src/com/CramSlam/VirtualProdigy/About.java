package com.CramSlam.VirtualProdigy;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.content.pm.PackageManager;
public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		PackageInfo packVersion = null;
		packVersion = getPackageVersion();
		final String versionKey = " " + packVersion.versionCode + "."
				+ packVersion.versionName;
		TextView about = (TextView)findViewById(R.id.about_content);
		about.setText("CramSlam is an application developed for students of all ages. The goal of this application is to efficiently and effectively help students manage their time studying without surpassing the point of information retention. In other words, putting a stop to \"Burning Out\". This is accomplished through the unique \"Study Timer\" . Once an amount of study time is selected, this unique timer will provide the user with prompts suggesting ideal times to take breaks. It also suggest to switch between subjects to help avoid lose of focus and boredom while studying. This app also includes other features frequently used and preferred by students. The \"Grade Calculator\" in CramSlam can calculate both weighted and un-weighted scores.And it features a practical notepad to store any note a user may have.  With the proper use of this application, students should notice boosted confidence in their course material and an easier study load. That's the perfect formula for a better grade." );
		TextView versionNumber = (TextView)findViewById(R.id.versionNum);
		versionNumber.setText(versionKey);
		
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
}
