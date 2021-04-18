package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.R;
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
            printer.print(application.getString(R.string.new_tea_error_no_name));
            return false;
        }
        return true;
    }

    boolean nameIsValid(String nameInput) {
        if (nameInput.length() > 300) {
            printer.print(application.getString(R.string.new_tea_error_name));
            return false;
        }
        return true;
    }

    boolean varietyIsValid(String varietyInput) {
        if (varietyInput.length() > 30) {
            printer.print(application.getString(R.string.new_tea_error_30Char));
            return false;
        }
        return true;
    }
}
