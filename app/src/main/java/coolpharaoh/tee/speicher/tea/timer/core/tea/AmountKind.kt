package coolpharaoh.tee.speicher.tea.timer.core.tea

enum class AmountKind(val text: String, val choice: Int) {
    TEA_SPOON("Ts", 0),
    GRAM("Gr", 1),
    TEA_BAG("Tb", 2);

    companion object {
        @JvmStatic
        fun fromText(text: String?): AmountKind {
            for (amountSetting in values()) {
                if (amountSetting.text.equals(text, ignoreCase = true)) {
                    return amountSetting
                }
            }
            return TEA_SPOON
        }

        @JvmStatic
        fun fromChoice(choice: Int): AmountKind {
            for (amountSetting in values()) {
                if (amountSetting.choice == choice) {
                    return amountSetting
                }
            }
            return TEA_SPOON
        }
    }
}