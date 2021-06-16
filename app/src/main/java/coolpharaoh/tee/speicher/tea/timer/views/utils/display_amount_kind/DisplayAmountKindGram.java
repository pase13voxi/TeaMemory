package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindGram implements DisplayAmountKind {

    private final Application application;

    public DisplayAmountKindGram(final Application application) {
        this.application = application;
    }

    @Override
    public String getTextShowTea(final double amount) {
        String text = DisplayAmountKind.getFormattedAmount(amount);
        return application.getString(R.string.show_tea_display_gr, text);
    }

    @Override
    public int getImageResourceIdShowTea() {
        return R.drawable.gram_black;
    }

    @Override
    public String getTextCalculatorShowTea(final float amountPerLiter, final float liter) {
        return application.getString(R.string.show_tea_dialog_amount_per_amount_gr, amountPerLiter, liter);
    }

    @Override
    public String getTextNewTea(final double amount) {
        String text = DisplayAmountKind.getFormattedAmount(amount);
        return application.getString(R.string.new_tea_edit_text_amount_text_gr, text);
    }
}
