package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import org.assertj.core.api.Assertions.*
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class RecyclerItemsHeaderStrategyVarietyTest {
    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @BeforeEach
    fun setUp() {
        `when`(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(VARIETIES)
        `when`(application.resources).thenReturn(resources)
    }

    @Test
    fun generateRecyclerItemsHeader() {
        val teas = createTeas()

        val recyclerItemsHeader: RecyclerItemsHeaderStrategy = RecyclerItemsHeaderStrategyVariety(application)
        val recyclerItems = recyclerItemsHeader.generateFrom(teas)

        assertThat(recyclerItems)
            .extracting(
                RecyclerItemOverview::teaId,
                RecyclerItemOverview::teaName,
                RecyclerItemOverview::variety,
                RecyclerItemOverview::color,
                RecyclerItemOverview::favorite,
                RecyclerItemOverview::category
            ).contains(
                Tuple.tuple(null, null, null, null, false, "- " + teas[0].variety + " -"),
                Tuple.tuple(teas[0].id, teas[0].name, teas[0].variety, teas[0].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- " + teas[1].variety + " -"),
                Tuple.tuple(teas[1].id, teas[1].name, teas[1].variety, teas[1].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- " + teas[2].variety + " -"),
                Tuple.tuple(teas[2].id, teas[2].name, teas[2].variety, teas[2].color, true, null)
            )
    }

    private fun createTeas(): ArrayList<Tea> {
        val teas = ArrayList<Tea>()
        for (i in 0..2) {
            val tea = Tea()
            tea.id = i.toLong()
            tea.name = "TEA" + i + 1
            tea.variety = "VARIETY" + i + 1
            tea.color = i
            tea.inStock = true
            teas.add(tea)
        }
        return teas
    }

    companion object {
        private val VARIETIES = arrayOf(
            "Black tea",
            "Green tea",
            "Yellow tea",
            "White tea",
            "Oolong tea",
            "Pu-erh tea",
            "Herbal tea",
            "Fruit tea",
            "Rooibus tea",
            "Other"
        )
    }
}