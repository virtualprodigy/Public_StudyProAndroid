package com.virtualprodigy.studypro.Utils;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.virtualprodigy.studypro.R;

public class Prefs extends PreferenceActivity {
	
	//Option names and default values
	private static final String OPT_Vib="vibrate";
	private static final boolean OPT_Vib_DEF =true;
	private static final String OPT_Alarm="alarm";
	private static final boolean OPT_Alarm_DEF =true;

	private static final String OPT_BVib="breakAlertVibrate";
	private static final boolean OPT_BVib_DEF =true;
	private static final String OPT_BAlarm="breakAlertRing";
	private static final boolean OPT_BAlarm_DEF =true;
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		/*Preference pref = findPreference("alarm"); 
			pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			        @Override
			        public boolean onPreferenceChange(Preference preference,
			                Object newValue) {
			         if(Prefs.getAlarm(getApplicationContext()))
			        	 StudyTimer.toast.show();
			            return true;
			        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
			       
			}); */
	}

	/** Get the current value of the music option */
	public static boolean getVib(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_Vib, OPT_Vib_DEF);
	}
	
	/** Get the current value of the hints option */
	public static boolean getAlarm(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_Alarm,OPT_Alarm_DEF);
		
	}
	
	public static boolean getBAlarm(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_BAlarm,OPT_BAlarm_DEF);
		
	}
	
	public static boolean getBVib(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_BVib,OPT_BVib_DEF);
		
	}
	
	

}
