package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.R;

class HintConversation {

    private HintConversation() {
    }

    static String getHintTemperature(int variety, String temperatureUnit, Context context) {
        switch (variety) {
            case 0:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_blacktea, R.string.newtea_hint_fahrenheit_blacktea, context);
            case 1:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_greentea, R.string.newtea_hint_fahrenheit_greentea, context);
            case 2:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_yellowtea, R.string.newtea_hint_fahrenheit_yellowtea, context);
            case 3:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_whitetea, R.string.newtea_hint_fahrenheit_whitetea, context);
            case 4:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_oolongtea, R.string.newtea_hint_fahrenheit_oolongtea, context);
            case 5:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_puerhtea, R.string.newtea_hint_fahrenheit_puerhtea, context);
            case 6:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_herbaltea, R.string.newtea_hint_fahrenheit_herbaltea, context);
            case 7:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_fruittea, R.string.newtea_hint_fahrenheit_fruittea, context);
            case 8:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_rooibustea, R.string.newtea_hint_fahrenheit_rooibustea, context);
            default:
                return getTemperatureHint(temperatureUnit, R.string.newtea_hint_celsius_other, R.string.newtea_hint_fahrenheit_other, context);
        }
    }

    private static String getTemperatureHint(String temperatureunit, int hintCelsius, int hintFahrenheit, Context context) {
        return temperatureunit.equals("Celsius") ?
                context.getResources().getString(hintCelsius) :
                context.getResources().getString(hintFahrenheit);
    }

    static String getHintAmount(int variety, String amountkind, Context context) {
        switch (variety) {
            case 0:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_blacktea, R.string.newtea_hint_gr_blacktea, context);
            case 1:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_greentea, R.string.newtea_hint_gr_greentea, context);
            case 2:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_yellowtea, R.string.newtea_hint_gr_yellowtea, context);
            case 3:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_whitetea, R.string.newtea_hint_gr_whitetea, context);
            case 4:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_oolongtea, R.string.newtea_hint_gr_oolongtea, context);
            case 5:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_puerhtea, R.string.newtea_hint_gr_puerhtea, context);
            case 6:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_herbaltea, R.string.newtea_hint_gr_herbaltea, context);
            case 7:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_fruittea, R.string.newtea_hint_gr_fruittea, context);
            case 8:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_rooibustea, R.string.newtea_hint_gr_rooibustea, context);
            default:
                return getAmountHint(amountkind, R.string.newtea_hint_ts_other, R.string.newtea_hint_gr_other, context);
        }
    }

    private static String getAmountHint(String amountkind, int hintTs, int hintGr, Context context) {
        return amountkind.equals("Ts") ?
                context.getResources().getString(hintTs) :
                context.getResources().getString(hintGr);
    }

    static String getHintTime(int variety, Context context) {
        switch (variety) {
            case 0:
                return context.getResources().getString(R.string.newtea_hint_time_blacktea);
            case 1:
                return context.getResources().getString(R.string.newtea_hint_time_greentea);
            case 2:
                return context.getResources().getString(R.string.newtea_hint_time_yellowtea);
            case 3:
                return context.getResources().getString(R.string.newtea_hint_time_whitetea);
            case 4:
                return context.getResources().getString(R.string.newtea_hint_time_oolongtea);
            case 5:
                return context.getResources().getString(R.string.newtea_hint_time_puerhtea);
            case 6:
                return context.getResources().getString(R.string.newtea_hint_time_herbaltea);
            case 7:
                return context.getResources().getString(R.string.newtea_hint_time_fruittea);
            case 8:
                return context.getResources().getString(R.string.newtea_hint_time_rooibustea);
            default:
                return context.getResources().getString(R.string.newtea_hint_time_other);
        }
    }
}
