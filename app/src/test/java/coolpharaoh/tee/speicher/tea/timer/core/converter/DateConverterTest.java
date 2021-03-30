package coolpharaoh.tee.speicher.tea.timer.core.converter;

import org.junit.Test;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class DateConverterTest {
    public static final long LONG_DATE = 214234L;

    @Test
    public void convertLongToDate() {
        Date dateAfter = DateConverter.toDate(LONG_DATE);

        assertThat(dateAfter).isEqualTo(new Date(LONG_DATE));
    }

    @Test
    public void convertNullToDateReturnNull() {
        Date dateAfter = DateConverter.toDate(null);

        assertThat(dateAfter).isNull();
    }

    @Test
    public void convertDateToLong() {
        Date date = new Date(LONG_DATE);

        Long longAfter = DateConverter.fromDate(date);

        assertThat(longAfter).isEqualTo(LONG_DATE);
    }

    @Test
    public void convertNullToLongReturnNull() {
        Long longAfter = DateConverter.fromDate(null);

        assertThat(longAfter).isNull();
    }
}
