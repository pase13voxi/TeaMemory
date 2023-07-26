package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import java.text.DecimalFormat

interface DisplayAmountKindStrategy {

    fun getTextShowTea(amount: Double): String

    fun getImageResourceIdShowTea(): Int

    fun getTextCalculatorShowTea(amountPerLiter: Float, liter: Float): String

    fun getTextNewTea(amount: Double): String

    companion object {
        @JvmStatic
        fun getFormattedAmount(amount: Double): String {
            var text = "-"
            if (exist(amount)) {
                text = removeZerosFromAmount(amount)
            }
            return text
        }

        fun removeZerosFromAmount(amount: Double): String {
            return if (amount == amount.toInt().toDouble()) amount.toInt().toString()
            else {
                val df = DecimalFormat("#.#")
                df.format(amount)
            }
        }

        fun exist(amount: Double): Boolean {
            return amount != -500.0
        }
    }
}