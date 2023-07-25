package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ThrowingConsumer
import org.assertj.core.groups.Tuple
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

@RunWith(AndroidJUnit4::class)
class TeaDaoTest {
    private var teaDao: TeaDao? = null
    private var db: TeaMemoryDatabase? = null

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = inMemoryDatabaseBuilder(context, TeaMemoryDatabase::class.java).build()
        teaDao = db!!.teaDao
    }

    @After
    fun closeDb() {
        db!!.close()
    }

    @Test
    fun insertTea() {
        assertThat(teaDao!!.getTeas()).isEmpty()

        val teaBefore = createTea("name", VARIETY, getDate())
        teaDao!!.insert(teaBefore)

        assertThat(teaDao!!.getTeas()).hasSize(1)

        val teaAfter = teaDao!!.getTeas()[0]
        assertThat(teaAfter).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore)
    }

    @Test
    fun updateTea() {
        assertThat(teaDao!!.getTeas()).isEmpty()

        val teaBefore = createTea("name", VARIETY, getDate())
        teaDao!!.insert(teaBefore)

        assertThat(teaDao!!.getTeas()).hasSize(1)

        val teaUpdate = teaDao!!.getTeas()[0]
        teaUpdate.name = "NameChanged"
        teaUpdate.name = "VarietyChanged"
        teaDao!!.update(teaUpdate)

        val teaAfter = teaDao!!.getTeas()[0]
        assertThat(teaAfter).usingRecursiveComparison().isEqualTo(teaUpdate)
    }

    @Test
    fun deleteTeaById() {
        assertThat(teaDao!!.getTeas()).isEmpty()

        val teaBefore1 = createTea("name1", VARIETY, getDate())
        teaDao!!.insert(teaBefore1)

        val teaBefore2 = createTea("name2", VARIETY, getDate())
        teaDao!!.insert(teaBefore2)

        assertThat(teaDao!!.getTeas()).hasSize(2)

        teaDao!!.deleteTeaById(teaDao!!.getTeas()[0].id!!)

        assertThat(teaDao!!.getTeas()).hasSize(1)

        val teaAfter2 = teaDao!!.getTeas()[0]
        assertThat(teaAfter2).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore2)
    }

    @Test
    fun deleteAllTeas() {
        assertThat(teaDao!!.getTeas()).isEmpty()

        val teaBefore1 = createTea("name1", VARIETY, getDate())
        teaDao!!.insert(teaBefore1)

        val teaBefore2 = createTea("name2", VARIETY, getDate())
        teaDao!!.insert(teaBefore2)

        assertThat(teaDao!!.getTeas()).hasSize(2)

        teaDao!!.deleteAll()

        assertThat(teaDao!!.getTeas()).isEmpty()
    }

    @Test
    fun getTeaById() {
        val teaBefore1 = createTea("nameOld", VARIETY,
            GregorianCalendar(2016, Calendar.FEBRUARY, 22).time)
        val teaId1 = teaDao!!.insert(teaBefore1)

        val teaBefore2 = createTea("nameMiddle", VARIETY,
            GregorianCalendar(2018, Calendar.FEBRUARY, 11).time)
        val teaId2 = teaDao!!.insert(teaBefore2)

        val teaAfter1 = teaDao!!.getTeaById(teaId1)
        assertThat(teaAfter1).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore1)

        val teaAfter2 = teaDao!!.getTeaById(teaId2)
        assertThat(teaAfter2).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore2)
    }

    @Test
    fun getTeasOrderByActivity() {
        val teaOld = createTea("nameOld", VARIETY,
            GregorianCalendar(2016, Calendar.FEBRUARY, 22).time)
        teaDao!!.insert(teaOld)
        val teaMiddle = createTea("nameMiddle", VARIETY,
            GregorianCalendar(2018, Calendar.FEBRUARY, 11).time)
        teaDao!!.insert(teaMiddle)
        val teaNew = createTea("nameNew", VARIETY,
            GregorianCalendar(2018, Calendar.DECEMBER, 15).time)
        teaDao!!.insert(teaNew)

        val teaList = teaDao!!.getTeasOrderByActivity()

        assertThat(teaList)
            .extracting(
                Tea::name,
                Tea::variety,
                Tea::date
            )
            .containsSequence(
                Tuple.tuple(
                    teaNew.name,
                    teaNew.variety,
                    teaNew.date
                ),
                Tuple.tuple(
                    teaMiddle.name,
                    teaMiddle.variety,
                    teaMiddle.date
                ),
                Tuple.tuple(
                    teaOld.name,
                    teaOld.variety,
                    teaOld.date
                )
            )
    }

    @Test
    fun getTeasStockOrderByActivity() {
        val teaOld = createTea("nameOld", VARIETY,
            GregorianCalendar(2016, Calendar.FEBRUARY, 22).time)
        teaDao!!.insert(teaOld)
        val teaMiddle = createTea("nameMiddle", VARIETY,
            GregorianCalendar(2018, Calendar.FEBRUARY, 11).time)
        teaMiddle.inStock = true
        teaDao!!.insert(teaMiddle)
        val teaNew = createTea("nameNew", VARIETY,
            GregorianCalendar(2018, Calendar.DECEMBER, 15).time)
        teaDao!!.insert(teaNew)

        val teaList = teaDao!!.getTeasInStockOrderByActivity()

        assertThat(teaList).hasSize(1)
    }

    @Test
    fun getTeasOrderByAlphabetic() {
        val teaC = createTea("nameC", VARIETY, getDate())
        teaDao!!.insert(teaC)
        val teaA = createTea("nameA", VARIETY, getDate())
        teaDao!!.insert(teaA)
        val teaB = createTea("nameB", VARIETY, getDate())
        teaDao!!.insert(teaB)

        val teaList = teaDao!!.getTeasOrderByAlphabetic()

        assertThat(teaList)
            .extracting(
                Tea::name,
                Tea::variety,
                Tea::date
            )
            .containsSequence(
                Tuple.tuple(
                    teaA.name,
                    teaA.variety,
                    teaA.date
                ),
                Tuple.tuple(
                    teaB.name,
                    teaB.variety,
                    teaB.date
                ),
                Tuple.tuple(
                    teaC.name,
                    teaC.variety,
                    teaC.date
                )
            )
    }

    @Test
    fun getTeasInStockOrderByAlphabetic() {
        val teaC = createTea("nameC", VARIETY, getDate())
        teaDao!!.insert(teaC)
        val teaA = createTea("nameA", VARIETY, getDate())
        teaA.inStock = true
        teaDao!!.insert(teaA)
        val teaB = createTea("nameB", VARIETY, getDate())
        teaDao!!.insert(teaB)

        val teaList = teaDao!!.getTeasInStockOrderByAlphabetic()

        assertThat(teaList).hasSize(1)
    }

    @Test
    fun getTeasOrderByVariety() {
        val teaC = createTea("name", "varietyC", getDate())
        teaDao!!.insert(teaC)
        val teaA = createTea("name", "varietyA", getDate())
        teaDao!!.insert(teaA)
        val teaB = createTea("name", "varietyB", getDate())
        teaDao!!.insert(teaB)

        val teaList = teaDao!!.getTeasOrderByVariety()

        assertThat(teaList)
            .extracting(
                Tea::name,
                Tea::variety,
                Tea::date
            )
            .containsSequence(
                Tuple.tuple(
                    teaA.name,
                    teaA.variety,
                    teaA.date
                ),
                Tuple.tuple(
                    teaB.name,
                    teaB.variety,
                    teaB.date
                ),
                Tuple.tuple(
                    teaC.name,
                    teaC.variety,
                    teaC.date
                )
            )
    }

    @Test
    fun getTeasInStockOrderByVariety() {
        val teaC = createTea("name", "varietyC", getDate())
        teaDao!!.insert(teaC)
        val teaA = createTea("name", "varietyA", getDate())
        teaA.inStock = true
        teaDao!!.insert(teaA)
        val teaB = createTea("name", "varietyB", getDate())
        teaDao!!.insert(teaB)

        val teaList = teaDao!!.getTeasInStockOrderByVariety()

        assertThat(teaList).hasSize(1)
    }

    @Test
    fun getTeasOrderByRating() {
        val tea3 = createTea("rating3", VARIETY, getDate(), 4)
        teaDao!!.insert(tea3)
        val tea5 = createTea("rating5", VARIETY, getDate(), 5)
        teaDao!!.insert(tea5)
        val tea1 = createTea("rating1", VARIETY, getDate(), 1)
        teaDao!!.insert(tea1)

        val teaList = teaDao!!.getTeasOrderByRating()

        assertThat(teaList)
            .extracting(
                Tea::name,
                Tea::rating
            )
            .containsSequence(
                Tuple.tuple(
                    tea5.name,
                    tea5.rating
                ),
                Tuple.tuple(
                    tea3.name,
                    tea3.rating
                ),
                Tuple.tuple(
                    tea1.name,
                    tea1.rating
                )
            )
    }

    @Test
    fun getTeasInStockOrderByRating() {
        val tea3 = createTea("rating3", VARIETY, getDate(), 4)
        teaDao!!.insert(tea3)
        val tea5 = createTea("rating5", VARIETY, getDate(), 5)
        tea5.inStock = true
        teaDao!!.insert(tea5)
        val tea1 = createTea("rating1", VARIETY, getDate(), 1)
        teaDao!!.insert(tea1)

        val teaList = teaDao!!.getTeasInStockOrderByRating()

        assertThat(teaList).hasSize(1)
    }

    @Test
    fun getRandomTeaInStock() {
        val tea1 = createTea("tea1", VARIETY, getDate())
        teaDao!!.insert(tea1)
        val tea2 = createTea("tea2", VARIETY, getDate())
        tea2.inStock = true
        teaDao!!.insert(tea2)
        val tea3 = createTea("tea3", VARIETY, getDate())
        tea3.inStock = true
        teaDao!!.insert(tea3)

        val randomTea = teaDao!!.getRandomTeaInStock()

        assertThat(randomTea!!.inStock).isTrue
        assertThat(randomTea.name)
            .satisfiesAnyOf(
                ThrowingConsumer { s: String? ->
                    assertThat(s).isEqualTo(tea2.name)
                },
                ThrowingConsumer { s: String? ->
                    assertThat(s).isEqualTo(tea3.name)
                }
            )
    }

    @Test
    fun getNoRandomTeaInStock() {
        val tea1 = createTea("tea1", VARIETY, getDate())
        teaDao!!.insert(tea1)
        val tea2 = createTea("tea2", VARIETY, getDate())
        teaDao!!.insert(tea2)
        val tea3 = createTea("tea3", VARIETY, getDate())
        teaDao!!.insert(tea3)

        val randomTea = teaDao!!.getRandomTeaInStock()

        assertThat(randomTea).isNull()
    }

    @Test
    fun getTeasBySearchString() {
        val teaA = createTea("A", VARIETY, getDate())
        teaDao!!.insert(teaA)
        val teaC = createTea("nameC", VARIETY, getDate())
        teaDao!!.insert(teaC)
        val teaB = createTea("nameB", VARIETY, getDate())
        teaDao!!.insert(teaB)

        val teaList = teaDao!!.getTeasBySearchString("name")

        assertThat(teaList)
            .extracting(
                Tea::name,
                Tea::variety,
                Tea::date
            )
            .containsSequence(
                Tuple.tuple(
                    teaB.name,
                    teaB.variety,
                    teaB.date
                ),
                Tuple.tuple(
                    teaC.name,
                    teaC.variety,
                    teaC.date
                )
            )
    }

    private fun createTea(name: String, variety: String, date: Date, rating: Int = 0): Tea {
        val tea = Tea(name, variety, 3.0, "ts", 15, 0, date)
        tea.rating = rating
        return tea
    }

    companion object {
        private const val VARIETY = "variety"
    }
}