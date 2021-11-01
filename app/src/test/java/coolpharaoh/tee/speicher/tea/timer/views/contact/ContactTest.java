package coolpharaoh.tee.speicher.tea.timer.views.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

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

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class ContactTest {

    public static final String EXTRA_INTENT = "android.intent.extra.INTENT";
    public static final String INTENT_ACTION_CHOOSER = "android.intent.action.CHOOSER";
    public static final String INTENT_ACTION_SENDTO = "android.intent.action.SENDTO";
    private ActivityScenario<Contact> contactActivityScenario;

    @Before
    public void setUp() {
        contactActivityScenario = ActivityScenario.launch(Contact.class);
    }

    @Test
    public void launchActivity() {
        contactActivityScenario.onActivity(contact -> {
            final Button buttonEmail = contact.findViewById(R.id.button_contact_send_email);

            buttonEmail.performClick();

            final Intent actual = shadowOf((Application) ApplicationProvider.getApplicationContext()).getNextStartedActivity();

            assertThat(actual.getAction()).isEqualTo(INTENT_ACTION_CHOOSER);

            final Intent emailIntent = (Intent) actual.getExtras().get(EXTRA_INTENT);
            assertThat(emailIntent.getAction()).isEqualTo(INTENT_ACTION_SENDTO);

            final String mailTo = "mailto:" + contact.getString(R.string.contact_email_address).replace("@", "%40");
            assertThat(emailIntent.getData()).hasToString(mailTo);
        });
    }
}
