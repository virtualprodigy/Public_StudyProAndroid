package com.virtualprodigy.studypro.layouts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * This view is for the nav drawer. A view must implement
 * checkable for setItemChecked() to work. This allows the
 * user to see their current selected listview item
 *
 * Created by virtualprodigyllc on 10/29/15.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private boolean isChecked;
    private Context context;
    private Drawable originalBackground;

    public CheckableRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, boolean isChecked) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context){
        this.context = context;
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        refreshCheckedState();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    /**
     * This method handles setting the background color of the relativelayout to reflect if it's checked(dleected) or not
     */
    private void refreshCheckedState(){
        if(originalBackground == null){
            originalBackground = getBackground();
        }
        if(isChecked){
            setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }else{
            setBackground(originalBackground);
        }
    }
}
