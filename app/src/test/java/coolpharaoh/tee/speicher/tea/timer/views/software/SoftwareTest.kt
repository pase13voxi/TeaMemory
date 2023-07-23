package coolpharaoh.tee.speicher.tea.timer.views.software

import android.os.Looper
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import coolpharaoh.tee.speicher.tea.timer.R
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SoftwareTest {
    private var softwareActivityScenario: ActivityScenario<Software>? = null
    @Before
    fun setUp() {
        softwareActivityScenario = ActivityScenario.launch(Software::class.java)
    }

    @Test
    fun expectSoftwareList() {
        softwareActivityScenario!!.onActivity { software: Software ->
            val softwareRecyclerView =
                software.findViewById<RecyclerView>(R.id.recycler_view_software)
            Assertions.assertThat(softwareRecyclerView.adapter!!.itemCount).isEqualTo(5)
            softwareRecyclerView.scrollToPosition(0)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(
                softwareRecyclerView,
                0,
                software.getString(R.string.software_color_picker_heading),
                software.getString(R.string.software_color_picker_description)
            )
            softwareRecyclerView.scrollToPosition(1)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(
                softwareRecyclerView,
                1,
                software.getString(R.string.software_chart_heading),
                software.getString(R.string.software_chart_description)
            )
            softwareRecyclerView.scrollToPosition(2)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(
                softwareRecyclerView,
                2,
                software.getString(R.string.software_glide_heading),
                software.getString(R.string.software_glide_description)
            )
            softwareRecyclerView.scrollToPosition(3)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(
                softwareRecyclerView,
                3,
                software.getString(R.string.software_junit5_heading),
                software.getString(R.string.software_junit5_description)
            )
            softwareRecyclerView.scrollToPosition(4)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
            checkHeaderAndPositionAtPositionInRecyclerView(
                softwareRecyclerView,
                4,
                software.getString(R.string.software_gson_heading),
                software.getString(R.string.software_gson_description)
            )
        }
    }

    private fun checkHeaderAndPositionAtPositionInRecyclerView(
        recyclerView: RecyclerView, position: Int,
        header: String, description: String
    ) {
        val itemView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
        val textViewHeading = itemView.findViewById<TextView>(R.id.text_view_recycler_view_heading)
        Assertions.assertThat(textViewHeading.text).hasToString(header)
        val textViewDescription =
            itemView.findViewById<TextView>(R.id.text_view_recycler_view_description)
        Assertions.assertThat(textViewDescription.text).hasToString(description)
    }
}