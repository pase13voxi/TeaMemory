package coolpharaoh.tee.speicher.tea.timer.core.counter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;

@ExtendWith(MockitoExtension.class)
class RefreshCounterTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;
    public static final long OVERALL = 4L;

    Date weekBefore;
    Date monthBefore;
    Date yearBefore;
    Date today;

    @Mock
    DateUtility fixedDate;

    @BeforeEach
    void setUp() {
        final Instant now = getFixedDate();

        today = Date.from(now);
        weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        monthBefore = Date.from(now.minus(Duration.ofDays(31)));
        yearBefore = Date.from(now.minus(Duration.ofDays(370)));

        when(fixedDate.getDate()).thenReturn(today);
        CurrentDate.setFixedDate(fixedDate);
    }

    private Instant getFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }

    @Test
    void resetIncompleteCounter() {
        final Counter counter = new Counter(0, WEEK, MONTH, YEAR, OVERALL, null, today, today);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall,
                        Counter::getWeekDate,
                        Counter::getMonthDate,
                        Counter::getYearDate)
                .containsExactly(
                        0,
                        0,
                        0,
                        0L,
                        today,
                        today,
                        today
                );
    }

    @Test
    void refreshCounterNothingChanged() {
        final Counter counter = new Counter(0, WEEK, MONTH, YEAR, OVERALL, today, today, today);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        WEEK,
                        MONTH,
                        YEAR,
                        OVERALL
                );
    }

    @Test
    void refreshCounterDayWeekRefreshed() {
        final Counter counter = new Counter(0, WEEK, MONTH, YEAR, OVERALL, weekBefore, weekBefore, weekBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        MONTH,
                        YEAR,
                        OVERALL
                );
    }

    @Test
    void refreshCounterDayWeekMonthRefreshed() {
        final Counter counter = new Counter(0, WEEK, MONTH, YEAR, OVERALL, monthBefore, monthBefore, monthBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        YEAR,
                        OVERALL
                );
    }

    @Test
    void refreshCounterYearRefreshed() {
        final Counter counter = new Counter(0, WEEK, MONTH, YEAR, OVERALL, yearBefore, yearBefore, yearBefore);

        RefreshCounter.refreshCounter(counter);

        assertThat(counter)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        0,
                        OVERALL
                );
    }

    @Test
    void refreshCounters() {
        final Counter counter1 = new Counter(0, WEEK, MONTH, YEAR, OVERALL, today, today, today);
        final Counter counter2 = new Counter(0, WEEK, MONTH, YEAR, OVERALL, yearBefore, yearBefore, yearBefore);

        final List<Counter> counters = Arrays.asList(counter1, counter2);

        RefreshCounter.refreshCounters(counters);

        assertThat(counter1)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        WEEK,
                        MONTH,
                        YEAR,
                        OVERALL
                );

        assertThat(counter2)
                .extracting(
                        Counter::getWeek,
                        Counter::getMonth,
                        Counter::getYear,
                        Counter::getOverall)
                .containsExactly(
                        0,
                        0,
                        0,
                        OVERALL
                );
    }
}