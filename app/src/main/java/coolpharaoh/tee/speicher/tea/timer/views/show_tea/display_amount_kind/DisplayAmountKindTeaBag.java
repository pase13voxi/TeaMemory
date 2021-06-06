package coolpharaoh.tee.speicher.tea.timer.views.show_tea.display_amount_kind;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindTeaBag implements DisplayAmountKind {

    @Override
    public int getTextId() {
        return R.string.show_tea_display_tb;
    }

    @Override
    public int getImageResourceId() {
        return R.drawable.tea_bag_black;
    }

    @Override
    public int getTextIdCalculator() {
        return R.string.show_tea_dialog_amount_per_amount_tb;
    }
}
