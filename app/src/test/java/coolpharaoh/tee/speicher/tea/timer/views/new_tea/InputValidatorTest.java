package coolpharaoh.tee.speicher.tea.timer.views.new_tea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import android.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InputValidatorTest {
    @Mock
    private Application application;

    private InputValidator inputValidator;

    @BeforeEach
    void setUp() {
        inputValidator = new InputValidator(application, System.out::println);
    }

    @Test
    void nameIsNotEmptyReturnsFalse() {
        when(application.getString(anyInt())).thenReturn("ErrorMessage");

        assertThat(inputValidator.nameIsNotEmpty("")).isFalse();
    }

    @Test
    void nameIsNotEmptyReturnsTrue() {
        assertThat(inputValidator.nameIsNotEmpty("Tea")).isTrue();
    }

    @Test
    void nameIsValidReturnsFalse() {
        when(application.getString(anyInt())).thenReturn("ErrorMessage");

        final char[] data = new char[350];
        final String largeName = new String(data);
        assertThat(inputValidator.nameIsValid(largeName)).isFalse();
    }

    @Test
    void nameIsValidReturnsTrue() {
        assertThat(inputValidator.nameIsValid("Tea")).isTrue();
    }

    @Test
    void varietyIsValidReturnsFalse() {
        when(application.getString(anyInt())).thenReturn("ErrorMessage");

        final char[] data = new char[50];
        final String largeVariety = new String(data);
        assertThat(inputValidator.varietyIsValid(largeVariety)).isFalse();
    }

    @Test
    void varietyIsValidReturnsTrue() {
        assertThat(inputValidator.varietyIsValid("Variety")).isTrue();
    }
}
