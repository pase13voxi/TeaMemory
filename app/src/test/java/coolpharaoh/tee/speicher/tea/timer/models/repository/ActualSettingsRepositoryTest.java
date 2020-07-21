package coolpharaoh.tee.speicher.tea.timer.models.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.models.*")
public class ActualSettingsRepositoryTest {
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    ActualSettingsDao actualSettingsDao;

    private ActualSettingsRepository actualSettingsRepository;

    @Before
    public void setUp() {
        mockCounterDao();
        actualSettingsRepository = new ActualSettingsRepository(null);
    }

    private void mockCounterDao() {
        initMocks(TeaMemoryDatabase.class);
        mockStatic(TeaMemoryDatabase.class);
        when(TeaMemoryDatabase.getDatabaseInstance(any())).thenReturn(teaMemoryDatabase);
        when(teaMemoryDatabase.getActualSettingsDAO()).thenReturn(actualSettingsDao);
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
