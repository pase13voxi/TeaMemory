package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.BLACK_TEA;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

class ButtonColorShape {

    private final Application application;
    private final GradientDrawable gradientDrawable;
    private int color;

    ButtonColorShape(final Drawable drawable, final Application application) {
        gradientDrawable = (GradientDrawable) drawable;
        this.application = application;

        setDefaultColor();
    }

    private void setDefaultColor() {
        final int varietyColor = ContextCompat.getColor(application, BLACK_TEA.getColor());
        setColor(varietyColor);
    }

    void setColor(final int color) {
        this.color = color;
        gradientDrawable.setColor(color);
    }

    int getColor() {
        return color;
    }
}
