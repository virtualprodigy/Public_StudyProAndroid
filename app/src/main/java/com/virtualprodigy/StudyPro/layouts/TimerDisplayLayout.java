package com.virtualprodigy.studypro.layouts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.virtualprodigy.studypro.R;

/**
 * Created by virtualprodigyllc on 2/1/16.
 */
public class TimerDisplayLayout extends RelativeLayout {
    private Context context;
    private Resources res;
    private RectF arcRect;
    private Paint arcPaint;
    private Paint circlePaint;
    private int sweepAngle;
    private int arcCircumference;
    private int circleRadius;
    private Point cicrleCoord;

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

    public TimerDisplayLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * A uniform constructor
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        this.res = context.getResources();

        arcRect = new RectF();
        arcPaint = new Paint();
        arcPaint.setColor(res.getColor(R.color.material_blue));
        arcPaint.setAntiAlias(true);
        arcPaint.setStrokeWidth(res.getDimension(R.dimen.timer_circle_stroke));
        arcPaint.setStyle(Paint.Style.STROKE);

        circlePaint = new Paint(arcPaint);
//        circlePaint = new Paint();
//        circlePaint.setColor(res.getColor(R.color.material_blue));
//        circlePaint.setAntiAlias(true);
//        circlePaint.setStrokeWidth(res.getDimension(R.dimen.timer_circle_stroke));
        circlePaint.setStyle(Paint.Style.FILL);
        circleRadius = (int) res.getDimension(R.dimen.timer_circle_radius);
        setWillNotDraw(false);
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

        arcRect = new RectF(left, top, right, bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //calculate the sweepAngle
        sweepAngle = 180;
        calculateCircleCoordinates();
        super.dispatchDraw(canvas);
    }

    /**
     * Calculate circle position using the
     * parametric equation for a circle
     */
    private void calculateCircleCoordinates() {
        int arcRadius = (arcCircumference/2);
        //remember to convert the degree angle to radians
        //remove 90 degrees from the sweep angle since the that's the factor used to adjust the arc initial drawing point
        int x = (int) ((arcRect.left + arcRadius) + arcRadius * Math.cos(((sweepAngle - 90) * Math.PI / 180F)));
        int y = (int) Math.abs ( (arcRect.top + arcRadius) + arcRadius * Math.sin(((sweepAngle -90) * Math.PI / 180F)));
        cicrleCoord = new Point();
        cicrleCoord.set(x,y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(arcRect, -90, sweepAngle, false, arcPaint);
        if(cicrleCoord != null)
        canvas.drawCircle(cicrleCoord.x, cicrleCoord.y, circleRadius, circlePaint);
//                postInvalidateDelayed(1000);
        super.onDraw(canvas);
    }
}
