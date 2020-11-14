package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class TeaCompleteReceiverTest {
    public static final String TEA_ID = "teaId";
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Application application;
    @Mock
    SystemUtility systemUtility;


    @Test
    public void onReceiveAfterAndroidO() {
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.O_MR1);
        TeaCompleteReceiver teaCompleteReceiver = new TeaCompleteReceiver();

        Intent intent = new Intent();
        intent.putExtra(TEA_ID, 1L);
        teaCompleteReceiver.onReceive(application, intent);

        ArgumentCaptor<Intent> serviceCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(application).startForegroundService(serviceCaptor.capture());
        Intent service = serviceCaptor.getValue();

        assertThat(service.getLongExtra(TEA_ID, 0L)).isEqualTo(1L);
    }

    @Test
    public void onReceiveBeforeAndroidO() {
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.N_MR1);
        TeaCompleteReceiver teaCompleteReceiver = new TeaCompleteReceiver();

        Intent intent = new Intent();
        intent.putExtra(TEA_ID, 1L);
        teaCompleteReceiver.onReceive(application, intent);


        ArgumentCaptor<Intent> serviceCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(application).startService(serviceCaptor.capture());
        Intent service = serviceCaptor.getValue();

        assertThat(service.getLongExtra(TEA_ID, 0L)).isEqualTo(1L);
    }
}
