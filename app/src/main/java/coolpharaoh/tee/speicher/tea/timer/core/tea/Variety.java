package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

public enum Variety {
    BLACK_TEA("01_black", R.color.blacktea, 0),
    GREEN_TEA("02_green", R.color.greentea, 1),
    YELLOW_TEA("03_yellow", R.color.yellowtea, 2),
    WHITE_TEA("04_white", R.color.whitetea, 3),
    OOLONG_TEA("05_oolong", R.color.oolongtea, 4),
    PU_ERH_TEA("06_pu", R.color.puerhtea, 5),
    HERBAL_TEA("07_herbal", R.color.herbaltea, 6),
    FRUIT_TEA("08_fruit", R.color.fruittea, 7),
    ROOIBUS_TEA("09_rooibus", R.color.rooibustea, 8),
    OTHER("10_other", R.color.other, 9);

    private final String code;
    private final int color;
    private final int choice;

    Variety(final String code, final int color, final int choice) {
        this.code = code;
        this.color = color;
        this.choice = choice;
    }

    public String getCode() {
        return code;
    }

    public int getColor() {
        return color;
    }

    public int getChoice() {
        return choice;
    }

    public static Variety fromStoredText(final String storedText) {
        for (Variety variety : Variety.values()) {
            if (variety.code.equalsIgnoreCase(storedText)) {
                return variety;
            }
        }

        return OTHER;
    }

    public static Variety fromChoice(final int choice) {
        for (final Variety variety : Variety.values()) {
            if (variety.getChoice() == choice) {
                return variety;
            }
        }
        return OTHER;
    }

    public static String convertTextToStoredVariety(final String text, final Application application) {
        final String[] texts = application.getResources().getStringArray(R.array.new_tea_variety_teas);
        final Variety[] varieties = Variety.values();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equals(text)) {
                return varieties[i].getCode();
            }
        }
        return text;
    }

    public static String convertStoredVarietyToText(final String storedText, final Application application) {
        final String[] texts = application.getResources().getStringArray(R.array.new_tea_variety_teas);

        final Variety variety = Variety.fromStoredText(storedText);

        if (OTHER.equals(variety) && !OTHER.getCode().equals(storedText)) {
            return storedText;
        } else {
            return texts[variety.getChoice()];
        }
    }
}
