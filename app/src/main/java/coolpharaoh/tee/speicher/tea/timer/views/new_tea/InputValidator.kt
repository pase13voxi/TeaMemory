package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer

class InputValidator internal constructor(private val application: Application, private val printer: Printer) {
    fun nameIsNotEmpty(nameInput: String): Boolean {
        if ("" == nameInput) {
            printer.print(application.getString(R.string.new_tea_error_no_name))
            return false
        }
        return true
    }

    fun nameIsValid(nameInput: String): Boolean {
        if (nameInput.length > 300) {
            printer.print(application.getString(R.string.new_tea_error_name))
            return false
        }
        return true
    }

    fun varietyIsValid(varietyInput: String): Boolean {
        if (varietyInput.length > 30) {
            printer.print(application.getString(R.string.new_tea_error_30Char))
            return false
        }
        return true
    }
}