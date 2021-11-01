package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKindStrategy.getFormattedAmount;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindStrategyTeaBag implements DisplayAmountKindStrategy {

    private final Application application;

    public DisplayAmountKindStrategyTeaBag(final Application application) {
        this.application = application;
    }

    @Override
    public String getTextShowTea(final double amount) {
        final String text = getFormattedAmount(amount);
        return application.getString(R.string.show_tea_display_tb, text);
    }

    @Override
    public int getImageResourceIdShowTea() {
        return R.drawable.tea_bag_black;
    }

    @Override
    public String getTextCalculatorShowTea(final float amountPerLiter, final float liter) {
        return application.getString(R.string.show_tea_dialog_amount_per_amount_tb, amountPerLiter, liter);
    }

    @Override
    public String getTextNewTea(final double amount) {
        final String text = getFormattedAmount(amount);
        return application.getString(R.string.new_tea_edit_text_amount_text_tb, text);
    }
}
