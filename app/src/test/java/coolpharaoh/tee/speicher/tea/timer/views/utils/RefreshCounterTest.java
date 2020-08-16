package coolpharaoh.tee.speicher.tea.timer.views.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;

import static org.assertj.core.api.Assertions.assertThat;

public class RefreshCounterTest {
    public static final int DAY = 1;
    public static final int WEEK = 2;
    public static final int MONTH = 3;
    public static final long OVERALL = 4L;
    Date today;
    Date dayBefore;
    Date weekBefore;
    Date monthBefore;

    @Before
    public void setUp() {
        Instant now = Instant.now();
        today = Date.from(now);
        dayBefore = Date.from(now.minus(Duration.ofDays(1)));
        weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        monthBefore = Date.from(now.minus(Duration.ofDays(31)));
    }

    @Test
    public void refreshCounterNothingChanged() {
        Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, today, today, today);

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
        Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, dayBefore, dayBefore, dayBefore);

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
        Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, weekBefore, weekBefore, weekBefore);

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
        Counter counter = new Counter(0, DAY, WEEK, MONTH, OVERALL, monthBefore, monthBefore, monthBefore);

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
        Counter counter1 = new Counter(0, DAY, WEEK, MONTH, OVERALL, today, today, today);
        Counter counter2 = new Counter(0, DAY, WEEK, MONTH, OVERALL, monthBefore, monthBefore, monthBefore);

        List<Counter> counters = Arrays.asList(counter1, counter2);

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
