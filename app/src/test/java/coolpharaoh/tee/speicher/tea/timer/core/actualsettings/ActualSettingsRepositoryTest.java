package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActualSettingsRepositoryTest {

    @Mock
    ActualSettingsDao actualSettingsDao;

    private ActualSettingsRepository actualSettingsRepository;

    @Before
    public void setUp() {
        actualSettingsRepository = new ActualSettingsRepository(actualSettingsDao);
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
