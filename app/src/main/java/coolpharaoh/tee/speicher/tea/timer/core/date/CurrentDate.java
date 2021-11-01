package coolpharaoh.tee.speicher.tea.timer.core.date;

import androidx.annotation.VisibleForTesting;

import java.util.Date;

public class CurrentDate {

    private CurrentDate() { }

    private static DateUtility dateUtility = new DateUtility();

    @VisibleForTesting
    public static void setFixedDate(final DateUtility fixedDate) {
        dateUtility = fixedDate;
    }

    public static Date getDate() {
        return dateUtility.getDate();
    }
}
