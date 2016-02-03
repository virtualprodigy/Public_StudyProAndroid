package com.virtualprodigy.studypro.layouts;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.StudyTimer.TimedBreaks;

import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by virtualprodigyllc on 2/1/16.
 */
public class TimerDisplayLayout extends RelativeLayout {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private RectF arcRect;
    private Paint arcPaint;
    private Paint circlePaint;
    private int sweepAngle;
    private int arcCircumference;
    private int circleRadius;
    private Point cicrleCoord;
    private TypedArray timeIncrements;
    private int currentIncrementIndex = -1;
    /**This is the total duration of the timer*/
    private long durationMS = 0;
    /**This is the current milliseconds on the countdown timer*/
    private long remainingMS = 0;
    private boolean isTimerStarted = false;

    protected @Bind(R.id.hourDisplay) TextView hourDisplay;
    protected @Bind(R.id.minuteDisplay) TextView minuteDisplay;
    protected @Bind(R.id.secondDisplay) TextView secondsDisplay;
    protected @Bind(R.id.increaseTimer) Button increaseTimer;
    protected @Bind(R.id.decreaseTimer) Button decreaseTimer;

    @Inject TimedBreaks timedBreaks;

    public TimerDisplayLayout(Context context) {
        super(context);
        init(context);
    }

    public TimerDisplayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimerDisplayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    /**
     * A uniform constructor
     *
     * @param context - context for the view
     */
    @SuppressWarnings("deprecation")
    private void init(Context context) {
        this.context = context;
        Resources res = context.getResources();
        ((StudyProApplication) ((Activity)context).getApplication()).getComponent().inject(this);

        arcRect = new RectF();
        arcPaint = new Paint();
        arcPaint.setColor(res.getColor(R.color.material_blue));
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(res.getDimension(R.dimen.timer_circle_stroke));
        arcPaint.setStyle(Paint.Style.STROKE);

        circlePaint = new Paint(arcPaint);
        circlePaint.setStyle(Paint.Style.FILL);
        circleRadius = (int) res.getDimension(R.dimen.timer_circle_radius);

        timeIncrements = res.obtainTypedArray(R.array.time_increments);

        arcRect = new RectF();
        setWillNotDraw(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displaymetrics);

        //find a square bound and then center it within the view
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        //find the dimension to use for the square bounds
        int circleWidth = screenWidth - padding;
        int circleHeight = screenHeight - padding;
        int sqrtBoundMeasure = circleHeight < circleWidth ? circleHeight : circleWidth;
        arcCircumference = sqrtBoundMeasure;
        //calculate the left and top for the bounds to be centered
        int left = (screenWidth / 2) - (sqrtBoundMeasure / 2);
        int top = (screenHeight / 2) - (sqrtBoundMeasure / 2);
        int right = left + sqrtBoundMeasure;
        int bottom = top + sqrtBoundMeasure;
        arcRect.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateSweepAngle();
        calculateCircleCoordinates();

        canvas.drawArc(arcRect, -90, sweepAngle, false, arcPaint);
        canvas.drawCircle(cicrleCoord.x, cicrleCoord.y, circleRadius, circlePaint);
//                postInvalidateDelayed(1000);
        super.onDraw(canvas);
    }

    /**
     * Calculates the sweep angle of the time display arc
     */
    private void calculateSweepAngle() {
        // draw a full circle, timer not started
        if(!isTimerStarted){
            sweepAngle = 360;
        }else {
            sweepAngle = (int) ((360/durationMS) / remainingMS);
        }
    }

    /**
     * Calculate circle position using the
     * parametric equation for a circle
     */
    private void calculateCircleCoordinates() {
        int arcRadius = (arcCircumference / 2);
        //remember to convert the degree angle to radians
        //remove 90 degrees from the sweep angle since the that's the factor used to adjust the arc initial drawing point
        int x = (int) ((arcRect.left + arcRadius) + arcRadius * Math.cos(((sweepAngle - 90) * Math.PI / 180F)));
        int y = (int) Math.abs((arcRect.top + arcRadius) + arcRadius * Math.sin(((sweepAngle - 90) * Math.PI / 180F)));
        if (cicrleCoord == null) {
            cicrleCoord = new Point();
        }
        cicrleCoord.set(x, y);
    }

    /**
     * This method increases the timer value
     */
    @OnClick(R.id.increaseTimer)
    public void onClickIncreaseTimer() {
        if(!isTimerStarted) {
            currentIncrementIndex = currentIncrementIndex + 1;
            Log.d(TAG, "Index " + currentIncrementIndex);

            if (currentIncrementIndex >= timeIncrements.length()) {
                currentIncrementIndex = 0;
            }

            long time = Long.parseLong(timeIncrements.getString(currentIncrementIndex));
            displayTime(time);
            //set the timed breaks
            timedBreaks.setTimerDuration(time);
        }
    }

    /**
     * This method decreases the timer value
     */
    @OnClick(R.id.decreaseTimer)
    public void onClickDecreaseTimer() {
        if(!isTimerStarted) {
            currentIncrementIndex = currentIncrementIndex - 1;
            Log.d(TAG, "Index " + currentIncrementIndex);

            if (currentIncrementIndex <= 0) {
                currentIncrementIndex = timeIncrements.length() - 1;
            }

            long time = Long.parseLong(timeIncrements.getString(currentIncrementIndex));
            displayTime(time);
            //set the timed breaks
            timedBreaks.setTimerDuration(time);
        }
    }

    /**
     * Parses the hours, minutes, and seconds from the long time
     * then displays then to the user
     *
     * @param time - time in milliseconds
     */
    private void displayTime(long time) {
        Log.d(TAG, "TIME " + time);
        durationMS = time;
        NumberFormat numberFormat;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(2);

        hourDisplay.setText(numberFormat.format((int) (time / 3600000)));
        minuteDisplay.setText(numberFormat.format((int) (time % 3600000) / 60000));
        secondsDisplay.setText(numberFormat.format((int) ((time % 3600000) % 60000) / 1000));
    }

    /**
     * This method sets the current milliseconds and then flags
     * for an invalid to update the animations
     *
     * @param milliseconds - the current milliseconds of the countdown
     */
    public void setRemainingMS(long milliseconds) {
        displayTime(milliseconds);
        remainingMS = milliseconds;
        postInvalidate();
    }

    /**
     * Flags if the timer has been started.
     * If the timer is stopped then the values are reset
     * @param isTimerStarted - true or false the timer is running
     */
    public void setIsTimerStarted(boolean isTimerStarted){
        this.isTimerStarted = isTimerStarted;
        if(!isTimerStarted){
            resetDisplayValues();
        }
    }

    private void resetDisplayValues(){
        hourDisplay.setText(R.string.zero_time);
        minuteDisplay.setText(R.string.zero_time);
        secondsDisplay.setText(R.string.zero_time);
        durationMS = 0;
        remainingMS = 0;
        currentIncrementIndex = -1;

    }
}
