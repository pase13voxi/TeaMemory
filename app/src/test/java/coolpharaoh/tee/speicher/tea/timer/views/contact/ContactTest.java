package coolpharaoh.tee.speicher.tea.timer.views.contact;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ContactTest {

    private ActivityScenario<Contact> contactActivityScenario;

    @Before
    public void setUp() {
        contactActivityScenario = ActivityScenario.launch(Contact.class);
    }

    @Test
    public void launchActivity() {
        contactActivityScenario.onActivity(contact -> {
            Button buttonEmail = contact.findViewById(R.id.buttonSendEmail);

            buttonEmail.performClick();

            Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getAction()).isEqualTo("android.intent.action.CHOOSER");

            Intent emailIntent = (Intent) actual.getExtras().get("android.intent.extra.INTENT");

            assertThat(emailIntent.getAction()).isEqualTo("android.intent.action.SENDTO");
            assertThat(emailIntent.getData().toString()).isEqualTo("mailto:pase.b%40outlook.com");
        });
    }
}
