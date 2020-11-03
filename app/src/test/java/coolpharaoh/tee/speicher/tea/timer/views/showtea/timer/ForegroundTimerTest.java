package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.views.showtea.*")
public class ForegroundTimerTest {
    @Mock
    Context context;
    @Mock
    SharedTimerPreferences sharedTimerPreferences;
    @Mock
    BackgroundTimer backgroundTimer;

    @Before
    public void setUp() throws Exception {
        whenNew(SharedTimerPreferences.class).withAnyArguments().thenReturn(sharedTimerPreferences);
        whenNew(BackgroundTimer.class).withAnyArguments().thenReturn(backgroundTimer);
    }

    @Test
    public void startForegroundTimer() {
        ForegroundTimer foregroundTimer = new ForegroundTimer(context);
        assertThat(foregroundTimer).isNotNull();

        //Test is not ready
    }

}
