package coolpharaoh.tee.speicher.tea.timer.viewmodels.helper;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.R;

public class LanguageConversation {
    public static String convertVarietyToCode(String variety, Context context){
        String[] varieties = context.getResources().getStringArray(R.array.variety_teas);
        String[] codes = context.getResources().getStringArray(R.array.variety_codes);

        for(int i=0; i<varieties.length; i++){
            if(varieties[i].equals(variety)){
                return codes[i];
            }
        }
        return variety;
    }

    public static String convertCodeToVariety(String code, Context context){
        String[] codes = context.getResources().getStringArray(R.array.variety_codes);
        String[] varieties = context.getResources().getStringArray(R.array.variety_teas);

        for(int i=0; i<codes.length; i++){
            if(codes[i].equals(code)){
                return varieties[i];
            }
        }
        return code;
    }
}
