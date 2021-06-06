package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindGram implements DisplayAmountKind {

    @Override
    public int getTextId() {
        return R.string.show_tea_display_gr;
    }

    @Override
    public int getImageResourceId() {
        return R.drawable.gram_black;
    }

    @Override
    public int getTextIdCalculator() {
        return R.string.show_tea_dialog_amount_per_amount_gr;
    }
}
