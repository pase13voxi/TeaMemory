package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind

object DisplayAmountKindFactory {
    @JvmStatic
    operator fun get(amountKind: AmountKind, application: Application): DisplayAmountKindStrategy {
        return when (amountKind) {
            AmountKind.GRAM -> DisplayAmountKindStrategyGram(application)
            AmountKind.TEA_BAG -> DisplayAmountKindStrategyTeaBag(application)
            else -> DisplayAmountKindStrategyTeaSpoon(application)
        }
    }
}