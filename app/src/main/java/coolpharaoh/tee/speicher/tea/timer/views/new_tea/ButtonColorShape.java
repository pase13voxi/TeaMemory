package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;

class ButtonColorShape {

    private final Application application;
    private final GradientDrawable gradientDrawable;
    private int color;

    ButtonColorShape(Drawable drawable, Application application) {
        gradientDrawable = (GradientDrawable) drawable;
        this.application = application;

        setDefaultColor();
    }

    private void setDefaultColor() {
        int varietyColor = ContextCompat.getColor(application, BLACK_TEA.getColor());
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
