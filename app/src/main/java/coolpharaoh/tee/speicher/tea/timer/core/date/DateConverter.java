package coolpharaoh.tee.speicher.tea.timer.core.date;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    private DateConverter() {
    }

    @TypeConverter
    public static Date toDate(final Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(final Date date) {
        return date == null ? null : date.getTime();
    }
}