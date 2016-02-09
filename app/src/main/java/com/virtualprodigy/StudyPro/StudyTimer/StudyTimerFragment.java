package com.virtualprodigy.studypro.StudyTimer;

import java.io.IOException;
import java.text.NumberFormat;

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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.Utils.Prefs;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.Tutorial;
import com.virtualprodigy.studypro.Utils.settingmenubuttons;
import com.virtualprodigy.studypro.layouts.TimerDisplayLayout;
import com.virtualprodigy.studypro.ottoBus.OttoHelper;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.StudyTimerSnackEvent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyTimerFragment extends Fragment {
	private final String TAG = this.getClass().getSimpleName();
	private Context fragmentContext;

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
	static int allowRestartBroadcast = 0;
	private boolean IggyVisiable;
	private SharedPreferences tutorialPrefs;

	boolean startVis;
	static int tCAble;

	private NotificationManager statusNoti;

	int ptotal;
	static MediaPlayer notSound = new MediaPlayer();

	double tickTime;
	TickReceive tickR = new TickReceive();
	FinishReceive finR = new FinishReceive();
	int TaskId;
	static int displayTutorial;

	private Intent startTimerService;

	protected @Bind(R.id.mascotSpeechTV) TextView popText;
	protected @Bind(R.id.mascotSpeechContainer) ScrollView sV;
	protected @Bind(R.id.mascotIcon) ImageView showingIggyReflected;
    protected @Bind(R.id.snackbarContainer) CoordinatorLayout coordinatorLayout;
    protected @Bind(R.id.timerDisplayLayout) TimerDisplayLayout timerDisplay;
    boolean isAlarm = false;
    boolean isTimerRunning = false;
	View displayEmptyListMessage;

    @Inject TimedBreaks timedBreaks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.study_timer_fragment, container, false);
		ButterKnife.bind(this, view);
		fragmentContext = getActivity();
		((StudyProApplication) getActivity().getApplication()).getComponent().inject(this);

		statusNoti = (NotificationManager) fragmentContext.getSystemService(Context.NOTIFICATION_SERVICE);
		startTimerService = new Intent(fragmentContext, TimerService.class);

		View timer = new TextView(fragmentContext);

        //displays a tutorial
        displayTutorial();

        displayReflectedIggy();

        return view;
	}

    /**
     * Displays a reflected effect to iggy
     */
    private void displayReflectedIggy() {
        showingIggyReflected.setImageBitmap(createReflectedImage(BitmapFactory.decodeResource(getResources(), R.drawable.iggy)));
    }

    /**
     * Displays the tutorial
     */
    private void displayTutorial() {
        tutorialPrefs = fragmentContext.getApplicationContext().getSharedPreferences("showHow", fragmentContext.MODE_PRIVATE);
        displayTutorial = tutorialPrefs.getInt("showHow", 0);

        if (displayTutorial == 0 ) {
            Intent Guide = new Intent(fragmentContext, Tutorial.class);
            startActivity(Guide);
        }
    }

    /**
     * Stops the the timer
     */
    @OnClick(R.id.stopTimerFAB)
    public void onClickStop() {
        try {
            if (isAlarm) {
                //the alarm is going off handle
                StudyTimerFragment.alarm.stop();
                vib.cancel();
                tCAble = 0;
            } else {
                //the user is stopping the timer early
                fragmentContext.stopService(startTimerService);
                tCAble = 0;
            }
            isTimerRunning = false;
            timerDisplay.setIsTimerStarted(isTimerRunning);
        } catch (Exception e) {
            Log.e(TAG, "Failed to stop the timer", e);
        }
    }

    /**
     * Starts the study timer
     */
    @OnClick(R.id.startTimerFAB)
    public void onClickStart() {
		try {
			Log.i(TAG, "Starting timer service");
			startTimerService.putExtra(TimerService.EXTRA_TIME_AMOUNT, time4Count);
			startTimerService.putExtra(TimerService.EXTRA_TIME_LIMIT, timerDisplay.getDurationMS());

			new Thread() {
				public void run() {
					fragmentContext.startService(startTimerService);
				}
			}.start();
            isTimerRunning = true;
            timerDisplay.setIsTimerStarted(isTimerRunning);
		} catch (NullPointerException e) {
			 Log.e(TAG, "Could not start timer", e);
            Snackbar.make(coordinatorLayout, R.string.please_select_time, Snackbar.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
        OttoHelper.getInstance().register(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
        OttoHelper.getInstance().unregister(this);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(fragmentContext, settingmenubuttons.class));
			return true;
        default:
             return false;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
        //TODO change the action names
		IggyVisiable = false;
		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
		send.setAction("com.gtech.CramSlam.died");
		fragmentContext.sendBroadcast(send);

		SharedPreferences.Editor edit = tutorialPrefs.edit();
		edit.putInt("showHow", displayTutorial);
		edit.commit();
	}

	@Override
	public void onResume() {

		super.onResume();
		IntentFilter intentFilterFin = new IntentFilter(
				"com.gtech.CramSlam.finish");
		fragmentContext.registerReceiver(finR, intentFilterFin);
		IggyVisiable = true;

		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
        //TODO add in main activity the code to pass the intent to this class
//		allowRestartBroadcast = getIntent().getIntExtra("allowRestartBroadcastMethod", 0);
//
//		if (allowRestartBroadcast == 0) {
//			send.setAction("com.gtech.CramSlam.died");
//		}
//		sendBroadcast(send);
//		IntentFilter intentFilterTick = new IntentFilter(
//				"com.gtech.CramSlam.tick");
//		registerReceiver(tickR, intentFilterTick);
//
//		tutorialPrefs = getApplicationContext().getSharedPreferences("showHow",
//				MODE_PRIVATE);
//		displayTutorial = tutorialPrefs.getInt("showHow", 0);

	}

	@Override
	public void onDestroy() {

		IggyVisiable = false;
		Intent send = new Intent();
		send.putExtra("IggyVisiable", IggyVisiable);
		send.setAction("com.gtech.CramSlam.died");
		fragmentContext.sendBroadcast(send);

		super.onDestroy();
	}

    @Subscribe
    public void receiveStudyTimerSnackEvent(StudyTimerSnackEvent event){
        Snackbar.make(coordinatorLayout, event.message, Snackbar.LENGTH_SHORT).show();
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
					isTimerRunning = true;
                    timerDisplay.setIsTimerStarted(isTimerRunning);
					tCAble = 1;
				} catch (Exception e) {
				}
			}

            NumberFormat nf;
            nf = NumberFormat.getInstance();
            nf.setMinimumIntegerDigits(2);

            //TODO make otto event from alarm to post the new message iggySayViewable(timedBreaks.(tickTime));
            timerDisplay.setRemainingMS((long) tickTime);

		}
	}

	public class FinishReceive extends BroadcastReceiver {
		// public static final String CUSTOM_INTENT = "com.time";
		private final String TAG = null;

		Toast toast;

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
                isTimerRunning = true;
                timerDisplay.setIsTimerStarted(isTimerRunning);
                isAlarm = true;
            } catch (Exception e) {

			} finally {
				tCAble = 0;
			}

			Context c = fragmentContext;
            Snackbar.make(coordinatorLayout, R.string.time_is_up_message, Snackbar.LENGTH_LONG).show();

			vib = (Vibrator) fragmentContext.getSystemService(Context.VIBRATOR_SERVICE);

			long[] vibpat = { 0, 200, 200, 200, 200, 200, 500, 500, 200, 200,
					200, 200, 200, 500 };
			// Vibrate for 300 milliseconds

			iggySayViewable("Yahoo, we finished studying!!");
			try {
				alarm = new MediaPlayer();
				alarm.setDataSource(c, RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_ALARM));
			} catch (IllegalArgumentException e) {
                Log.e(TAG, "Failed to create alarm", e);
			} catch (SecurityException e) {
                Log.e(TAG, "Failed to create alarm", e);
			} catch (IllegalStateException e) {
                Log.e(TAG, "Failed to create alarm", e);
			} catch (IOException e) {
                Log.e(TAG, "Failed to create alarm", e);
			} catch (NullPointerException e) {
                Log.e(TAG, "Failed to create alarm", e);
			}
			final AudioManager audioManager = (AudioManager) fragmentContext.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				alarm.setAudioStreamType(AudioManager.STREAM_ALARM);
				alarm.setLooping(true);
				try {

					alarm.prepare();

				} catch (IllegalStateException e) {
                    Log.e(TAG, "Failed to prepare alarm", e);
				} catch (IOException e) {
                    Log.e(TAG, "Failed to prepare alarm", e);
				}
				if (Prefs.getAlarm(c)) {
					alarm.start();
				}

				if (Prefs.getVib(c)) {
					vib.vibrate(vibpat, 1);

				}
			}
		}
	}

    /**
     * this method checks if you are on a break or not from the
     * timedbreak class and then the method check if your in the app
     * or not, if you are the message is shown by iggy if not a pop up
     * message is displayed
     * @param iS
     */
    public void iggySayViewable(String iS) {

        if (iS.equals("iggy")) {

        } else {
            if (IggyVisiable == true) {

                popText.setText(iS);
                sV.setVisibility(View.VISIBLE);
                extraTime quickie;
                quickie = new extraTime(7000, 1000, 1);
                quickie.start();

                Context cc = getContext();
                Vibrator breakvib;
                breakvib = (Vibrator) fragmentContext.getSystemService(Context.VIBRATOR_SERVICE);

                long[] vibpat = {0, 200, 200, 200, 200};
                // Vibrate for 300 milliseconds
                if (Prefs.getBVib(cc))
                    breakvib.vibrate(vibpat, -1);

                try {

                    // here's where I make the noficiation sound
                    notSound.setDataSource(cc, RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Failed to start the notifcation sound", e);
                } catch (SecurityException e) {
                    Log.e(TAG, "Failed to start the notifcation sound", e);
                } catch (IllegalStateException e) {
                    Log.e(TAG, "Failed to start the notifcation sound", e);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to start the notifcation sound", e);
                }

                final AudioManager audioManager = (AudioManager) fragmentContext.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    notSound.setAudioStreamType(AudioManager.STREAM_ALARM);
                    notSound.setLooping(false);
                    try {
                        // j.prepare();
                        notSound.prepare();

                    } catch (IllegalStateException e) {
                        Log.e(TAG, "Failed to start the notifcation sound", e);
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to start the notifcation sound", e);
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


	public static void displayHowCheckBox(int value) {
		// displayHow = value;
	}

	public static void allowRestartBroadcastMethod(int v) {
	//	Log.d("studytimer_resume", "no restart" + v);
		allowRestartBroadcast = v;
	}

    /**
     * Draws a reflected images, similar to something reflected by water
     * @param imageOriginal
     * @return
     */
    private static Bitmap createReflectedImage(Bitmap imageOriginal) {
        final int reflectiveGap = 0;
        int width = imageOriginal.getWidth();
        int height = imageOriginal.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        //reflectionImage
        Bitmap reflectedIggyPart = Bitmap.createBitmap(imageOriginal, 0, height / 2, width, height / 2, matrix, false);
        //bitmapWithReflection
        Bitmap bitmapRelectedEnlargement = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapRelectedEnlargement);
        canvas.drawBitmap(imageOriginal, 0, 0, null);
        //deafaultPaint
        Paint gapPaint = new Paint();
        canvas.drawRect(0, height, width, (height + reflectiveGap), gapPaint);
        canvas.drawBitmap(reflectedIggyPart, 0, (height + reflectiveGap), null);

        Paint shaderPaint = new Paint();//paint
        LinearGradient shaderGradient = new LinearGradient(0, imageOriginal.getHeight(), 0,
                (bitmapRelectedEnlargement.getHeight() + reflectiveGap), 0x70ffffff, 0x00ffffff, TileMode.CLAMP);//shader
        shaderPaint.setShader(shaderGradient);
        shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0, height, width, (bitmapRelectedEnlargement.getHeight() + reflectiveGap), shaderPaint);

        return bitmapRelectedEnlargement;
    }
}
