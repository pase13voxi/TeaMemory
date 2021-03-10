package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import androidx.core.content.ContextCompat;

import coolpharaoh.tee.speicher.tea.timer.R;

public class ColorConversation {

    private ColorConversation() {
    }

    public static int getVarietyColor(final int variety, final Application application) {
        switch (variety) {
            case 0:
                return ContextCompat.getColor(application, R.color.blacktea);
            case 1:
                return ContextCompat.getColor(application, R.color.greentea);
            case 2:
                return ContextCompat.getColor(application, R.color.yellowtea);
            case 3:
                return ContextCompat.getColor(application, R.color.whitetea);
            case 4:
                return ContextCompat.getColor(application, R.color.oolongtea);
            case 5:
                return ContextCompat.getColor(application, R.color.puerhtea);
            case 6:
                return ContextCompat.getColor(application, R.color.herbaltea);
            case 7:
                return ContextCompat.getColor(application, R.color.fruittea);
            case 8:
                return ContextCompat.getColor(application, R.color.rooibustea);
            default:
                return ContextCompat.getColor(application, R.color.other);
        }
    }

    public static int discoverForegroundColor(final int color) {
        final int red = (color >> 16) & 0x000000FF;
        final int green = (color >> 8) & 0x000000FF;
        final int blue = (color) & 0x000000FF;
        final double lum = ((0.299 * red) + ((0.587 * green) + (0.114 * blue)));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;

    }
}
