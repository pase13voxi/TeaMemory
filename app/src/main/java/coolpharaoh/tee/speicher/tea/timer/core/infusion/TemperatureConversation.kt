package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import java.text.DecimalFormat;

public class TemperatureConversation {

    private TemperatureConversation() {
    }

    public static int celsiusToFahrenheit(final int celsius) {
        if (celsius == -500) {
            return -500;
        } else {
            final float tmp = (float) (9.0 / 5.0 * celsius + 32.0);
            return Math.round(tmp);
        }
    }

    public static int fahrenheitToCelsius(final int fahrenheit) {
        if (fahrenheit == -500) {
            return -500;
        } else {
            final float tmp = (float) ((5.0 / 9.0) * (fahrenheit - 32.0));
            return Math.round(tmp);
        }
    }

    public static String celsiusToCoolDownTime(int celsius){
        if(celsius != 100 && celsius != -500) {
            float tmpTime = (100 - (float) celsius) / 2;
            int minute = (int) tmpTime;
            int sek = (int) ((tmpTime - minute) * 60);
            DecimalFormat df = new DecimalFormat("00");
            return minute + ":" + df.format(sek);
        }else{
            return null;
        }
    }
}
