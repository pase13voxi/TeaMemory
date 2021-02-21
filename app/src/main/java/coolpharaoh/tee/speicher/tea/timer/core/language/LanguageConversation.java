package coolpharaoh.tee.speicher.tea.timer.core.language;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;

public class LanguageConversation {

    private LanguageConversation() {
    }

    public static String convertVarietyToCode(String variety, Application application) {
        String[] varieties = application.getResources().getStringArray(R.array.new_tea_variety_teas);
        String[] codes = application.getResources().getStringArray(R.array.new_tea_variety_codes);

        for (int i = 0; i < varieties.length; i++) {
            if (varieties[i].equals(variety)) {
                return codes[i];
            }
        }
        return variety;
    }

    public static String convertCodeToVariety(String code, Application application) {
        String[] codes = application.getResources().getStringArray(R.array.new_tea_variety_codes);
        String[] varieties = application.getResources().getStringArray(R.array.new_tea_variety_teas);

        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(code)) {
                return varieties[i];
            }
        }
        return code;
    }
}
