package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.utils.CustomResources;

public class ColorConversation {

    private ColorConversation() {
    }

    public static int getVarietyColor(int variety, Application application) {
        switch (variety) {
            case 0:
                return new CustomResources(application).getColor(R.color.blacktea);
            case 1:
                return new CustomResources(application).getColor(R.color.greentea);
            case 2:
                return new CustomResources(application).getColor(R.color.yellowtea);
            case 3:
                return new CustomResources(application).getColor(R.color.whitetea);
            case 4:
                return new CustomResources(application).getColor(R.color.oolongtea);
            case 5:
                return new CustomResources(application).getColor(R.color.puerhtea);
            case 6:
                return new CustomResources(application).getColor(R.color.herbaltea);
            case 7:
                return new CustomResources(application).getColor(R.color.fruittea);
            case 8:
                return new CustomResources(application).getColor(R.color.rooibustea);
            default:
                return new CustomResources(application).getColor(R.color.other);
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
