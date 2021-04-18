package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import coolpharaoh.tee.speicher.tea.timer.core.tea.ColorConversation;

class ButtonColorShape {

    private static final int VARIETY_BLACK_TEA = 0;

    private final Application application;
    private final GradientDrawable gradientDrawable;
    private int color;

    ButtonColorShape(Drawable drawable, Application application) {
        gradientDrawable = (GradientDrawable) drawable;
        this.application = application;

        setDefaultColor();
    }

    private void setDefaultColor() {
        int varietyColor = ColorConversation.getVarietyColor(VARIETY_BLACK_TEA, application);
        setColor(varietyColor);
    }

    void setColor(int color) {
        this.color = color;
        gradientDrawable.setColor(color);
    }

    int getColor() {
        return color;
    }
}
