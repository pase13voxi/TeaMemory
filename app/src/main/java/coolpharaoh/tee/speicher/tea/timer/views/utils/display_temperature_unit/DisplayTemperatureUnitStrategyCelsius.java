package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitStrategy.getFormattedTemperature;

class DisplayTemperatureUnitStrategyCelsius implements DisplayTemperatureUnitStrategy {

    private final Application application;

    public DisplayTemperatureUnitStrategyCelsius(final Application application) {
        this.application = application;
    }

    @Override
    public String getTextIdShowTea(final int temperature) {
        return application.getString(R.string.show_tea_display_celsius, getFormattedTemperature(temperature));
    }

    @Override
    public String getTextNewTea(final int temperature) {
        return application.getString(R.string.new_tea_edit_text_temperature_text_celsius, getFormattedTemperature(temperature));
    }
}
