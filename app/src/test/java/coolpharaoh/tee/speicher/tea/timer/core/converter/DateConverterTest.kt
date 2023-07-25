package coolpharaoh.tee.speicher.tea.timer.core.converter

import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter.fromDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter.toDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Date

internal class DateConverterTest {
    @Test
    fun convertLongToDate() {
        val dateAfter = toDate(LONG_DATE)

        assertThat(dateAfter).isEqualTo(Date(LONG_DATE))
    }

    @Test
    fun convertNullToDateReturnNull() {
        val dateAfter = toDate(null)

        assertThat(dateAfter).isNull()
    }

    @Test
    fun convertDateToLong() {
        val date = Date(LONG_DATE)

        val longAfter = fromDate(date)

        assertThat(longAfter).isEqualTo(LONG_DATE)
    }

    @Test
    fun convertNullToLongReturnNull() {
        val longAfter = fromDate(null)

        assertThat(longAfter).isNull()
    }

    companion object {
        const val LONG_DATE = 214234L
    }
}