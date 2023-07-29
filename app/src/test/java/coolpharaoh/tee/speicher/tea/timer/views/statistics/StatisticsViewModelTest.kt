package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;

@ExtendWith(MockitoExtension.class)
class StatisticsViewModelTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";

    private StatisticsViewModel statisticsViewModel;

    @Mock
    CounterRepository counterRepository;
    @Mock
    DateUtility fixedDate;

    @BeforeEach
    void setUp() {
        statisticsViewModel = new StatisticsViewModel(counterRepository);
    }

    @Test
    void getStatisticsOverall() {
        final List<StatisticsPOJO> counterOverallBefore = new ArrayList<>();

        final StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaName = "TEA1";
        statisticsPOJO1.teaColor = 15;
        statisticsPOJO1.counter = 15;
        counterOverallBefore.add(statisticsPOJO1);

        final StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaName = "TEA2";
        statisticsPOJO2.teaColor = 18;
        statisticsPOJO2.counter = 18;
        counterOverallBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterOverall()).thenReturn(counterOverallBefore);

        final List<StatisticsPOJO> counterOverallAfter = statisticsViewModel.getStatisticsOverall();

        assertThat(counterOverallAfter).isEqualTo(counterOverallBefore);
    }

    @Test
    void getStatisticsYear() {
        final List<StatisticsPOJO> counterYearBefore = new ArrayList<>();

        final StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaName = "TEA1";
        statisticsPOJO1.teaColor = 15;
        statisticsPOJO1.counter = 15;
        counterYearBefore.add(statisticsPOJO1);

        final StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaName = "TEA2";
        statisticsPOJO2.teaColor = 18;
        statisticsPOJO2.counter = 18;
        counterYearBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterYear()).thenReturn(counterYearBefore);

        final List<StatisticsPOJO> counterDayAfter = statisticsViewModel.getStatisticsYear();

        assertThat(counterDayAfter).isEqualTo(counterYearBefore);
    }

    @Test
    void getStatisticsMonth() {
        final List<StatisticsPOJO> counterMonthBefore = new ArrayList<>();

        final StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaName = "TEA1";
        statisticsPOJO1.teaColor = 15;
        statisticsPOJO1.counter = 15;
        counterMonthBefore.add(statisticsPOJO1);

        final StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaName = "TEA2";
        statisticsPOJO2.teaColor = 18;
        statisticsPOJO2.counter = 18;
        counterMonthBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterMonth()).thenReturn(counterMonthBefore);

        final List<StatisticsPOJO> counterMonthAfter = statisticsViewModel.getStatisticsMonth();

        assertThat(counterMonthAfter).isEqualTo(counterMonthBefore);
    }

    @Test
    void getStatisticsWeek() {
        final List<StatisticsPOJO> counterWeekBefore = new ArrayList<>();

        final StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaName = "TEA1";
        statisticsPOJO1.teaColor = 15;
        statisticsPOJO1.counter = 15;
        counterWeekBefore.add(statisticsPOJO1);

        final StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaName = "TEA2";
        statisticsPOJO2.teaColor = 18;
        statisticsPOJO2.counter = 18;
        counterWeekBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterWeek()).thenReturn(counterWeekBefore);

        final List<StatisticsPOJO> counterWeekAfter = statisticsViewModel.getStatisticsWeek();

        assertThat(counterWeekAfter).isEqualTo(counterWeekBefore);
    }

    @Test
    void refreshAllCounter() {
        final Instant now = getFixedDate();
        final Date today = Date.from(now);
        final Date weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        final Date monthBefore = Date.from(now.minus(Duration.ofDays(31)));
        final Date yearBefore = Date.from(now.minus(Duration.ofDays(370)));

        when(fixedDate.getDate()).thenReturn(today);
        CurrentDate.setFixedDate(fixedDate);

        final List<Counter> countersBefore = new ArrayList<>();

        final Counter noRefresh = new Counter(1L, 4, 7, 9, 15L, today, today, today);
        countersBefore.add(noRefresh);
        final Counter refreshWeek = new Counter(1L, 4, 7, 9, 15L, weekBefore, today, today);
        countersBefore.add(refreshWeek);
        final Counter refreshMonth = new Counter(1L, 4, 7, 9, 15L, today, monthBefore, today);
        countersBefore.add(refreshMonth);
        final Counter refreshDay = new Counter(1L, 4, 7, 9, 15L, today, today, yearBefore);
        countersBefore.add(refreshDay);
        final Counter refreshAll = new Counter(1L, 4, 7, 9, 15L, yearBefore, yearBefore, yearBefore);
        countersBefore.add(refreshAll);

        when(counterRepository.getCounters()).thenReturn(countersBefore);

        statisticsViewModel.refreshAllCounter();

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository, times(5)).updateCounter(captor.capture());

        final List<Counter> counterAfter = captor.getAllValues();

        assertThat(counterAfter.get(0)).isEqualTo(noRefresh);

        assertThat(counterAfter.get(1).getWeek()).isZero();
        assertThat(counterAfter.get(1).getMonth()).isEqualTo(7);
        assertThat(counterAfter.get(1).getYear()).isEqualTo(9);
        assertThat(counterAfter.get(1).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(2).getWeek()).isEqualTo(4);
        assertThat(counterAfter.get(2).getMonth()).isZero();
        assertThat(counterAfter.get(2).getYear()).isEqualTo(9);
        assertThat(counterAfter.get(2).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(3).getWeek()).isEqualTo(4);
        assertThat(counterAfter.get(3).getMonth()).isEqualTo(7);
        assertThat(counterAfter.get(3).getYear()).isZero();
        assertThat(counterAfter.get(3).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(4).getWeek()).isZero();
        assertThat(counterAfter.get(4).getMonth()).isZero();
        assertThat(counterAfter.get(4).getYear()).isZero();
        assertThat(counterAfter.get(4).getOverall()).isEqualTo(15L);
    }

    private Instant getFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }
}
