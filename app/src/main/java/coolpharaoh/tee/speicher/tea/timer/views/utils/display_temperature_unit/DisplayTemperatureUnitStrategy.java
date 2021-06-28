package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import static java.lang.String.valueOf;

public interface DisplayTemperatureUnitStrategy {

    String getTextIdShowTea(int temperature);

    String getTextNewTea(int temperature);

    static String getFormattedTemperature(final int temperature) {
        String text = "-";
        if (exist(temperature)) {
            text = valueOf(temperature);
        }
        return text;
    }

    static boolean exist(final int temperature) {
        return temperature != -500;
    }
}
