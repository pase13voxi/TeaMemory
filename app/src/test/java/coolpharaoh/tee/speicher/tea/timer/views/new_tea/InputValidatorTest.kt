package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.core.print.Printer
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class InputValidatorTest {
    @MockK
    lateinit var application: Application

    private var inputValidator: InputValidator? = null

    @BeforeEach
    fun setUp() {
        inputValidator = InputValidator(application, object : Printer { override fun print(message: String?) { println(message) } })
    }

    @Test
    fun nameIsNotEmptyReturnsFalse() {
         every { application.getString(any()) } returns "ErrorMessage"

        assertThat(inputValidator!!.nameIsNotEmpty("")).isFalse
    }

    @Test
    fun nameIsNotEmptyReturnsTrue() {
        assertThat(inputValidator!!.nameIsNotEmpty("Tea")).isTrue
    }

    @Test
    fun nameIsValidReturnsFalse() {
         every { application.getString(any()) } returns "ErrorMessage"

        val data = CharArray(350)
        val largeName = String(data)
        assertThat(inputValidator!!.nameIsValid(largeName)).isFalse
    }

    @Test
    fun nameIsValidReturnsTrue() {
        assertThat(inputValidator!!.nameIsValid("Tea")).isTrue
    }

    @Test
    fun varietyIsValidReturnsFalse() {
         every { application.getString(any()) } returns "ErrorMessage"

        val data = CharArray(50)
        val largeVariety = String(data)
        assertThat(inputValidator!!.varietyIsValid(largeVariety)).isFalse
    }

    @Test
    fun varietyIsValidReturnsTrue() {
        assertThat(inputValidator!!.varietyIsValid("Variety")).isTrue
    }
}