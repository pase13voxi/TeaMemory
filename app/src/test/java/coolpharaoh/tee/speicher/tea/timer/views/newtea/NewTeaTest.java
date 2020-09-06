package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.os.Build;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionDao;
import coolpharaoh.tee.speicher.tea.timer.core.note.NoteDao;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class NewTeaTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TeaMemoryDatabase teaMemoryDatabase;
    @Mock
    TeaDao teaDao;
    @Mock
    InfusionDao infusionDao;
    @Mock
    NoteDao noteDao;
    @Mock
    CounterDao counterDao;
    @Mock
    ActualSettingsDao actualSettingsDao;

    @Before
    public void setUp() {
        mockDB();
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setTemperatureUnit("Celsius");
        when(actualSettingsDao.getSettings()).thenReturn(actualSettings);
    }

    private void mockDB() {
        TeaMemoryDatabase.setMockedDatabase(teaMemoryDatabase);
        when(teaMemoryDatabase.getTeaDao()).thenReturn(teaDao);
        when(teaMemoryDatabase.getInfusionDao()).thenReturn(infusionDao);
        when(teaMemoryDatabase.getNoteDao()).thenReturn(noteDao);
        when(teaMemoryDatabase.getCounterDao()).thenReturn(counterDao);
        when(teaMemoryDatabase.getActualSettingsDao()).thenReturn(actualSettingsDao);
    }

    @Test
    public void launchActivityAndAddNewTea() {
        ActivityScenario<NewTea> newTeaActivityScenario = ActivityScenario.launch(NewTea.class);
        newTeaActivityScenario.onActivity(newTea -> {
            EditText name = newTea.findViewById(R.id.editTextName);
            EditText amount = newTea.findViewById(R.id.editTextAmount);
            EditText temperature = newTea.findViewById(R.id.editTextTemperature);
            EditText time = newTea.findViewById(R.id.editTextTime);
            EditText coolDownTime = newTea.findViewById(R.id.editTextCoolDownTime);

            name.setText("Name");
            amount.setText("15");
            temperature.setText("100");
            time.setText("2:30");
            coolDownTime.setText("5:00");

            newTea.onOptionsItemSelected(new RoboMenuItem(R.id.action_done));

            verify(teaDao).insert(any());
            verify(infusionDao).insert(any());
            verify(counterDao).insert(any());
            verify(noteDao).insert(any());
        });
    }
}
