package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

import static coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind.DisplayAmountKind.getFormattedAmount;

class DisplayAmountKindTeaSpoon implements DisplayAmountKind {

    private final Application application;

    DisplayAmountKindTeaSpoon(final Application application) {
        this.application = application;
    }

    @Override
    public String getTextShowTea(final double amount) {
        String text = getFormattedAmount(amount);
        return application.getString(R.string.show_tea_display_ts, text);
    }

    @Override
    public int getImageResourceIdShowTea() {
        return R.drawable.spoon_black;
    }

    @Override
    public String getTextCalculatorShowTea(final float amountPerLiter, final float liter) {
        return application.getString(R.string.show_tea_dialog_amount_per_amount_ts, amountPerLiter, liter);
    }

    @Override
    public String getTextNewTea(double amount) {
        String text = getFormattedAmount(amount);
        return application.getString(R.string.new_tea_edit_text_amount_text_ts, text);
    }
}
