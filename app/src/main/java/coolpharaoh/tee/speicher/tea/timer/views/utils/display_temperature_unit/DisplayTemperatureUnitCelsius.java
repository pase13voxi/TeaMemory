package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayTemperatureUnitCelsius implements DisplayTemperatureUnit {
    @Override
    public int getTextIdShowTea() {
        return R.string.show_tea_display_celsius;
    }

    @Override
    public int getTextIdEmptyTemperatureNewTea() {
        return R.string.new_tea_edit_text_temperature_empty_text_celsius;
    }

    @Override
    public int getTextIdNewTea() {
        return R.string.new_tea_edit_text_temperature_text_celsius;
    }
}
