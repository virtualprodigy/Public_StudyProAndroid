package timer;

import java.io.IOException;
import java.text.NumberFormat;

import com.CramSlam.VirtualProdigy.NotiBarBreakDisplay;
import com.CramSlam.VirtualProdigy.Prefs;
import com.CramSlam.VirtualProdigy.R;
import com.CramSlam.VirtualProdigy.CramSlam;
import com.CramSlam.VirtualProdigy.R.drawable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.widget.Toast;

public class TimerService extends Service {
	Intent i = new Intent();
	int time4Count;
	MyCount count;
	double tickTime;

	boolean IggyVisiable = true;
	boolean startVis;
	TimedBreaks cb = new TimedBreaks();
	private NotificationManager statusNoti;
	private NotificationManager serviceStatus;
	private NotificationManager timeRemainingNoti;
	static MediaPlayer notSound = new MediaPlayer();
	Notification timeNoti;
	NumberFormat nf;
	createWakeLock createWLock;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		StudyTimer.allowRestartBroadcastMethod(0);

		try {
			
			count.cancel();
			// i think i need an intent listener in the freaking service

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block

		}
		try {
			 
			RestartReceive RR = new RestartReceive();
			IntentFilter intentFilterRestart = new IntentFilter(
					"com.gtech.CramSlam.died");
			registerReceiver(RR, intentFilterRestart);
		} catch (Exception e) {
		}

		statusNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		serviceStatus = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		timeRemainingNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		

	}

	@Override
	public void onDestroy() {

		try {
			statusNoti.cancel(1);
			stopForeground(true);
			// "stopping timer from within the destroy method in service");
			count.cancel();
			// i think i need an intent listener in the freaking service
			createWLock.releaseLock();


		} catch (NullPointerException e) {
			// TODO Auto-generated catch block

		}

	}

	@Override
	public void onStart(Intent intent, int startid) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 
		createWLock = new createWakeLock(pm);
		
		try {


			count.cancel();
			// i think i need an intent listener in the freaking service

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block



		}



		time4Count = intent.getIntExtra("amountOfStudyTime", 0);

		cb.getTimeLimit(intent.getIntExtra("timeLimit", 50));


		// if (time4Count >= 60000) {
		if (time4Count >= 15000) {
			count = new MyCount(time4Count, 1000);

			Thread timeserviceThread = new Thread() {
				public void run() {
					count.start();
					if(!createWLock.isHeld()){
						createWLock.aquireLock();
					}
						
						
				}
			};
			timeserviceThread.start();// count.start();
		} else {
			stopSelf();
			Toast.makeText(this, "Stop Self Service", Toast.LENGTH_SHORT)
					.show();
		}

		timeNoti = new Notification(R.drawable.iggy, "Study Time Remaining ",
				System.currentTimeMillis());

		timeNoti.flags = Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_NO_CLEAR;

		Intent tickIntent = new Intent(getBaseContext(),
				com.CramSlam.VirtualProdigy.CramSlam.class);

		PendingIntent PendingTickIntent = PendingIntent.getActivity(
				getBaseContext(), 0, tickIntent, 0);
		timeNoti.setLatestEventInfo(getBaseContext(), "Study Time Remaining ",
				"", PendingTickIntent);
		try {
			startForeground(609, timeNoti);
		} catch (Exception e) {
		}

	}

	// countdowntimer is an abstract class, so extend it and fill in methods
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Intent startIntent = new Intent(getBaseContext(),
					CramSlam.class);
			// startIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//
			// this flag
			/* do I need */// startIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
							// // makes a
							// single entry
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // point for my
																	// app along
																	// with the
																	// singleTOp
																	// in
																	// the
																	// manifest
																	// so when
																	// this
																	// intent
																	// starts
																	// it pulls
																	// up
																	// the
																	// orginal
																	// cramslam
			startIntent.putExtra("allowRestartBroadcastMethod", 1);
			startActivity(startIntent);

			try {
				wait(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				timeRemainingNoti.cancel(609);

				i.setAction("com.gtech.CramSlam.finish");
				sendBroadcast(i);


				try {
					stopSelf();
				} catch (Exception e) {
				}
			}
		}

		@Override
		// this is where the display for the seconds in outputted
		public void onTick(long millisUntilFinished) {
			// View i;
			

			double time = millisUntilFinished / 1000;
			i.setAction("com.gtech.CramSlam.tick");
			i.putExtra("timeForTick", time);
			i.putExtra("startVis", false);


			iggySayVeiwable(cb.breakMessages(time));

			Intent tickIntent = new Intent(getBaseContext(),
					com.CramSlam.VirtualProdigy.CramSlam.class);

			PendingIntent PendingTickIntent = PendingIntent.getActivity(
					getBaseContext(), 0, tickIntent, 0);

			/*
			 * final Notification timeNoti = new Notification(R.drawable.iggy,
			 * "Study Time Remaining ", System.currentTimeMillis());
			 */try {
				timeNoti.flags = Notification.FLAG_ONGOING_EVENT
						| Notification.FLAG_NO_CLEAR;

				timeNoti.setLatestEventInfo(
						getBaseContext(),
						"Study Time Remaining ",
						" " + nf.format((int) (time / 3600)) + " : "
								+ nf.format((int) (time % 3600) / 60) + " : "
								+ nf.format((int) time % 60), PendingTickIntent);
				// startForeground(609,timeNoti);
				timeRemainingNoti.notify(609, timeNoti);
			} catch (Exception e) {
			} finally {

				sendBroadcast(i);
			}

		}

	}

	public void iggySayVeiwable(String iS) {


		Context cont = getApplicationContext();
		if (iS.equals("iggy")) {


		} else {

			if (IggyVisiable == false) {// set a boardcast recieve for this
				try {
					final Notification noti = new Notification(R.drawable.iggy,
							"CramSlam Break Alert", System.currentTimeMillis());
					noti.ledARGB = 0xff00ff00;
					noti.ledOnMS = 400;
					noti.ledOffMS = 1500;

					noti.flags = Notification.FLAG_SHOW_LIGHTS
							| Notification.FLAG_AUTO_CANCEL;

					String breakTitle = "";
					// String message ="display";
					Intent letKnow = new Intent(cont, NotiBarBreakDisplay.class);
					letKnow.putExtra("message", iS);
					PendingIntent intent = PendingIntent.getActivity(cont, 0,
							letKnow, 0);
					if (cb.isBreak() == 0) {
						breakTitle = "Break Time";// for the status bar
													// notifcation
					}
					if (cb.isBreak() == 1) {
						breakTitle = "Back To Studying";
					}

					noti.setLatestEventInfo(cont, breakTitle, iS, intent);
					

					statusNoti.notify(9, noti);
				} catch (Exception e) {
				}
			}

			Vibrator breakvib;
			breakvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			long[] vibpat = { 0, 200, 200, 200, 200 };
			// Vibrate for 300 milliseconds
			if (Prefs.getBVib(cont)) {
				try {
					breakvib.vibrate(vibpat, -1);
				} catch (Exception e) {
				}
			}

			try {

				// here's where I make the noficiation sound
				notSound.setDataSource(cont, RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				notSound.setAudioStreamType(AudioManager.STREAM_ALARM);
				notSound.setLooping(false);
				try {
					// j.prepare();
					notSound.prepare();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Prefs.getBAlarm(cont)) {
					try {
						notSound.start();
					} catch (Exception e) {
					}
				}

			}
		}

	}

	public class RestartReceive extends BroadcastReceiver {
		// public static final String CUSTOM_INTENT = "com.time";

		@Override
		public void onReceive(Context context, Intent intent) {

			IggyVisiable = intent.getBooleanExtra("IggyVisiable", true);

		}
	}

}
