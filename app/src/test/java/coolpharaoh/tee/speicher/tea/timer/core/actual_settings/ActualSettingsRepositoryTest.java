package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

// Could be removed after the successful migration (In half a year 1.6.2022)
@RunWith(MockitoJUnitRunner.class)
public class ActualSettingsRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    ActualSettingsDao actualSettingsDao;

    private ActualSettingsRepository actualSettingsRepository;

    @Before
    public void setUp() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);

        actualSettingsRepository = new ActualSettingsRepository(null);
    }

    @Test
    public void insertSettings() {
        ActualSettings actualSettings = new ActualSettings();

        actualSettingsRepository.insertSettings(actualSettings);

        verify(actualSettingsDao).insert(actualSettings);
    }

    @Test
    public void updateSettings() {
        ActualSettings actualSettings = new ActualSettings();

        actualSettingsRepository.updateSettings(actualSettings);

        verify(actualSettingsDao).update(actualSettings);
    }

    @Test
    public void getCounters() {
        ActualSettings mockedSettings = new ActualSettings();
        when(actualSettingsDao.getSettings()).thenReturn(mockedSettings);

        ActualSettings settings = actualSettingsRepository.getSettings();

        assertThat(settings).isEqualTo(mockedSettings);
    }

    @Test
    public void getCountItems() {
        int size = 1;
        when(actualSettingsDao.getCountItems()).thenReturn(size);

        int countItems = actualSettingsRepository.getCountItems();

        assertThat(countItems).isEqualTo(size);
    }
}
