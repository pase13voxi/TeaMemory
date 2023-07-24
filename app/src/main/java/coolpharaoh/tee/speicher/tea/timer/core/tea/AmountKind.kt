package coolpharaoh.tee.speicher.tea.timer.core.tea;

public enum AmountKind {
    TEA_SPOON("Ts", 0),
    GRAM("Gr", 1),
    TEA_BAG("Tb", 2);

    private final String text;
    private final int choice;

    AmountKind(final String text, final int choice) {
        this.text = text;
        this.choice = choice;
    }

    public String getText() {
        return text;
    }

    public int getChoice() {
        return choice;
    }

    public static AmountKind fromText(final String text) {
        for (final AmountKind amountSetting : AmountKind.values()) {
            if (amountSetting.text.equalsIgnoreCase(text)) {
                return amountSetting;
            }
        }
        return TEA_SPOON;
    }

    public static AmountKind fromChoice(final int choice) {
        for (final AmountKind amountSetting : AmountKind.values()) {
            if (amountSetting.choice == choice) {
                return amountSetting;
            }
        }
        return TEA_SPOON;
    }
}
