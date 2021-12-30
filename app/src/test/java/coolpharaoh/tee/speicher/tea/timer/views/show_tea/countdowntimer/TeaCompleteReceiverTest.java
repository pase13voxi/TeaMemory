package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import android.app.Application;
import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TeaCompleteReceiverTest {
    public static final String TEA_ID = "teaId";
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Application application;


    @Test
    public void onReceiveAfterAndroidO() {
        final TeaCompleteReceiver teaCompleteReceiver = new TeaCompleteReceiver();

        final Intent intent = new Intent();
        intent.putExtra(TEA_ID, 1L);
        teaCompleteReceiver.onReceive(application, intent);

        final ArgumentCaptor<Intent> serviceCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(application).startForegroundService(serviceCaptor.capture());
        final Intent service = serviceCaptor.getValue();

        assertThat(service.getLongExtra(TEA_ID, 0L)).isEqualTo(1L);
    }
}
