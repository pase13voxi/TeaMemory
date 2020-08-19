package coolpharaoh.tee.speicher.tea.timer.views.utils.date;

import java.util.Date;

public class CurrentDate {

    private CurrentDate() {
    }

    private static DateUtility dateUtility = new DateUtility();

    public static void setFixedDate(DateUtility fixedDate) {
        dateUtility = fixedDate;
    }

    public static Date getDate() {
        return dateUtility.getDate();
    }
}
