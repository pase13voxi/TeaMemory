package coolpharaoh.tee.speicher.tea.timer.core.date

import java.util.Calendar
import java.util.Date

class DateUtility {
    val date: Date
        get() = Calendar.getInstance().time
}