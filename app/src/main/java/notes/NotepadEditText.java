package notes;

import com.virtualprodigy.studypro.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class NotepadEditText  extends EditText {
	private Rect mRect;
	private Paint mPaint;
	private Context mContext;

	public NotepadEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setColor(getResources().getColor(R.color.black)); 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getHeight();
		int lineHeight = getLineHeight();

		int numLinesToDraw = height / lineHeight;

		if (getLineCount() > numLinesToDraw)
			numLinesToDraw = getLineCount();//for long text with scrolling
//		numLinesToDraw = numLinesToDraw <= 1 ? 200 : numLinesToDraw;
		Rect r = mRect;
		Paint paint = mPaint;
		int baseline = getLineBounds(0, r);//the first line

		for (int i = 0; i < numLinesToDraw; i++) {

			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
			baseline += getLineHeight();//next line draw
		}

		super.onDraw(canvas);
	}
}
