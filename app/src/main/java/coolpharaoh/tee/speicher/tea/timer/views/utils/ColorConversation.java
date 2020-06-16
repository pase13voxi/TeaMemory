package coolpharaoh.tee.speicher.tea.timer.views.utils;

import android.content.Context;
import android.graphics.Color;

import coolpharaoh.tee.speicher.tea.timer.R;

public class ColorConversation {

    private ColorConversation() {
    }

    public static int getVarietyColor(int variety, Context context) {
        switch (variety) {
            case 0:
                return context.getResources().getColor(R.color.blacktea);
            case 1:
                return context.getResources().getColor(R.color.greentea);
            case 2:
                return context.getResources().getColor(R.color.yellowtea);
            case 3:
                return context.getResources().getColor(R.color.whitetea);
            case 4:
                return context.getResources().getColor(R.color.oolongtea);
            case 5:
                return context.getResources().getColor(R.color.puerhtea);
            case 6:
                return context.getResources().getColor(R.color.herbaltea);
            case 7: return context.getResources().getColor(R.color.fruittea);
            case 8: return context.getResources().getColor(R.color.rooibustea);
            default: return context.getResources().getColor(R.color.other);
        }
    }

    public static int discoverForegroundColor(int color){
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        double lum = ((0.299 * red) + ((0.587 * green) + (0.114 * blue)));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;

    }
}
