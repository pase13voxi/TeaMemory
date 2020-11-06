//package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;
//
//import android.content.Context;
//import android.os.CountDownTimer;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.powermock.api.mockito.PowerMockito.when;
//import static org.powermock.api.mockito.PowerMockito.whenNew;
//
//// Test as much as possible. Find out how to test CountDownTimer???
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(value = CountDownTimer.class, fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.views.showtea.*")
//public class ForegroundTimerTest {
//    @Mock
//    Context context;
//    @Mock
//    SharedTimerPreferences sharedTimerPreferences;
//    @Mock
//    BackgroundTimer backgroundTimer;
//
//    @Before
//    public void setUp() throws Exception {
//        whenNew(SharedTimerPreferences.class).withAnyArguments().thenReturn(sharedTimerPreferences);
//        whenNew(BackgroundTimer.class).withAnyArguments().thenReturn(backgroundTimer);
//    }
//
//    @Test
//    public void startForegroundTimer() {
//        ForegroundTimer foregroundTimer = new ForegroundTimer(context);
//        assertThat(foregroundTimer).isNotNull();
//
//        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);
//
//        foregroundTimer.startForegroundTimer(60000L, 1L);
//
//        //mock getNow -> and test here
//        verify(sharedTimerPreferences).setStartedTime(anyLong());
//    }
//
//    @Test
//    public void reset() {
//        ForegroundTimer foregroundTimer = new ForegroundTimer(context);
//        assertThat(foregroundTimer).isNotNull();
//
//        foregroundTimer.reset();
//
//        verify(sharedTimerPreferences).setStartedTime(0);
//        verify(backgroundTimer).removeAlarmManager();
//        verify(context).stopService(any());
//    }
//
//    @Test
//    public void startBackgroundTimer() {
//        ForegroundTimer foregroundTimer = new ForegroundTimer(context);
//        assertThat(foregroundTimer).isNotNull();
//
//        foregroundTimer.startBackgroundTimer();
//    }
//
//    @Test
//    public void resumeForegroundTimer() {
//        ForegroundTimer foregroundTimer = new ForegroundTimer(context);
//        assertThat(foregroundTimer).isNotNull();
//
//        when(sharedTimerPreferences.getStartedTime()).thenReturn(0L);
//
//        foregroundTimer.resumeForegroundTimer();
//
//    }
//}
