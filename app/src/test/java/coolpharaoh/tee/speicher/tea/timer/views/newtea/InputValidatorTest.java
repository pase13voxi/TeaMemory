package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

// allow system.out.println
@SuppressWarnings("java:S106")
@RunWith(MockitoJUnitRunner.class)
public class InputValidatorTest {

    public static final String CELSIUS = "Celsius";
    public static final String FAHRENHEIT = "Fahrenheit";
    public static final String COOL_DOWN_TIME = "02:00";
    public static final String TIME = "05:00";
    @Mock
    private Application application;

    private InputValidator inputValidator;

    @Before
    public void setUp() {
        when(application.getString(anyInt())).thenReturn("ErrorMessage");
        inputValidator = new InputValidator(application, System.out::println);
    }

    @Test
    public void nameIsNotEmptyReturnsFalse() {
        assertThat(inputValidator.nameIsNotEmpty("")).isFalse();
    }

    @Test
    public void nameIsNotEmptyReturnsTrue() {
        assertThat(inputValidator.nameIsNotEmpty("Tea")).isTrue();
    }

    @Test
    public void nameIsValidReturnsFalse() {
        char[] data = new char[350];
        String largeName = new String(data);
        assertThat(inputValidator.nameIsValid(largeName)).isFalse();
    }

    @Test
    public void nameIsValidReturnsTrue() {
        assertThat(inputValidator.nameIsValid("Tea")).isTrue();
    }

    @Test
    public void varietyIsValidReturnsFalse() {
        char[] data = new char[50];
        String largeVariety = new String(data);
        assertThat(inputValidator.varietyIsValid(largeVariety)).isFalse();
    }

    @Test
    public void varietyIsValidReturnsTrue() {
        assertThat(inputValidator.varietyIsValid("Variety")).isTrue();
    }

    @Test
    public void amountIsValidWithAmountContainsPointReturnsFalse() {
        assertThat(inputValidator.amountIsValid("1.2")).isFalse();
    }

    @Test
    public void amountIsValidWithAmountTooLargeReturnsFalse() {
        assertThat(inputValidator.amountIsValid("1332")).isFalse();
    }

    @Test
    public void amountIsValidWithAmountEmptyReturnsTrue() {
        assertThat(inputValidator.amountIsValid("")).isTrue();
    }

    @Test
    public void amountIsValidReturnsTrue() {
        assertThat(inputValidator.amountIsValid("123")).isTrue();
    }

    @Test
    public void infusionIsValidWithAllInputStringEmptyReturnsTrue() {
        String temperature = "";
        String coolDownTime = "";
        String time = "";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, coolDownTime, time)).isTrue();
    }

    @Test
    public void infusionIsValidWithTemperatureContainsPointReturnsTrue() {
        String temperature = "1.2";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, TIME)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureStringTooLongReturnsTrue() {
        String temperature = "1000";
        assertThat(inputValidator.infusionIsValid(temperature, FAHRENHEIT, COOL_DOWN_TIME, TIME)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureCelsiusTooLargeReturnsTrue() {
        String temperature = "150";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, TIME)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureFahrenheitTooLargeReturnsTrue() {
        String temperature = "250";
        assertThat(inputValidator.infusionIsValid(temperature, FAHRENHEIT, COOL_DOWN_TIME, TIME)).isFalse();
    }

    @Test
    public void infusionIsValidWithTimeWrongFormatMinutesReturnsFalse() {
        String temperature = "70";
        String coolDownTime = "020";
        String time = "005:00";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTimeWrongFormatSecondsReturnsFalse() {
        String temperature = "70";
        String time = "005:00";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTime69MinutesReturnsFalse() {
        String temperature = "70";
        String time = "69";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTime69MinutesWithSecondsReturnsFalse() {
        String temperature = "70";
        String time = "69:00";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithCoolDownTimeWrongFormatWithSecondsReturnsFalse() {
        String temperature = "70";
        String coolDownTime = "444";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, coolDownTime, TIME)).isFalse();
    }

    @Test
    public void infusionIsValidReturnsTrue() {
        String temperature = "70";
        assertThat(inputValidator.infusionIsValid(temperature, CELSIUS, COOL_DOWN_TIME, TIME)).isTrue();
    }
}
