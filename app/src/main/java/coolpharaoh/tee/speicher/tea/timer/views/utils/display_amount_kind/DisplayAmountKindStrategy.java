package coolpharaoh.tee.speicher.tea.timer.views.utils.display_amount_kind;

import java.text.DecimalFormat;

public interface DisplayAmountKindStrategy {
    String getTextShowTea(double amount);

    int getImageResourceIdShowTea();

    String getTextCalculatorShowTea(float amountPerLiter, float liter);

    String getTextNewTea(double amount);

    static String getFormattedAmount(final double amount) {
        String text = "-";
        if (exist(amount)) {
            text = removeZerosFromAmount(amount);
        }
        return text;
    }

    static String removeZerosFromAmount(final double amount) {
        if (amount == (int) amount)
            return String.valueOf((int) amount);
        else {
            final DecimalFormat df = new DecimalFormat("#.#");
            return df.format(amount);
        }
    }

    static boolean exist(final double amount) {
        return amount != -500;
    }
}
