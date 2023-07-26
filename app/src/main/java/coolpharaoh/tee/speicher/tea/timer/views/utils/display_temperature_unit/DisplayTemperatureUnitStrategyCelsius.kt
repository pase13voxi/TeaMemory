package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit.DisplayTemperatureUnitStrategy.Companion.getFormattedTemperature

class DisplayTemperatureUnitStrategyCelsius(private val application: Application)
    : DisplayTemperatureUnitStrategy {

    override fun getTextIdShowTea(temperature: Int): String {
        return application.getString(R.string.show_tea_display_celsius,
            getFormattedTemperature(temperature))
    }

    override fun getTextNewTea(temperature: Int): String {
        return application.getString(R.string.new_tea_edit_text_temperature_text_celsius,
            getFormattedTemperature(temperature))
    }
}