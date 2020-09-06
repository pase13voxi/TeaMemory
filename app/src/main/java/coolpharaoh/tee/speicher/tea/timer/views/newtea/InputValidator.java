package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Pattern;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;

public class InputValidator {
    private Context applicationContext;

    InputValidator(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    boolean nameIsNotEmpty(String nameInput) {
        if ("".equals(nameInput)) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    boolean nameIsValid(String nameInput) {
        if (nameInput.length() > 300) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_name, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    boolean varietyIsValid(String varietyInput) {
        if (varietyInput.length() > 30) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    boolean amountIsValid(String amountInput) {
        if ("".equals(amountInput)) {
            return true;
        } else if (amountInput.contains(".") || amountInput.length() > 3) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_amount, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    boolean infusionIsValid(String temperatureInput, String temperatureUnit, String coolDownTimeInput, String timeInput) {
        return temperatureIsValid(temperatureInput, temperatureUnit)
                && coolDownTimeIsValid(coolDownTimeInput)
                && steepingTimeIsValid(timeInput);
    }

    private boolean coolDownTimeIsValid(String coolDownTimeInput) {
        if (!timeIsValid(coolDownTimeInput)) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean steepingTimeIsValid(String timeInput) {
        if (!timeIsValid(timeInput)) {
            Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    private boolean timeIsValid(String time) {
        boolean timeValid;

        timeValid = time.length() < 6;
        if (timeValid && !"".equals(time)) {
            boolean formatMinutes = Pattern.matches("\\d\\d", time) || Pattern.matches("\\d", time);
            boolean formatSeconds = Pattern.matches("\\d\\d:\\d\\d", time) || Pattern.matches("\\d:\\d\\d", time);
            if (formatMinutes) {
                timeValid = Integer.parseInt(time) < 60;
            } else if (formatSeconds) {
                String[] split = time.split(":");
                timeValid = Integer.parseInt(split[0]) < 60 && Integer.parseInt(split[1]) < 60;
            } else {
                timeValid = false;
            }
        }
        return timeValid;
    }

    private boolean temperatureIsValid(String temperatureInput, String temperatureUnit) {
        if (!temperatureInputIsValid(temperatureInput, temperatureUnit)) {
            if ("Celsius".equals(temperatureUnit)) {
                Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                toast.show();
            } else if ("Fahrenheit".equals(temperatureUnit)) {
                Toast toast = Toast.makeText(applicationContext, R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                toast.show();
            }
            return false;
        }
        return true;
    }

    boolean temperatureInputIsValid(String temperature, String temperatureUnit) {
        boolean temperatureValid = true;
        if (!"".equals(temperature)) {
            if (temperature.contains(".") || temperature.length() > 3) {
                temperatureValid = false;
            } else {
                int checktemperature = 0;
                if ("Celsius".equals(temperatureUnit)) {
                    checktemperature = Integer.parseInt(temperature);
                } else if ("Fahrenheit".equals(temperatureUnit)) {
                    checktemperature = TemperatureConversation.fahrenheitToCelsius(Integer.parseInt(temperature));
                }
                if (checktemperature > 100 || checktemperature < 0) {
                    temperatureValid = false;
                }
            }
        }
        return temperatureValid;
    }
}
