package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindTeaSpoon implements DisplayAmountKind {

    @Override
    public int getTextId() {
        return R.string.show_tea_display_ts;
    }

    @Override
    public int getImageResourceId() {
        return R.drawable.spoon_black;
    }

    @Override
    public int getTextIdCalculator() {
        return R.string.show_tea_dialog_amount_per_amount_ts;
    }
}
