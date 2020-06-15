package coolpharaoh.tee.speicher.tea.timer.views.helper;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.R;

public class HintConversation {

    private HintConversation() {
    }

    public static String getHintTemperature(int variety, String temperatureunit, Context context) {
        switch (variety) {
            case 0:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_blacktea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_blacktea);
            case 1:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_greentea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_greentea);
            case 2:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_yellowtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_yellowtea);
            case 3:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_whitetea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_whitetea);
            case 4:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_oolongtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_oolongtea);
            case 5:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_puerhtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_puerhtea);
            case 6:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_herbaltea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_herbaltea);
            case 7:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_fruittea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_fruittea);
            case 8:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_rooibustea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_rooibustea);
            default:
                return temperatureunit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_other) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_other);
        }
    }

    public static String getHintAmount(int variety, String amountkind, Context context) {
        switch (variety) {
            case 0:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_blacktea) :
                        context.getResources().getString(R.string.newtea_hint_gr_blacktea);
            case 1:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_greentea) :
                        context.getResources().getString(R.string.newtea_hint_gr_greentea);
            case 2:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_yellowtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_yellowtea);
            case 3:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_whitetea) :
                        context.getResources().getString(R.string.newtea_hint_gr_whitetea);
            case 4:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_oolongtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_oolongtea);
            case 5:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_puerhtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_puerhtea);
            case 6:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_herbaltea) :
                        context.getResources().getString(R.string.newtea_hint_gr_herbaltea);
            case 7:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_fruittea) :
                        context.getResources().getString(R.string.newtea_hint_gr_fruittea);
            case 8:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_rooibustea) :
                        context.getResources().getString(R.string.newtea_hint_gr_rooibustea);
            default:
                return amountkind.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_other) :
                        context.getResources().getString(R.string.newtea_hint_gr_other);
        }
    }

    public static String getHintTime(int variety, Context context) {
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
