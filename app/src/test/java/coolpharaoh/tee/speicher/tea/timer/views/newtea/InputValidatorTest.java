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
}
