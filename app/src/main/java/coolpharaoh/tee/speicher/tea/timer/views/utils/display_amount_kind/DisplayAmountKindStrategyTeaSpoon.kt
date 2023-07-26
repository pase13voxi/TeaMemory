package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindStrategy.Companion.getFormattedAmount

class DisplayAmountKindStrategyTeaSpoon(private val application: Application) :
    DisplayAmountKindStrategy {

    override fun getTextShowTea(amount: Double): String {
        val text = getFormattedAmount(amount)
        return application.getString(R.string.show_tea_display_ts, text)
    }

    override fun getImageResourceIdShowTea(): Int {
        return R.drawable.spoon_black
    }

    override fun getTextCalculatorShowTea(amountPerLiter: Float, liter: Float): String {
        return application.getString(R.string.show_tea_dialog_amount_per_amount_ts,
            amountPerLiter, liter)
    }

    override fun getTextNewTea(amount: Double): String {
        val text = getFormattedAmount(amount)
        return application.getString(R.string.new_tea_edit_text_amount_text_ts, text)
    }
}