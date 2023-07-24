package coolpharaoh.tee.speicher.tea.timer.core.date

import androidx.annotation.VisibleForTesting
import java.util.Date

object CurrentDate {
    private var dateUtility = DateUtility()

    @JvmStatic
    @VisibleForTesting
    fun setFixedDate(fixedDate: DateUtility) {
        dateUtility = fixedDate
    }

    @JvmStatic
    fun getDate(): Date? {
        return dateUtility.getDate()
    }
}