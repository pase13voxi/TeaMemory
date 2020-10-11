package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Application;

import java.util.regex.Pattern;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation;
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer;

public class InputValidator {
    private final Application application;
    private final Printer printer;

    InputValidator(Application application, Printer printer) {
        this.application = application;
        this.printer = printer;
    }

    boolean nameIsNotEmpty(String nameInput) {
        if ("".equals(nameInput)) {
            printer.print(application.getString(R.string.newtea_error_no_name));
            return false;
        }
        return true;
    }

    boolean nameIsValid(String nameInput) {
        if (nameInput.length() > 300) {
            printer.print(application.getString(R.string.newtea_error_name));
            return false;
        }
        return true;
    }

    boolean varietyIsValid(String varietyInput) {
        if (varietyInput.length() > 30) {
            printer.print(application.getString(R.string.newtea_error_30Char));
            return false;
        }
        return true;
    }

    boolean amountIsValid(String amountInput) {
        if ("".equals(amountInput)) {
            return true;
        } else if (amountInput.contains(".") || amountInput.length() > 3) {
            printer.print(application.getString(R.string.newtea_error_amount));
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
            printer.print(application.getString(R.string.newtea_error_cooldown_time));
            return false;
        }
        return true;
    }

    private boolean steepingTimeIsValid(String timeInput) {
        if (!timeIsValid(timeInput)) {
            printer.print(application.getString(R.string.newtea_error_time_format));
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
                printer.print(application.getString(R.string.newtea_error_wrong_celsius));
            } else if ("Fahrenheit".equals(temperatureUnit)) {
                printer.print(application.getString(R.string.newtea_error_wrong_fahrenheit));
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
