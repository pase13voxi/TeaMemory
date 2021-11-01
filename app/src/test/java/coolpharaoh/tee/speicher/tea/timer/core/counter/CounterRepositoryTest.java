package coolpharaoh.tee.speicher.tea.timer.core.counter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;


@RunWith(MockitoJUnitRunner.class)
public class CounterRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    CounterDao counterDao;

    private CounterRepository counterRepository;

    @Before
    public void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);

        counterRepository = new CounterRepository(null);
    }

    @Test
    public void insertCounter() {
        final Counter counter = new Counter();

        counterRepository.insertCounter(counter);

        verify(counterDao).insert(counter);
    }

    @Test
    public void updateCounter() {
        final Counter counter = new Counter();

        counterRepository.updateCounter(counter);

        verify(counterDao).update(counter);
    }

    @Test
    public void getCounters() {
        when(counterDao.getCounters()).thenReturn(Arrays.asList(new Counter(), new Counter()));

        final List<Counter> counters = counterRepository.getCounters();

        verify(counterDao).getCounters();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getCounterByTeaId() {
        final int teaId = 2;
        final Counter counter = new Counter();
        when(counterDao.getCounterByTeaId(teaId)).thenReturn(counter);

        final Counter counterByTeaId = counterRepository.getCounterByTeaId(teaId);

        assertThat(counterByTeaId).isEqualTo(counter);
    }

    @Test
    public void getTeaCounterOverall() {
        when(counterDao.getTeaCounterOverall()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        final List<StatisticsPOJO> counters = counterRepository.getTeaCounterOverall();

        verify(counterDao).getTeaCounterOverall();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterMonth() {
        when(counterDao.getTeaCounterMonth()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        final List<StatisticsPOJO> counters = counterRepository.getTeaCounterMonth();

        verify(counterDao).getTeaCounterMonth();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterWeek() {
        when(counterDao.getTeaCounterWeek()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        final List<StatisticsPOJO> counters = counterRepository.getTeaCounterWeek();

        verify(counterDao).getTeaCounterWeek();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterDay() {
        when(counterDao.getTeaCounterDay()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        final List<StatisticsPOJO> counters = counterRepository.getTeaCounterDay();

        verify(counterDao).getTeaCounterDay();
        assertThat(counters).hasSize(2);
    }
}
