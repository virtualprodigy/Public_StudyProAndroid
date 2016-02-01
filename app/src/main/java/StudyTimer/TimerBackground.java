package studytimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class TimerBackground extends View {
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	public TimerBackground(Context context) {
		super(context);
		paint.setARGB(255, 242, 101, 34);
		// TODO Auto-generated constructor stub
	}
	
	
	public Canvas drawBack() {

		// savePic();
		Canvas screen = new Canvas();
		screen.drawRGB(242, 101, 34);
        return(screen);
	//	screen.drawCircle(centerX, centerY, radius, paint);
		

	}

}
