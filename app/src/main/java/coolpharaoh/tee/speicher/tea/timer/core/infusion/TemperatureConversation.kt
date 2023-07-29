package coolpharaoh.tee.speicher.tea.timer.core.infusion

import java.text.DecimalFormat
import kotlin.math.roundToInt

object TemperatureConversation {
    @JvmStatic
    fun celsiusToFahrenheit(celsius: Int): Int {
        return if (celsius == -500) {
            -500
        } else {
            val tmp = (9.0 / 5.0 * celsius + 32.0).toFloat()
            tmp.roundToInt()
        }
    }

    @JvmStatic
    fun fahrenheitToCelsius(fahrenheit: Int): Int {
        return if (fahrenheit == -500) {
            -500
        } else {
            val tmp = (5.0 / 9.0 * (fahrenheit - 32.0)).toFloat()
            tmp.roundToInt()
        }
    }

    @JvmStatic
    fun celsiusToCoolDownTime(celsius: Int): String? {
        return if (celsius != 100 && celsius != -500) {
            val tmpTime = (100 - celsius.toFloat()) / 2
            val minutes = tmpTime.toInt()
            val seconds = ((tmpTime - minutes) * 60).toInt()
            val decimalFormat = DecimalFormat("00")
            minutes.toString() + ":" + decimalFormat.format(seconds.toLong())
        } else {
            null
        }
    }
}