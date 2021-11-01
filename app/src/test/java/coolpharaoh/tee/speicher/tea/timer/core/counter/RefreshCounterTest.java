package coolpharaoh.tee.speicher.tea.timer.core.counter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;


@RunWith(MockitoJUnitRunner.class)
public class RefreshCounterTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    public static final int DAY = 1;
    public static final int WEEK = 2;
    public static final int MONTH = 3;
    public static final long OVERALL = 4L;

    Date today;
    Date dayBefore;
    Date weekBefore;
    Date monthBefore;

    @Mock
    DateUtility fixedDate;

    @Before
    public void setUp() {
        final Instant now = getFixedDate();

        today = Date.from(now);
        dayBefore = Date.from(now.minus(Duration.ofDays(1)));
        weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        monthBefore = Date.from(now.minus(Duration.ofDays(31)));

        when(fixedDate.getDate()).thenReturn(today);
        CurrentDate.setFixedDate(fixedDate);
    }

    private Instant getFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }

    @Test
    public void refreshCounterNothingChanged() {
        final Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, today, today, today);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        DAY,
                        WEEK,
                        MONTH,
                        OVERALL
                );
    }

    @Test
    public void refreshCounterDayRefreshed() {
        final Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, dayBefore, dayBefore, dayBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        WEEK,
                        MONTH,
                        OVERALL
                );
    }

    @Test
    public void refreshCounterDayWeekRefreshed() {
        final Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, weekBefore, weekBefore, weekBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        MONTH,
                        OVERALL
                );
    }

    @Test
    public void refreshCounterDayWeekMonthRefreshed() {
        final Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, monthBefore, monthBefore, monthBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        0,
                        OVERALL
                );
    }

    @Test
    public void refreshCounters() {
        final Counter counter1 = new Counter(0, DAY, WEEK, MONTH, OVERALL, today, today, today);
        final Counter counter2 = new Counter(0, DAY, WEEK, MONTH, OVERALL, monthBefore, monthBefore, monthBefore);

        final List<Counter> counters = Arrays.asList(counter1, counter2);

        RefreshCounter.refreshCounters(counters);

        assertThat(counter1)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        DAY,
                        WEEK,
                        MONTH,
                        OVERALL
                );

        assertThat(counter2)
                .extracting(
                        Counter::getDay,
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        0,
                        OVERALL
                );
    }
}
