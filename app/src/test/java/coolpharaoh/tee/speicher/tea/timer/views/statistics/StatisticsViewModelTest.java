package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.*")
public class StatisticsViewModelTest {
    private StatisticsViewModel statisticsViewModel;

    @Mock
    CounterRepository counterRepository;

    @Mock
    Application application;

    @Before
    public void setUp() throws Exception {
        whenNew(CounterRepository.class).withAnyArguments().thenReturn(counterRepository);

        statisticsViewModel = new StatisticsViewModel(application);
    }

    @Test
    public void getStatisticsOverall() {
        List<StatisticsPOJO> counterOverallBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterOverallBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterOverallBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterOverall()).thenReturn(counterOverallBefore);

        List<StatisticsPOJO> counterOverallAfter = statisticsViewModel.getStatisticsOverall();

        assertThat(counterOverallAfter).isEqualTo(counterOverallBefore);
    }

    @Test
    public void getStatisticsMonth() {
        List<StatisticsPOJO> counterMonthBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterMonthBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterMonthBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterMonth()).thenReturn(counterMonthBefore);

        List<StatisticsPOJO> counterMonthAfter = statisticsViewModel.getStatisticsMonth();

        assertThat(counterMonthAfter).isEqualTo(counterMonthBefore);
    }

    @Test
    public void getStatisticsWeek() {
        List<StatisticsPOJO> counterWeekBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterWeekBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterWeekBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterWeek()).thenReturn(counterWeekBefore);

        List<StatisticsPOJO> counterWeekAfter = statisticsViewModel.getStatisticsWeek();

        assertThat(counterWeekAfter).isEqualTo(counterWeekBefore);
    }

    @Test
    public void getStatisticsDay() {
        List<StatisticsPOJO> counterDayBefore = new ArrayList<>();

        StatisticsPOJO statisticsPOJO1 = new StatisticsPOJO();
        statisticsPOJO1.teaname = "TEA1";
        statisticsPOJO1.teacolor = 15;
        statisticsPOJO1.counter = 15;
        counterDayBefore.add(statisticsPOJO1);

        StatisticsPOJO statisticsPOJO2 = new StatisticsPOJO();
        statisticsPOJO2.teaname = "TEA2";
        statisticsPOJO2.teacolor = 18;
        statisticsPOJO2.counter = 18;
        counterDayBefore.add(statisticsPOJO2);

        when(counterRepository.getTeaCounterDay()).thenReturn(counterDayBefore);

        List<StatisticsPOJO> counterDayAfter = statisticsViewModel.getStatisticsDay();

        assertThat(counterDayAfter).isEqualTo(counterDayBefore);
    }

    @Test
    public void refreshAllCounter() {
        Instant now = Instant.now();
        Date today = Date.from(now);
        Date dayBefore = Date.from(now.minus(Duration.ofDays(1)));
        Date weekBefore = Date.from(now.minus(Duration.ofDays(7)));
        Date monthBefore = Date.from(now.minus(Duration.ofDays(31)));

        List<Counter> countersBefore = new ArrayList<>();

        Counter noRefresh = new Counter(1L, 4, 7, 9, 15L, today, today, today);
        countersBefore.add(noRefresh);
        Counter refreshDay = new Counter(1L, 4, 7, 9, 15L, dayBefore, today, today);
        countersBefore.add(refreshDay);
        Counter refreshWeek = new Counter(1L, 4, 7, 9, 15L, today, weekBefore, today);
        countersBefore.add(refreshWeek);
        Counter refreshMonth = new Counter(1L, 4, 7, 9, 15L, today, today,monthBefore);
        countersBefore.add(refreshMonth);
        Counter refreshAll = new Counter(1L, 4, 7, 9, 15L, monthBefore, monthBefore, monthBefore);
        countersBefore.add(refreshAll);

        when(counterRepository.getCounters()).thenReturn(countersBefore);

        statisticsViewModel.refreshAllCounter();

        ArgumentCaptor<Counter> captor = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository, times(5)).updateCounter(captor.capture());

        List<Counter> counterAfter = captor.getAllValues();

        for (Counter counter : counterAfter) {
            System.out.println("Day: " + counter.getDay() + " ; Week: " + counter.getWeek() + " ; Month: " + counter.getMonth() + " ; Overall: " + counter.getOverall());
        }

        assertThat(counterAfter.get(0)).isEqualTo(noRefresh);

        assertThat(counterAfter.get(1).getDay()).isEqualTo(0);
        assertThat(counterAfter.get(1).getWeek()).isEqualTo(7);
        assertThat(counterAfter.get(1).getMonth()).isEqualTo(9);
        assertThat(counterAfter.get(1).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(2).getDay()).isEqualTo(4);
        assertThat(counterAfter.get(2).getWeek()).isEqualTo(0);
        assertThat(counterAfter.get(2).getMonth()).isEqualTo(9);
        assertThat(counterAfter.get(2).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(3).getDay()).isEqualTo(4);
        assertThat(counterAfter.get(3).getWeek()).isEqualTo(7);
        assertThat(counterAfter.get(3).getMonth()).isEqualTo(0);
        assertThat(counterAfter.get(3).getOverall()).isEqualTo(15L);

        assertThat(counterAfter.get(4).getDay()).isEqualTo(0);
        assertThat(counterAfter.get(4).getWeek()).isEqualTo(0);
        assertThat(counterAfter.get(4).getMonth()).isEqualTo(0);
        assertThat(counterAfter.get(4).getOverall()).isEqualTo(15L);
    }
}
