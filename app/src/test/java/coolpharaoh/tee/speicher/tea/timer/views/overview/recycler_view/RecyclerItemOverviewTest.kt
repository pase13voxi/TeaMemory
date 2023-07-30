package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RecyclerItemOverviewTest {
    @Test
    fun getImageTextOneWord() {
        val imageText = getImageText("Sencha")
        assertThat(imageText).isEqualTo("S")
    }

    @Test
    fun getImageTextFirstCharSpace() {
        val imageText = getImageText(" Sencha")
        assertThat(imageText).isEqualTo("S")
    }

    @Test
    fun getImageTextTwoWords() {
        val imageText = getImageText("Earl Grey")
        assertThat(imageText).isEqualTo("EG")
    }

    @Test
    fun getImageTextThreeWords() {
        val imageText = getImageText("Pai Mu Tan")
        assertThat(imageText).isEqualTo("PT")
    }

    private fun getImageText(teaName: String): String {
        val itemOverview = RecyclerItemOverview(null, null, teaName, null, null, false)
        return itemOverview.imageText
    }
}