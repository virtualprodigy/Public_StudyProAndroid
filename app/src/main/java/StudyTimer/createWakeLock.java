package studytimer;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;


public class createWakeLock  /*extends Activity*/  {
	PowerManager pm;
	WakeLock pmWL;
	Context context;
	/*  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 pmWL = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CRAMSLAM_WAKELOCK");
		 aquireLock();
		 Log.d("Important","is held check 1"+pmWL.isHeld());
	}*/
  protected  createWakeLock(PowerManager p){
	  pm = p;
		 pmWL = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CRAMSLAM_WAKELOCK");
		 aquireLock();
    }
	
	
	
	protected boolean isHeld() {
		// TODO Auto-generated method stub
		 //Log.d("Important","is held check 1"+pmWL.isHeld());
		if (pmWL.isHeld())
			return true;
		else if(!pmWL.isHeld())
			return false;
		
		return false;
	
	}
	protected void releaseLock() {
		// TODO Auto-generated method stub

		try {
			pmWL.release();
		}catch(Exception e){
			
		} 
	}
	protected void aquireLock() {
		// TODO Auto-generated method stub
		
		try {
			pmWL.acquire();
		}catch(Exception e){
			
		} 
		if(!isHeld()){
		while(!isHeld()){

			try {
				pmWL.acquire();
			}catch(Exception e){
				
			} 
		}
		}
	
		
	}
	
}
/*
boolean isScreenOn = pm2.isScreenOn();
if (!isScreenOn) {
	WakeLock pmWL = pm2.newWakeLock(
			PowerManager.SCREEN_DIM_WAKE_LOCK, "tag");// maybe
														// try
														// using
														// the
														// aquire
														// wakes
														// up
														// and
														// remove
														// the
														// parital
														// wake

*/