package coolpharaoh.tee.speicher.tea.timer.views.new_tea.suggestions

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R

class HerbalTeaSuggestions(private val application: Application) : Suggestions {

    override val amountTsSuggestions: IntArray
        get() = application.resources.getIntArray(R.array.new_tea_suggestions_herbal_tea_amount_ts)

    override val amountGrSuggestions: IntArray
        get() = application.resources.getIntArray(R.array.new_tea_suggestions_herbal_tea_amount_gr)

    override val amountTbSuggestions: IntArray
        get() = application.resources.getIntArray(R.array.new_tea_suggestions_herbal_tea_amount_tb)

    override val temperatureCelsiusSuggestions: IntArray
        get() = application.resources.getIntArray(R.array.new_tea_suggestions_herbal_tea_temperature_celsius)

    override val temperatureFahrenheitSuggestions: IntArray
        get() = application.resources.getIntArray(R.array.new_tea_suggestions_herbal_tea_temperature_fahrenheit)

    override val timeSuggestions: Array<String>
        get() = application.resources.getStringArray(R.array.new_tea_suggestions_herbal_tea_time)
}