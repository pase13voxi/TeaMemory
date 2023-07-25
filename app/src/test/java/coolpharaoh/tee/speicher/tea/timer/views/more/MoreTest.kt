package coolpharaoh.tee.speicher.tea.timer.views.more

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import coolpharaoh.tee.speicher.tea.timer.BuildConfig
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.contact.Contact
import coolpharaoh.tee.speicher.tea.timer.views.export_import.ExportImport
import coolpharaoh.tee.speicher.tea.timer.views.software.Software
import coolpharaoh.tee.speicher.tea.timer.views.statistics.Statistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class MoreTest {

    private var moreActivityScenario: ActivityScenario<More>? = null

    @Before
    fun setUp() {
        moreActivityScenario = ActivityScenario.launch(More::class.java)
    }

    @Test
    fun expectMoreListAndVersion() {
        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            assertThat(moreRecyclerView.adapter!!.itemCount).isEqualTo(6)

            moreRecyclerView.scrollToPosition(0)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 0,
                more.getString(R.string.more_contact_heading), more.getString(R.string.more_contact_description))

            moreRecyclerView.scrollToPosition(1)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 1,
                more.getString(R.string.more_rating_heading), more.getString(R.string.more_rating_description))

            moreRecyclerView.scrollToPosition(2)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 2,
                more.getString(R.string.more_statistics_heading), more.getString(R.string.more_statistics_description))

            moreRecyclerView.scrollToPosition(3)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 3,
                more.getString(R.string.more_export_import_heading), more.getString(R.string.more_export_import_description))

            moreRecyclerView.scrollToPosition(4)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 4,
                more.getString(R.string.more_software_heading), more.getString(R.string.more_software_description))

            moreRecyclerView.scrollToPosition(5)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(moreRecyclerView, 5,
                more.getString(R.string.more_privacy_heading), more.getString(R.string.more_privacy_description))

            val textViewVersion = more.findViewById<TextView>(R.id.text_view_more_version)
            assertThat(textViewVersion.text).isEqualTo(more.getString(R.string.more_version, BuildConfig.VERSION_NAME))
        }
    }

    @Test
    fun navigateToContact() {
        val positionContact = 0

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionContact)

            val expected = Intent(more, Contact::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigateToRating() {
        val positionRating = 1

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionRating)

            val expected = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + more.packageName))
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.data).isEqualTo(expected.data)
        }
    }

    @Test
    fun navigateToStatistics() {
        val positionStatistics = 2

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionStatistics)

            val expected = Intent(more, Statistics::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigateToExportImport() {
        val positionExportImport = 3

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionExportImport)

            val expected = Intent(more, ExportImport::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigateToSoftware() {
        val positionSoftware = 4

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionSoftware)

            val expected = Intent(more, Software::class.java)
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.component).isEqualTo(expected.component)
        }
    }

    @Test
    fun navigateToPrivacyPolicy() {
        val positionPrivacy = 5

        moreActivityScenario!!.onActivity { more: More ->
            val moreRecyclerView = more.findViewById<RecyclerView>(R.id.recycler_view_more)

            clickAtPositionRecyclerView(moreRecyclerView, positionPrivacy)

            val expected = Intent(Intent.ACTION_VIEW, Uri.parse(more.getString(R.string.more_privacy_policy_url)))
            val actual = Shadows.shadowOf(ApplicationProvider.getApplicationContext<Context>() as Application).nextStartedActivity

            assertThat(actual.data).isEqualTo(expected.data)
        }
    }

    private fun clickAtPositionRecyclerView(recyclerView: RecyclerView, position: Int) {
        recyclerView.scrollToPosition(position)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        itemView.performClick()
    }

    private fun checkHeaderAndPositionAtPositionInRecyclerView(recyclerView: RecyclerView,
                                                               position: Int, header: String,
                                                               description: String) {
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        val textViewHeading = itemView.findViewById<TextView>(R.id.text_view_recycler_view_heading)
        assertThat(textViewHeading.text).hasToString(header)
        val textViewDescription = itemView.findViewById<TextView>(R.id.text_view_recycler_view_description)
        assertThat(textViewDescription.text).hasToString(description)
    }
}