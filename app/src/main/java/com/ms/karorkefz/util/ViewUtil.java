package com.ms.karorkefz.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ViewUtil {
    public static Drawable genBackgroundDefaultDrawable() {
        return genBackgroundDefaultDrawable( Color.TRANSPARENT );
    }

    public static Drawable genBackgroundDefaultDrawable(int defaultColor) {
        StateListDrawable statesDrawable = new StateListDrawable();
        statesDrawable.addState( new int[]{android.R.attr.state_pressed}, new ColorDrawable( 0xFFE5E5E5 ) );
        statesDrawable.addState( new int[]{}, new ColorDrawable( defaultColor ) );
        return statesDrawable;
    }
}
