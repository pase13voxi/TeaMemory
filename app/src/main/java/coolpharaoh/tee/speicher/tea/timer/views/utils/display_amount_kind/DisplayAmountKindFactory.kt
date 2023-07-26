package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind;

public class DisplayAmountKindFactory {

    private DisplayAmountKindFactory() {
    }

    public static DisplayAmountKindStrategy get(final AmountKind amountKind, final Application application) {
        switch (amountKind) {
            case GRAM:
                return new DisplayAmountKindStrategyGram(application);
            case TEA_BAG:
                return new DisplayAmountKindStrategyTeaBag(application);
            default:
                return new DisplayAmountKindStrategyTeaSpoon(application);
        }
    }
}
