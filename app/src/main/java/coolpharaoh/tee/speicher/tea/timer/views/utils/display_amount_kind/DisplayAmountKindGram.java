package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import coolpharaoh.tee.speicher.tea.timer.R;

class DisplayAmountKindGram implements DisplayAmountKind {

    @Override
    public int getTextIdShowTea() {
        return R.string.show_tea_display_gr;
    }

    @Override
    public int getImageResourceIdShowTea() {
        return R.drawable.gram_black;
    }

    @Override
    public int getTextIdCalculatorShowTea() {
        return R.string.show_tea_dialog_amount_per_amount_gr;
    }

    @Override
    public int getTextIdNewTea() {
        return R.string.new_tea_edit_text_amount_text_gr;
    }
}
