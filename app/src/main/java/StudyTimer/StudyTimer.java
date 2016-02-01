package studytimer;

import java.io.IOException;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualprodigy.studypro.Prefs;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.how;
import com.virtualprodigy.studypro.settingmenubuttons;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class StudyTimer extends Activity {
	protected static final String TAG = null;

	Vibrator vib;

	static MediaPlayer alarm = null;
	static AlertDialog alert;
	AlertDialog.Builder builder;
	int SHOW_ALERT = 1;
	int DONT_SHOW_ALERT = 0;
	static int ALERT_READY_STATE;
	static int RESTART_VAR;
	int RESTART_NO = 0;
	int RESTART_YES = 1;
	int time4Count;
	static int timeLimit, allowRestartBroadcast = 0;
	private boolean IggyVisiable;
	private SharedPreferences tutorialPrefs;

	TextView popText;
	ScrollView sV;
	boolean startVis;
	static int tCAble;

	private NotificationManager statusNoti;

	int ptotal;
	TimedBreaks cb = new TimedBreaks();// c allb reak
	static MediaPlayer notSound = new MediaPlayer();

	double tickTime;
	TickReceive tickR = new TickReceive();
	FinishReceive finR = new FinishReceive();
	// Kill kill = new Kill();
	int TaskId;
	static int displayHow;
	Button start, stop, alarmButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d(TAG, "starting study timer on create");

		statusNoti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		tutorialPrefs = getApplicationContext().getSharedPreferences("showHow",
				MODE_PRIVATE);
		displayHow = tutorialPrefs.getInt("showHow", 0);
		
		
		final Intent startTimer = new Intent(this, TimerService.class);

		View timer;
		setContentView(R.layout.time);
		timer = new TextView(this);
		popText = (TextView) findViewById(R.id.iggypoptalk);
		sV = (ScrollView) findViewById(R.id.scrollTalk);
		/*
		 * // Log.d(TAG, "register the broadcast recievers"); IntentFilter
		 * intentFilterTick = new IntentFilter( "com.gtech.CramSlam.tick");
		 * registerReceiver(tickR, intentFilterTick);
		 * 
		 * IntentFilter intentFilterkill = new IntentFilter(
		 * "com.gtech.CramSlam.kill"); // registerReceiver(kill,
		 * intentFilterkill);
		 * 
		 * IntentFilter intentFilterFin = new IntentFilter(
		 * "com.gtech.CramSlam.finish"); registerReceiver(finR,
		 * intentFilterFin);
		 */
		Button increase, decrease, info;

		start = (Button) findViewById(R.id.StartButton);
		stop = (Button) findViewById(R.id.StopButton);
		alarmButton = (Button) findViewById(R.id.alarmstop);
		stop.setVisibility(View.INVISIBLE);
		alarmButton.setVisibility(View.INVISIBLE);
		increase = (Button) findViewById(R.id.IncreaseButton);
		decrease = (Button) findViewById(R.id.DecreaseButton);
		info = (Button) findViewById(R.id.infobutton);

		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					// count.start();
					// Log.d(TAG,
					// "starting service from StudyTImer along with putting in the value of the timer");

					startTimer.putExtra("amountOfStudyTime", time4Count);
					startTimer.putExtra("timeLimit", timeLimit);
					Thread timeserviceThread = new Thread() {
						public void run() {

							startService(startTimer);
						}
					};
					timeserviceThread.start();

				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					((TextView) findViewById(R.id.totaltime)).setTextSize(25);
					((TextView) findViewById(R.id.totaltime))
							.setText("Please Select Your Amount Of Study Time");

				}

			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// count.cancel();
				// Log.d(TAG, "stopping timer from StudyTimer");
				try {
					stopService(startTimer);
					start.setVisibility(View.VISIBLE);
					stop.setVisibility(View.INVISIBLE);
					tCAble = 0;
				} catch (Exception e) {
				}
			}
		});

		alarmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// count.cancel();
				// Log.d(TAG, "stopping timer from StudyTimer");
				try {
					StudyTimer.alarm.stop();
					vib.cancel();
					start.setVisibility(View.VISIBLE);
					alarmButton.setVisibility(View.INVISIBLE);
					tCAble = 0;
				} catch (Exception e) {
				}
			}
		});

		increase.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeTimeLimit(1);
			}
		});
		decrease.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeTimeLimit(-1);
			}
		});

		info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						settingmenubuttons.class));

			}
		});

		AdView ad = (AdView) findViewById(R.id.timescreenads);
		ad.loadAd(new AdRequest());

		final Thread tutorialThread = new Thread() {
			@Override
			public void run() {

				Intent Guide = new Intent(getApplicationContext(), how.class);
				// Guide.putExtra("checkVaule", displayHow);
				startActivity(Guide);

			}
		};
		if (displayHow == 0) {
			tutorialThread.start();
		}
		
		ImageView showingIggyReflected = (ImageView)findViewById(R.id.showiggy);
		showingIggyReflected.setImageBitmap(reflectiveIggy(BitmapFactory.decodeResource(getResources(), R.drawable.iggy)) );
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(getApplicationContext(),
					settingmenubuttons.class));

			return true;

		}

		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		IggyVisiable = false;
		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
		send.setAction("com.gtech.CramSlam.died");
		// send.putExtra("timeLimit", timeLimit);

		sendBroadcast(send);

		SharedPreferences.Editor edit = tutorialPrefs.edit();
		edit.putInt("showHow", displayHow);
		edit.commit();
		// finish();
	}

	@Override
	protected void onResume() {

		super.onResume();
		IntentFilter intentFilterFin = new IntentFilter(
				"com.gtech.CramSlam.finish");
		registerReceiver(finR, intentFilterFin);
		IggyVisiable = true;

		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
		// send.putExtra("timeLimit", timeLimit);

		allowRestartBroadcast = getIntent().getIntExtra(
				"allowRestartBroadcastMethod", 0);

		if (allowRestartBroadcast == 0) {
			send.setAction("com.gtech.CramSlam.died");
		}
		sendBroadcast(send);
		IntentFilter intentFilterTick = new IntentFilter(
				"com.gtech.CramSlam.tick");
		registerReceiver(tickR, intentFilterTick);

		tutorialPrefs = getApplicationContext().getSharedPreferences("showHow",
				MODE_PRIVATE);
		displayHow = tutorialPrefs.getInt("showHow", 0);

	}

	@Override
	protected void onDestroy() {

		IggyVisiable = false;
		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
		send.setAction("com.gtech.CramSlam.died");
		// send.putExtra("timeLimit", timeLimit);

		sendBroadcast(send);

		super.onDestroy();
	}

	public class TickReceive extends BroadcastReceiver {
		// public static final String CUSTOM_INTENT = "com.time";
		private final String TAG = null;

		@Override
		public void onReceive(Context context, Intent intent) {

			tickTime = intent.getDoubleExtra("timeForTick", 1.1);
			// Log.d(TAG, "recieving the on tick");
			startVis = intent.getBooleanExtra("startVis", true);
			if (startVis == false) {
				try {
					start.setVisibility(View.INVISIBLE);
					stop.setVisibility(View.VISIBLE);
					tCAble = 1;
				} catch (Exception e) {
				}
			}
			NumberFormat nf;
			nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(2);

			// seconds

			iggySayVeiwable(cb.breakMessages(tickTime));
			// Log.d(TAG," Checking for the timed breaks and then displaying the time");
			((TextView) findViewById(R.id.totaltime)).setTextSize(37);
			if (tickTime % 2 == 0) {
				((TextView) findViewById(R.id.totaltime)).setText(nf
						.format((int) (tickTime / 3600))
						+ "   "
						+ nf.format((int) (tickTime % 3600) / 60)
						+ "   "
						+ nf.format((int) tickTime % 60));

			} else {
				((TextView) findViewById(R.id.totaltime)).setText(nf
						.format((int) (tickTime / 3600))
						+ " :  "
						+ nf.format((int) (tickTime % 3600) / 60)
						+ " : "
						+ nf.format((int) tickTime % 60));
			}

		}
	}

	public class FinishReceive extends BroadcastReceiver {
		// public static final String CUSTOM_INTENT = "com.time";
		private final String TAG = null;

		Toast toast;

		@Override
		public void onReceive(Context context, Intent intent) {


			// start.setVisibility(View.VISIBLE);
			try {
				alarmButton.setVisibility(View.VISIBLE);
				stop.setVisibility(View.INVISIBLE);
			} catch (Exception e) {

			} finally {
				tCAble = 0;
			}

			Context c = getApplicationContext();
			((TextView) findViewById(R.id.totaltime))
					.setText(" Time's up! Please stop the timer. ");

			vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			long[] vibpat = { 0, 200, 200, 200, 200, 200, 500, 500, 200, 200,
					200, 200, 200, 500 };
			// Vibrate for 300 milliseconds

			iggySayVeiwable("Yahoo, we finished studying!!");
			try {
				alarm = new MediaPlayer();
				alarm.setDataSource(c, RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_ALARM));
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
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				alarm.setAudioStreamType(AudioManager.STREAM_ALARM);
				alarm.setLooping(true);
				try {

					alarm.prepare();

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Prefs.getAlarm(c)) {
					alarm.start();
				}

				if (Prefs.getVib(c)) {
					vib.vibrate(vibpat, 1);

				}
			}

			// have it wait until the view is done recreating

			// fix cuz it they set the volume for alarm to off then
			// then the stop button will never display
			// have red led flash in sos in morse code

		}
	}

	public void changeTimeLimit(int tC) {
		// Log.d(TAG, "timelimt changing");
		timeLimit = timeLimit + tC;
		cb.getTimeLimit(timeLimit);
		if (tCAble == 1) {
			return;
		}
		if (timeLimit == -1) {
			timeLimit = 0;
		}
		((TextView) findViewById(R.id.totaltime)).setTextSize(30);
		switch (timeLimit) {
		case 0:
			timeLimit = 8;

			((TextView) findViewById(R.id.totaltime)).setText(" 4 hours ");// pretends
																			// to
																			// be
																			// case
																			// 8
			time4Count = 14400000;

			break;
		case 1:
			time4Count = 1800000;
			// time4Count = 60000;//15000;
			// 60000
			((TextView) findViewById(R.id.totaltime)).setText("30 minutes");

			break;
		case 2:
			((TextView) findViewById(R.id.totaltime)).setText("1 hour ");
			time4Count = 3600000;

			break;
		case 3:
			((TextView) findViewById(R.id.totaltime)).setText("1 hr 30 mins");
			time4Count = 5400000;

			break;
		case 4:
			((TextView) findViewById(R.id.totaltime)).setText(" 2 hours");
			time4Count = 7200000;

			break;
		case 5:
			((TextView) findViewById(R.id.totaltime)).setText("2 hr 30 mins");
			time4Count = 9000000;

			break;
		case 6:
			((TextView) findViewById(R.id.totaltime)).setText("3 hours");
			time4Count = 10800000;

			break;
		case 7:
			((TextView) findViewById(R.id.totaltime)).setText("3 hrs 30 mins");
			time4Count = 12600000;

			break;
		case 8:
			((TextView) findViewById(R.id.totaltime)).setText("4 hrs ");
			time4Count = 14400000;

			break;
		case 9:
			timeLimit = 1;
			((TextView) findViewById(R.id.totaltime)).setText("30 mins");// pretends
																			// to
																			// be
																			// case
																			// 1
			time4Count = 1800000;

			break;
		default:
			/*
			 * if(timeLimit<0){ timeLimit = 8; ((TextView)
			 * findViewById(R.id.totaltime)) .setText("loop to back"+timeLimit);
			 * break; }if(timeLimit>8){ timeLimit=1; ((TextView)
			 * findViewById(R.id.totaltime))
			 * .setText("loop to front"+timeLimit); break; }
			 */
			break;

		}

	}

	public void iggySayVeiwable(String iS) {// this method checks if you are on
											// a break or not from the
											// timedbreak class and then the
											// method check if your in the app
											// or not, if you are the message is
											// shown by iggy if not a pop up
											// message is displayed
		if (iS.equals("iggy")) {

			// to avoid the null pointer exceptions when no data is sent lulz
			// maybe an if statememtn before the call would be better idc right
			// now tho
		} else {

			/*
			 * if (IggyVisiable == false) { final Notification noti = new
			 * Notification(R.drawable.iggy, "CramSlam Break Alert",
			 * System.currentTimeMillis()); noti.ledARGB = 0xff00ff00;
			 * noti.ledOnMS = 400; noti.ledOffMS = 1500;
			 * 
			 * noti.flags = Notification.FLAG_SHOW_LIGHTS |
			 * Notification.FLAG_AUTO_CANCEL;
			 * 
			 * Context cont = getApplicationContext(); String breakTitle = "";
			 * String message ="display"; Intent letKnow = new Intent(this,
			 * NotiBarBreakDisplay.class); letKnow.putExtra("message", iS);
			 * PendingIntent intent = PendingIntent.getActivity(this, 0,
			 * letKnow, 0); if (cb.isBreak() == 0) { breakTitle =
			 * "Break Time";// for the status bar notifcation } if (cb.isBreak()
			 * == 1) { breakTitle = "Back To Studying"; }
			 * 
			 * noti.setLatestEventInfo(cont, breakTitle, iS, intent);
			 * statusNoti.notify(1, noti); }
			 */
			/*
			 * LayoutInflater inflat = getLayoutInflater(); View layout =
			 * inflat.inflate(R.layout.iggytalk, (ViewGroup)
			 * findViewById(R.id.iggyhome)); texti = (TextView)
			 * layout.findViewById(R.id.iggytalk); toaster = new
			 * Toast(getApplicationContext()); // toast.setGravity(Gravity.LEFT,
			 * 10, 0); texti.setText(iS);
			 * toaster.setDuration(Toast.LENGTH_LONG); // 1800000, 1000
			 * 
			 * toaster.setView(layout); toaster.show();
			 * 
			 * 
			 * }
			 */
			if (IggyVisiable == true) {

				// Log.d("Visible","seting text");
				// popText.setVisibility(8);
				popText.setText(iS);
				// Log.d("Visible","POPTEXT set");
				sV.setVisibility(View.VISIBLE);
				// Log.d("Visible","set scrollview visible now timing till invisble");
				extraTime quickie;
				quickie = new extraTime(7000, 1000, 1);
				quickie.start();

				Context cc = getApplicationContext();
				Vibrator breakvib;
				breakvib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				long[] vibpat = { 0, 200, 200, 200, 200 };
				// Vibrate for 300 milliseconds
				if (Prefs.getBVib(cc))
					breakvib.vibrate(vibpat, -1);

				try {

					// here's where I make the noficiation sound
					notSound.setDataSource(cc, RingtoneManager
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
					if (Prefs.getBAlarm(cc)) {

						notSound.start();
					}

				}
			}

		}

	}

	public class extraTime extends CountDownTimer {
		int task;

		public extraTime(long millisInFuture, long countDownInterval, int tK) {
			super(millisInFuture, countDownInterval);
			task = tK;
		}

		@Override
		public void onFinish() {
			if (task == 1) {
				sV.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		// this is where the display for the seconds in outputted
		public void onTick(long millisUntilFinished) {
			// View i;

			double time = millisUntilFinished / 1000;

		}
	}

	private void buildAlert(int status) {
		ALERT_READY_STATE = status;
		//Log.d("Alert0", "" + ALERT_READY_STATE);
	}

	/*
	 * private void showAlert(){ Log.d("Alert1",""+ALERT_READY_STATE);
	 * if(ALERT_READY_STATE!=SHOW_ALERT){ return; }
	 * Log.d("Alert2",""+ALERT_READY_STATE); if(ALERT_READY_STATE==SHOW_ALERT){
	 * 
	 * 
	 * builder = new AlertDialog.Builder(this);
	 * builder.setMessage("End alarm").setCancelable(false).setPositiveButton(
	 * "Disable", new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int id) { StudyTimer.alarm.stop();
	 * vib.cancel(); alert.cancel(); } });
	 * 
	 * alert = builder.create();
	 * 
	 * 
	 * alert.show(); //ALERT_READY_STATE=DONT_SHOW_ALERT; } }
	 */

	public static void displayHowCheckBox(int value) {
		// displayHow = value;
	}

	public static void allowRestartBroadcastMethod(int v) {
	//	Log.d("studytimer_resume", "no restart" + v);
		allowRestartBroadcast = v;
	}
	private static Bitmap reflectiveIggy(Bitmap imageOrginal){
		final int reflectiveGap = 0;
		Bitmap iggyOrginal = imageOrginal;//originalImage
		
		int width = iggyOrginal.getWidth();
	       int height = iggyOrginal.getHeight();
	       Matrix matrix= new Matrix();
	       matrix.preScale(1,-1);
	       Bitmap reflectedIggyPart = Bitmap.createBitmap(iggyOrginal,0,height/2,width,height/2,matrix,false);//reflectionImage
	       
	       Bitmap bitmapRelectedEnlargement = Bitmap.createBitmap(width,(height+height/2),Config.ARGB_8888);//bitmapWithReflection
	       
	       Canvas canvas = new Canvas(bitmapRelectedEnlargement);
	       canvas.drawBitmap(iggyOrginal, 0,0,null);
            Paint gapPaint = new Paint();//deafaultPaint
	        canvas.drawRect(0,height,width,(height+ reflectiveGap),gapPaint);
	        canvas.drawBitmap(reflectedIggyPart,0,(height+ reflectiveGap),null);
	
	        Paint shaderPaint = new Paint();//paint
	        LinearGradient shaderGradient = new LinearGradient(0,iggyOrginal.getHeight(),0,
	        		(bitmapRelectedEnlargement.getHeight()+reflectiveGap),0x70ffffff,0x00ffffff	,TileMode.CLAMP);//shader
	       shaderPaint.setShader(shaderGradient);
	      shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	        canvas.drawRect(0, height,width,(bitmapRelectedEnlargement.getHeight()+reflectiveGap),shaderPaint);
	        
	        return bitmapRelectedEnlargement;
	  
	    
	     
	}
}
