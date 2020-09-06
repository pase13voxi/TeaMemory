package coolpharaoh.tee.speicher.tea.timer.views.newtea;

import android.content.Context;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;


//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class InputValidatorTest {

    private InputValidator inputValidator;

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        inputValidator = new InputValidator(context);
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
        String temperatureUnit = "Celsius";
        String coolDownTime = "";
        String time = "";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isTrue();
    }

    @Test
    public void infusionIsValidWithTemperatureContainsPointReturnsTrue() {
        String temperature = "1.2";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "05:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureStringTooLongReturnsTrue() {
        String temperature = "1000";
        String temperatureUnit = "Fahrenheit";
        String coolDownTime = "02:00";
        String time = "05:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureCelsiusTooLargeReturnsTrue() {
        String temperature = "150";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "05:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTemperatureFahrenheitTooLargeReturnsTrue() {
        String temperature = "250";
        String temperatureUnit = "Fahrenheit";
        String coolDownTime = "02:00";
        String time = "05:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTimeWrongFormatMinutesReturnsFalse() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "020";
        String time = "005:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTimeWrongFormatSecondsReturnsFalse() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "005:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTime69MinutesReturnsFalse() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "69";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithTime69MinutesWithSecondsReturnsFalse() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "69:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidWithCoolDownTimeWrongFormatWithSecondsReturnsFalse() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "444";
        String time = "6";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isFalse();
    }

    @Test
    public void infusionIsValidReturnsTrue() {
        String temperature = "70";
        String temperatureUnit = "Celsius";
        String coolDownTime = "02:00";
        String time = "05:00";
        assertThat(inputValidator.infusionIsValid(temperature, temperatureUnit, coolDownTime, time)).isTrue();
    }
}
