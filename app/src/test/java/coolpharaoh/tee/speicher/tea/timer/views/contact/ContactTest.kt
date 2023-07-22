package coolpharaoh.tee.speicher.tea.timer.views.contact

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class ContactTest {
    private var contactActivityScenario: ActivityScenario<Contact>? = null
    @Before
    fun setUp() {
        contactActivityScenario = ActivityScenario.launch(Contact::class.java)
    }

    @Test
    fun launchActivity() {
        contactActivityScenario!!.onActivity { contact: Contact ->
            val buttonEmail = contact.findViewById<Button>(R.id.button_contact_send_email)
            buttonEmail.performClick()
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity
            Assertions.assertThat(actual.action).isEqualTo(INTENT_ACTION_CHOOSER)
            val emailIntent = actual.extras!![EXTRA_INTENT] as Intent?
            Assertions.assertThat(emailIntent!!.action).isEqualTo(INTENT_ACTION_SENDTO)
            val mailTo = "mailto:" + contact.getString(R.string.contact_email_address).replace("@", "%40")
            Assertions.assertThat(emailIntent.data).hasToString(mailTo)
        }
    }

    companion object {
        const val EXTRA_INTENT = "android.intent.extra.INTENT"
        const val INTENT_ACTION_CHOOSER = "android.intent.action.CHOOSER"
        const val INTENT_ACTION_SENDTO = "android.intent.action.SENDTO"
    }
}