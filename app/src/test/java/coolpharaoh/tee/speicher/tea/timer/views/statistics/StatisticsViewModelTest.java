package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class StatisticsViewModelTest {
    public static final String CURRENT_DATE = "2020-08-19T10:15:30Z";

    private StatisticsViewModel statisticsViewModel;

    @Mock
    CounterRepository counterRepository;
    @Mock
    DateUtility fixedDate;

    @Before
    public void setUp() {
        statisticsViewModel = new StatisticsViewModel(counterRepository);
    }

    @Test
    public void getStatisticsOverall() {
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
    public void getStatisticsMonth() {
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
    public void getStatisticsWeek() {
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
    public void getStatisticsDay() {
        final List<StatisticsPOJO> counterDayBefore = new ArrayList<>();

        final StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaName = "TEA1";
        statisticsPOJO1.teaColor = 15;
        statisticsPOJO1.counter = 15;
        counterDayBefore.add(statisticsPOJO1);

        final StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaName = "TEA2";
        statisticsPOJO2.teaColor = 18;
        statisticsPOJO2.counter = 18;
        counterDayBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterDay()).thenReturn(counterDayBefore);

        final List<StatisticsPOJO> counterDayAfter = statisticsViewModel.getStatisticsDay();

        assertThat(counterDayAfter).isEqualTo(counterDayBefore);
    }

    @Test
    public void refreshAllCounter() {
        final Instant now = getFixedDate();
        final Date today = Date.from(now);
        final Date dayBefore = Date.from(now.minus(Duration.ofDays(1)));
        final Date weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        final Date monthBefore = Date.from(now.minus(Duration.ofDays(31)));

        when(fixedDate.getDate()).thenReturn(today);
        CurrentDate.setFixedDate(fixedDate);

        final List<Counter> countersBefore = new ArrayList<>();

        final Counter noRefresh = new Counter(1L, 4, 7, 9, 15L, today, today, today);
        countersBefore.add(noRefresh);
        final Counter refreshDay = new Counter(1L, 4, 7, 9, 15L, dayBefore, today, today);
        countersBefore.add(refreshDay);
        final Counter refreshWeek = new Counter(1L, 4, 7, 9, 15L, today, weekBefore, today);
        countersBefore.add(refreshWeek);
        final Counter refreshMonth = new Counter(1L, 4, 7, 9, 15L, today, today, monthBefore);
        countersBefore.add(refreshMonth);
        final Counter refreshAll = new Counter(1L, 4, 7, 9, 15L, monthBefore, monthBefore, monthBefore);
        countersBefore.add(refreshAll);

        when(counterRepository.getCounters()).thenReturn(countersBefore);

        statisticsViewModel.refreshAllCounter();

        final ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository, times(5)).updateCounter(captor.capture());

        final List<Counter> counterAfter = captor.getAllValues();

        assertThat(counterAfter.get(0)).isEqualTo(noRefresh);

        assertThat(counterAfter.get(1).getDay()).isZero();
        assertThat(counterAfter.get(1).getWeek()).isEqualTo(7);
        assertThat(counterAfter.get(1).getMonth()).isEqualTo(9);
        assertThat(counterAfter.get(1).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(2).getDay()).isEqualTo(4);
        assertThat(counterAfter.get(2).getWeek()).isZero();
        assertThat(counterAfter.get(2).getMonth()).isEqualTo(9);
        assertThat(counterAfter.get(2).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(3).getDay()).isEqualTo(4);
        assertThat(counterAfter.get(3).getWeek()).isEqualTo(7);
        assertThat(counterAfter.get(3).getMonth()).isZero();
        assertThat(counterAfter.get(3).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(4).getDay()).isZero();
        assertThat(counterAfter.get(4).getWeek()).isZero();
        assertThat(counterAfter.get(4).getMonth()).isZero();
        assertThat(counterAfter.get(4).getOverall()).isEqualTo(15L);
    }

    private Instant getFixedDate() {
        final Clock clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"));
        return Instant.now(clock);
    }
}
