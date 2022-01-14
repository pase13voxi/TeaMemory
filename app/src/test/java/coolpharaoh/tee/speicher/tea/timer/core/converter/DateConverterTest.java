package coolpharaoh.tee.speicher.tea.timer.core.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter;

class DateConverterTest {
    public static final long LONG_DATE = 214234L;

    @Test
    void convertLongToDate() {
        final Date dateAfter = DateConverter.toDate(LONG_DATE);

        assertThat(dateAfter).isEqualTo(new Date(LONG_DATE));
    }

    @Test
    void convertNullToDateReturnNull() {
        final Date dateAfter = DateConverter.toDate(null);

        assertThat(dateAfter).isNull();
    }

    @Test
    void convertDateToLong() {
        final Date date = new Date(LONG_DATE);

        final Long longAfter = DateConverter.fromDate(date);

        assertThat(longAfter).isEqualTo(LONG_DATE);
    }

    @Test
    void convertNullToLongReturnNull() {
        final Long longAfter = DateConverter.fromDate(null);

        assertThat(longAfter).isNull();
    }
}
