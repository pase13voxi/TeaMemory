package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

public class ColorConversation {

    private ColorConversation() {
    }

    public static int getVarietyColor(int variety, Application application) {
        switch (variety) {
            case 0:
                return application.getResources().getColor(R.color.blacktea);
            case 1:
                return application.getResources().getColor(R.color.greentea);
            case 2:
                return application.getResources().getColor(R.color.yellowtea);
            case 3:
                return application.getResources().getColor(R.color.whitetea);
            case 4:
                return application.getResources().getColor(R.color.oolongtea);
            case 5:
                return application.getResources().getColor(R.color.puerhtea);
            case 6:
                return application.getResources().getColor(R.color.herbaltea);
            case 7:
                return application.getResources().getColor(R.color.fruittea);
            case 8:
                return application.getResources().getColor(R.color.rooibustea);
            default:
                return application.getResources().getColor(R.color.other);
        }
    }

    public static int discoverForegroundColor(int color){
        int red = (color >> 16) & 0x000000FF;
        int green = (color >>8 ) & 0x000000FF;
        int blue = (color) & 0x000000FF;
        double lum = ((0.299 * red) + ((0.587 * green) + (0.114 * blue)));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;

    }
}
