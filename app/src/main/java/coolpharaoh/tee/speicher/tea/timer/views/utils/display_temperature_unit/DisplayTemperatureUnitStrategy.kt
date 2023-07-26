package coolpharaoh.tee.speicher.tea.timer.views.utils.display_temperature_unit

interface DisplayTemperatureUnitStrategy {

    fun getTextIdShowTea(temperature: Int): String

    fun getTextNewTea(temperature: Int): String

    companion object {
        @JvmStatic
        fun getFormattedTemperature(temperature: Int): String {
            var text = "-"
            if (exist(temperature)) {
                text = temperature.toString()
            }
            return text
        }

        fun exist(temperature: Int): Boolean {
            return temperature != -500
        }
    }
}