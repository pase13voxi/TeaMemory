package coolpharaoh.tee.speicher.tea.timer.core.counter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.core.*")
public class CounterRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    CounterDao counterDao;

    private CounterRepository counterRepository;

    @Before
    public void setUp() {
        mockCounterDao();
        counterRepository = new CounterRepository(null);
    }

    private void mockCounterDao() {
        initMocks(TeaMemoryDatabase.class);
        mockStatic(TeaMemoryDatabase.class);
        when(TeaMemoryDatabase.getDatabaseInstance(any())).thenReturn(teaMemoryDatabase);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
    }

    @Test
    public void insertCounter() {
        Counter counter = new Counter();

        counterRepository.insertCounter(counter);

        verify(counterDao).insert(counter);
    }

    @Test
    public void updateCounter() {
        Counter counter = new Counter();

        counterRepository.updateCounter(counter);

        verify(counterDao).update(counter);
    }

    @Test
    public void getCounters() {
        when(counterDao.getCounters()).thenReturn(Arrays.asList(new Counter(), new Counter()));

        List<Counter> counters = counterRepository.getCounters();

        verify(counterDao).getCounters();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getCounterByTeaId() {
        int teaId = 2;
        Counter counter = new Counter();
        when(counterDao.getCounterByTeaId(teaId)).thenReturn(counter);

        Counter counterByTeaId = counterRepository.getCounterByTeaId(teaId);

        assertThat(counterByTeaId).isEqualTo(counter);
    }

    @Test
    public void getTeaCounterOverall() {
        when(counterDao.getTeaCounterOverall()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        List<StatisticsPOJO> counters = counterRepository.getTeaCounterOverall();

        verify(counterDao).getTeaCounterOverall();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterMonth() {
        when(counterDao.getTeaCounterMonth()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        List<StatisticsPOJO> counters = counterRepository.getTeaCounterMonth();

        verify(counterDao).getTeaCounterMonth();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterWeek() {
        when(counterDao.getTeaCounterWeek()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        List<StatisticsPOJO> counters = counterRepository.getTeaCounterWeek();

        verify(counterDao).getTeaCounterWeek();
        assertThat(counters).hasSize(2);
    }

    @Test
    public void getTeaCounterDay() {
        when(counterDao.getTeaCounterDay()).thenReturn(Arrays.asList(new StatisticsPOJO(), new StatisticsPOJO()));

        List<StatisticsPOJO> counters = counterRepository.getTeaCounterDay();

        verify(counterDao).getTeaCounterDay();
        assertThat(counters).hasSize(2);
    }
}
