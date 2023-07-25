package coolpharaoh.tee.speicher.tea.timer.core.counter

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
class CounterDaoTest {

    private var counterDao: CounterDao? = null
    private var teaDAO: TeaDao? = null
    private var db: TeaMemoryDatabase? = null

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, TeaMemoryDatabase::class.java).build()
        counterDao = db!!.counterDao
        teaDAO = db!!.teaDao
    }

    @After
    fun closeDb() {
        db!!.close()
    }

    @Test
    fun insertCounter() {
        val teaId = teaDAO!!.insert(createTea("name"))

        assertThat(counterDao!!.getCounters()).hasSize(0)

        val counterBefore = Counter(teaId, 1, 2, 3, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterBefore)

        assertThat(counterDao!!.getCounters()).hasSize(1)

        val counterAfter = counterDao!!.getCounterByTeaId(teaId)
        assertThat(counterAfter).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(counterBefore)
    }

    @Test
    fun updateCounter() {
        val teaId = teaDAO!!.insert(createTea("name"))

        val counterBefore = Counter(teaId, 1, 2, 3, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterBefore)

        assertThat(counterDao!!.getCounters()).hasSize(1)

        val counterUpdate = counterDao!!.getCounterByTeaId(teaId)
        counterUpdate!!.week = 4
        counterUpdate.month = 3
        counterUpdate.year = 5
        counterDao!!.update(counterUpdate)

        val counterAfter = counterDao!!.getCounterByTeaId(teaId)
        assertThat(counterAfter).usingRecursiveComparison().isEqualTo(counterUpdate)
    }

    @Test
    fun getTeaCounterOverall() {
        val teas = insertTeas()

        val counterA = Counter(teas[0].id!!, 4, 4, 4, 1, getDate(), getDate(), getDate())
        counterDao!!.insert(counterA)
        val counterB = Counter(teas[1].id!!, 3, 3, 3, 2, getDate(), getDate(), getDate())
        counterDao!!.insert(counterB)
        val counterC = Counter(teas[2].id!!, 2, 2, 2, 3, getDate(), getDate(), getDate())
        counterDao!!.insert(counterC)
        val counterD = Counter(teas[3].id!!, 1, 1, 1, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterD)
        val counterE = Counter(teas[4].id!!, 0, 0, 0, 0, getDate(), getDate(), getDate())
        counterDao!!.insert(counterE)

        val counterOverall = counterDao!!.getTeaCounterOverall()

        assertThat(counterOverall).hasSize(4)

        assertThat(counterOverall[3].counter).isEqualTo(counterD.overall)
        assertThat(counterOverall[3].teaName).isEqualTo(teas[3].name)
        assertThat(counterOverall[3].teaColor).isEqualTo(teas[3].color)
        assertThat(counterOverall[2].counter).isEqualTo(counterC.overall)
        assertThat(counterOverall[2].teaName).isEqualTo(teas[2].name)
        assertThat(counterOverall[2].teaColor).isEqualTo(teas[2].color)
        assertThat(counterOverall[1].counter).isEqualTo(counterB.overall)
        assertThat(counterOverall[1].teaName).isEqualTo(teas[1].name)
        assertThat(counterOverall[1].teaColor).isEqualTo(teas[1].color)
        assertThat(counterOverall[0].counter).isEqualTo(counterA.overall)
        assertThat(counterOverall[0].teaName).isEqualTo(teas[0].name)
        assertThat(counterOverall[0].teaColor).isEqualTo(teas[0].color)
    }

    private fun insertTeas(): List<Tea> {
        teaDAO!!.insert(createTea("A"))
        teaDAO!!.insert(createTea("B"))
        teaDAO!!.insert(createTea("C"))
        teaDAO!!.insert(createTea("D"))
        teaDAO!!.insert(createTea("E"))
        return teaDAO!!.getTeas()
    }

    @Test
    fun getTeaCounterYear() {
        val teas = insertTeas()

        val counterA = Counter(teas[0].id!!, 4, 4, 1, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterA)
        val counterB = Counter(teas[1].id!!, 3, 3, 3, 3, getDate(), getDate(), getDate())
        counterDao!!.insert(counterB)
        val counterC = Counter(teas[2].id!!, 2, 2, 2, 2, getDate(), getDate(), getDate())
        counterDao!!.insert(counterC)
        val counterD = Counter(teas[3].id!!, 1, 1, 4, 1, getDate(), getDate(), getDate())
        counterDao!!.insert(counterD)
        val counterE = Counter(teas[4].id!!, 0, 0, 0, 0, getDate(), getDate(), getDate())
        counterDao!!.insert(counterE)

        val counterMonth = counterDao!!.getTeaCounterYear()

        assertThat(counterMonth).hasSize(4)

        assertThat(counterMonth[3].counter).isEqualTo(counterD.year.toLong())
        assertThat(counterMonth[3].teaName).isEqualTo(teas[3].name)
        assertThat(counterMonth[3].teaColor).isEqualTo(teas[3].color)
        assertThat(counterMonth[2].counter).isEqualTo(counterB.year.toLong())
        assertThat(counterMonth[2].teaName).isEqualTo(teas[1].name)
        assertThat(counterMonth[2].teaColor).isEqualTo(teas[1].color)
        assertThat(counterMonth[1].counter).isEqualTo(counterC.year.toLong())
        assertThat(counterMonth[1].teaName).isEqualTo(teas[2].name)
        assertThat(counterMonth[1].teaColor).isEqualTo(teas[2].color)
        assertThat(counterMonth[0].counter).isEqualTo(counterA.year.toLong())
        assertThat(counterMonth[0].teaName).isEqualTo(teas[0].name)
        assertThat(counterMonth[0].teaColor).isEqualTo(teas[0].color)
    }

    @Test
    fun getTeaCounterMonth() {
        val teas = insertTeas()

        val counterA = Counter(teas[0].id!!, 4, 3, 4, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterA)
        val counterB = Counter(teas[1].id!!, 3, 2, 3, 3, getDate(), getDate(), getDate())
        counterDao!!.insert(counterB)
        val counterC = Counter(teas[2].id!!, 2, 1, 2, 2, getDate(), getDate(), getDate())
        counterDao!!.insert(counterC)
        val counterD = Counter(teas[3].id!!, 1, 4, 1, 1, getDate(), getDate(), getDate())
        counterDao!!.insert(counterD)
        val counterE = Counter(teas[4].id!!, 0, 0, 0, 0, getDate(), getDate(), getDate())
        counterDao!!.insert(counterE)

        val counterWeek = counterDao!!.getTeaCounterMonth()

        assertThat(counterWeek).hasSize(4)

        assertThat(counterWeek[3].counter).isEqualTo(counterD.month.toLong())
        assertThat(counterWeek[3].teaName).isEqualTo(teas[3].name)
        assertThat(counterWeek[3].teaColor).isEqualTo(teas[3].color)
        assertThat(counterWeek[2].counter).isEqualTo(counterA.month.toLong())
        assertThat(counterWeek[2].teaName).isEqualTo(teas[0].name)
        assertThat(counterWeek[2].teaColor).isEqualTo(teas[0].color)
        assertThat(counterWeek[1].counter).isEqualTo(counterB.month.toLong())
        assertThat(counterWeek[1].teaName).isEqualTo(teas[1].name)
        assertThat(counterWeek[1].teaColor).isEqualTo(teas[1].color)
        assertThat(counterWeek[0].counter).isEqualTo(counterC.month.toLong())
        assertThat(counterWeek[0].teaName).isEqualTo(teas[2].name)
        assertThat(counterWeek[0].teaColor).isEqualTo(teas[2].color)
    }

    @Test
    fun getTeaCounterWeek() {
        val teas = insertTeas()

        val counterA = Counter(teas[0].id!!, 2, 4, 4, 4, getDate(), getDate(), getDate())
        counterDao!!.insert(counterA)
        val counterB = Counter(teas[1].id!!, 4, 3, 3, 3, getDate(), getDate(), getDate())
        counterDao!!.insert(counterB)
        val counterC = Counter(teas[2].id!!, 3, 2, 2, 2, getDate(), getDate(), getDate())
        counterDao!!.insert(counterC)
        val counterD = Counter(teas[3].id!!, 1, 1, 1, 1, getDate(), getDate(), getDate())
        counterDao!!.insert(counterD)
        val counterE = Counter(teas[4].id!!, 0, 0, 0, 0, getDate(), getDate(), getDate())
        counterDao!!.insert(counterE)

        val counterDay = counterDao!!.getTeaCounterWeek()

        assertThat(counterDay).hasSize(4)

        assertThat(counterDay[3].counter).isEqualTo(counterB.week.toLong())
        assertThat(counterDay[3].teaName).isEqualTo(teas[1].name)
        assertThat(counterDay[3].teaColor).isEqualTo(teas[1].color)
        assertThat(counterDay[2].counter).isEqualTo(counterC.week.toLong())
        assertThat(counterDay[2].teaName).isEqualTo(teas[2].name)
        assertThat(counterDay[2].teaColor).isEqualTo(teas[2].color)
        assertThat(counterDay[1].counter).isEqualTo(counterA.week.toLong())
        assertThat(counterDay[1].teaName).isEqualTo(teas[0].name)
        assertThat(counterDay[1].teaColor).isEqualTo(teas[0].color)
        assertThat(counterDay[0].counter).isEqualTo(counterD.week.toLong())
        assertThat(counterDay[0].teaName).isEqualTo(teas[3].name)
        assertThat(counterDay[0].teaColor).isEqualTo(teas[3].color)
    }

    private fun createTea(name: String): Tea {
        return Tea(name, "variety", 3.0, "ts", 15, 0, getDate())
    }
}