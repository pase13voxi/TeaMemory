package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.ColorConversation;

class ButtonColorShape {

    private static final int VARIETY_BLACK_TEA = 0;

    private final Context context;
    private final GradientDrawable gradientDrawable;
    private int color;

    ButtonColorShape(Drawable drawable, Context context) {
        gradientDrawable = (GradientDrawable) drawable;
        this.context = context;

        setDefaultColor();
    }

    private void setDefaultColor() {
        setColorByVariety(VARIETY_BLACK_TEA);
    }

    void setColor(int argb) {
        this.color = argb;
        gradientDrawable.setColor(argb);
    }

    int getColor() {
        return color;
    }

    void setColorByVariety(int variety) {
        color = ColorConversation.getVarietyColor(variety, context);
        gradientDrawable.setColor(color);
    }
}
