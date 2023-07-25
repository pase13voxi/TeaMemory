package coolpharaoh.tee.speicher.tea.timer.core.infusion

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InfusionDaoTest {

    private var mInfusionDAO: InfusionDao? = null
    private var mTeaDAO: TeaDao? = null
    private var db: TeaMemoryDatabase? = null

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, TeaMemoryDatabase::class.java).build()
        mInfusionDAO = db!!.infusionDao
        mTeaDAO = db!!.teaDao
    }

    @After
    fun closeDb() {
        db!!.close()
    }

    @Test
    fun insertInfusion() {
        assertThat(mInfusionDAO!!.getInfusions()).isEmpty()

        val teaId = insertTea()

        val infusionBefore = insertInfusions(teaId, 1)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(1)

        val infusionAfter = mInfusionDAO!!.getInfusions()[0]
        assertThat(infusionAfter).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(infusionBefore[0])
    }

    @Test
    fun getInfusionsByTeaId() {
        assertThat(mInfusionDAO!!.getInfusions()).isEmpty()

        val teaId1 = insertTea()

        val infusionsBefore1 = insertInfusions(teaId1, 2)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(2)

        val teaId2 = insertTea()

        val infusionBefore2 = insertInfusions(teaId2, 1)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(3)

        val infusionAfter1 = mInfusionDAO!!.getInfusionsByTeaId(teaId1)
        assertThat(infusionAfter1).hasSize(2)

        assertThat(infusionAfter1[0]).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(infusionsBefore1[0])

        assertThat(infusionAfter1[1]).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(infusionsBefore1[1])

        val infusionAfter2 = mInfusionDAO!!.getInfusionsByTeaId(teaId2)
        assertThat(infusionAfter2).hasSize(1)

        assertThat(infusionAfter2[0]).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(infusionBefore2[0])
    }

    @Test
    fun deleteInfusionsByTeaId() {
        assertThat(mInfusionDAO!!.getInfusions()).isEmpty()

        val teaId1 = insertTea()

        insertInfusions(teaId1, 2)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(2)

        val teaId2 = insertTea()

        val infusionBefore2 = insertInfusions(teaId2, 1)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(3)

        mInfusionDAO!!.deleteInfusionsByTeaId(teaId1)

        assertThat(mInfusionDAO!!.getInfusions()).hasSize(1)

        val infusionAfter = mInfusionDAO!!.getInfusions()

        assertThat(infusionAfter[0]).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(infusionBefore2[0])
    }

    private fun insertTea(): Long {
        return mTeaDAO!!.insert(Tea("name", "variety", 3.0, "ts", 15, 0, getDate()))
    }

    private fun insertInfusions(teaId1: Long, count: Int): List<Infusion> {
        val infusionBefore1: MutableList<Infusion> = ArrayList()
        for (i in 0 until count) {
            infusionBefore1.add(Infusion(teaId1, i + 1, "03:00", "10:00", 70, 158))
            mInfusionDAO!!.insert(infusionBefore1[i])
        }
        return infusionBefore1
    }
}